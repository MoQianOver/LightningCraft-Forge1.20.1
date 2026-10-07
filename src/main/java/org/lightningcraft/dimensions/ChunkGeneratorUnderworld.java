package org.lightningcraft.dimensions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.util.LCMisc;
import org.lightningcraft.worldgen.NoiseOctaves;

public class ChunkGeneratorUnderworld extends ChunkGenerator {

	public static final int CHUNK_HEIGHT = 256;

	private static final byte WATER_LEVEL = 32;

	private static final byte H_STEP = 4;

	public static final Codec<ChunkGeneratorUnderworld> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource)
	).apply(instance, ChunkGeneratorUnderworld::new));

	private final Random dimRNG;
	private NoiseOctaves noiseGen1;
	private NoiseOctaves noiseGen2;
	private NoiseOctaves noiseGen3;
	private NoiseOctaves grassAndStoneNoiseGen;
	private NoiseOctaves understoneExclusivityNoiseGen;
	private NoiseOctaves noiseGen6;
	private NoiseOctaves noiseGen7;

	private double[] noiseField;
	private double[] grassNoise = new double[256];
	private double[] stoneNoise = new double[256];
	private double[] understoneExclusivityNoise = new double[256];
	private double[] noiseData1;
	private double[] noiseData2;
	private double[] noiseData3;
	private double[] noiseData4;
	private double[] noiseData5;

	private long cachedChunkKey = Long.MIN_VALUE;
	private BlockState[] cachedBlocks;
	private final Object cacheLock = new Object();

	public ChunkGeneratorUnderworld(BiomeSource biomeSource) {
		super(biomeSource);

		this.dimRNG = new Random(0L);
		RandomSource placeholder = RandomSource.create(0L);
		this.noiseGen1 = new NoiseOctaves(placeholder, 16);
		this.noiseGen2 = new NoiseOctaves(placeholder, 16);
		this.noiseGen3 = new NoiseOctaves(placeholder, 8);
		this.grassAndStoneNoiseGen = new NoiseOctaves(placeholder, 4);
		this.understoneExclusivityNoiseGen = new NoiseOctaves(placeholder, 4);
		this.noiseGen6 = new NoiseOctaves(placeholder, 10);
		this.noiseGen7 = new NoiseOctaves(placeholder, 16);
		this.caveNoiseGen = new NoiseOctaves(placeholder, 4);
		this.terrainNoiseA = new net.minecraft.world.level.levelgen.synth.ImprovedNoise(placeholder);
		this.terrainNoiseB = new net.minecraft.world.level.levelgen.synth.ImprovedNoise(placeholder);
		this.caveNoiseGenB = new NoiseOctaves(placeholder, 4);
	}

	private static final double ENV_SOLID = 1.8D;

	private static final double ENV_OPEN = -0.25D;

	private static final int FLOOR_TOP = 24;
	private static final int FLOOR_END = 40;

	private static final int CEIL_START = 186;
	private static final int CEIL_END = 206;

	private static final double F1 = 0.022D, FY1 = 0.035D, A1 = 1.0D;

	private static final double F2 = 0.055D, FY2 = 0.080D, A2 = 0.3D;

	private net.minecraft.world.level.levelgen.synth.ImprovedNoise terrainNoiseA;
	private net.minecraft.world.level.levelgen.synth.ImprovedNoise terrainNoiseB;

	private static double envelope(int y) {
		if (y < FLOOR_TOP) return ENV_SOLID;
		if (y < FLOOR_END) return ENV_SOLID + (ENV_OPEN - ENV_SOLID) * (y - FLOOR_TOP) / (double) (FLOOR_END - FLOOR_TOP);
		if (y < CEIL_START) return ENV_OPEN;
		if (y < CEIL_END) return ENV_OPEN + (ENV_SOLID - ENV_OPEN) * (y - CEIL_START) / (double) (CEIL_END - CEIL_START);
		return ENV_SOLID;
	}

	private double density(double x, int y, double z) {
		if (this.terrainNoiseA == null || this.terrainNoiseB == null) {
			return envelope(y);
		}
		double n = this.terrainNoiseA.noise(x * F1, y * FY1, z * F1) * A1
				+ this.terrainNoiseB.noise(x * F2 + 100.0D, y * FY2 + 100.0D, z * F2 + 100.0D) * A2;
		return envelope(y) + n;
	}

	private NoiseOctaves caveNoiseGen;
	private boolean noiseSeeded = false;

	private static final double TERRAIN_NOISE_SCALE_DEFAULT = 2048.0D;

	private static double noiseScale() {
		try {
			return org.lightningcraft.config.LCConfig.getUnderworldNoiseScale();
		} catch (Throwable t) {
			return TERRAIN_NOISE_SCALE_DEFAULT;
		}
	}

	private void ensureNoiseSeeded(RandomState randomState) {
		if (this.noiseSeeded) {
			return;
		}
		this.noiseSeeded = true;

		net.minecraft.resources.ResourceLocation key =
				new net.minecraft.resources.ResourceLocation("lightningcraft", "underworld_terrain");
		RandomSource rng = randomState.getOrCreateRandomFactory(key).fromHashOf(key);
		this.dimRNG.setSeed(rng.nextLong());
		this.noiseGen1 = new NoiseOctaves(rng, 16);
		this.noiseGen2 = new NoiseOctaves(rng, 16);
		this.noiseGen3 = new NoiseOctaves(rng, 8);
		this.grassAndStoneNoiseGen = new NoiseOctaves(rng, 4);
		this.understoneExclusivityNoiseGen = new NoiseOctaves(rng, 4);
		this.noiseGen6 = new NoiseOctaves(rng, 10);
		this.noiseGen7 = new NoiseOctaves(rng, 16);
		this.caveNoiseGen = new NoiseOctaves(rng, 4);
		this.terrainNoiseA = new net.minecraft.world.level.levelgen.synth.ImprovedNoise(rng);
		this.terrainNoiseB = new net.minecraft.world.level.levelgen.synth.ImprovedNoise(rng);
		this.caveNoiseGenB = new NoiseOctaves(rng, 4);
	}

	private static final double CAVE_TUNNEL_HALF_WIDTH = 0.22D;

	private NoiseOctaves caveNoiseGenB;

	private void carveCaves(int chunkX, int chunkZ, BlockState[] blocks) {
		if (this.caveNoiseGen == null || this.caveNoiseGenB == null) {
			return;
		}
		double[] caveA = this.caveNoiseGen.generateNoiseOctaves(null,
				chunkX * 16, 0, chunkZ * 16, 16, CHUNK_HEIGHT, 16,
				0.055D, 0.09D, 0.055D);
		double[] caveB = this.caveNoiseGenB.generateNoiseOctaves(null,
				chunkX * 16 + 4096, 0, chunkZ * 16 - 4096, 16, CHUNK_HEIGHT, 16,
				0.055D, 0.09D, 0.055D);

		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				for (int y = 6; y < CHUNK_HEIGHT - 6; ++y) {
					int i = (x * 16 + z) * CHUNK_HEIGHT + y;
					BlockState cur = blocks[i];
					if (cur == null || cur.isAir() || cur.getBlock() == Blocks.BEDROCK) {
						continue;
					}
					if (Math.abs(caveA[i]) < CAVE_TUNNEL_HALF_WIDTH
							&& Math.abs(caveB[i]) < CAVE_TUNNEL_HALF_WIDTH) {
						blocks[i] = (y < WATER_LEVEL)
								? Blocks.WATER.defaultBlockState()
								: Blocks.AIR.defaultBlockState();
					}
				}
			}
		}
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	private static boolean isExposedSurface(BlockState[] blocks, int idx, int y, int depth) {
		for (int d = 1; d <= depth; ++d) {
			int yy = y + d;
			if (yy >= CHUNK_HEIGHT) {
				return false;
			}
			BlockState above = blocks[idx + d];
			if (above != null && !above.isAir()) {
				return false;
			}
		}
		return true;
	}

	private void initBlocks(int chunkX, int chunkZ, BlockState[] blocks) {

		BlockState under = ((org.lightningcraft.blocks.base.BlockVariant) LCBlocks.stoneBlock)
				.getStateFromMeta(BlockStone.UNDER);
		BlockState water = Blocks.WATER.defaultBlockState();
		BlockState air = Blocks.AIR.defaultBlockState();

		int baseX = chunkX * 16;
		int baseZ = chunkZ * 16;
		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				int base = (x * 16 + z) * CHUNK_HEIGHT;
				double wx = baseX + x;
				double wz = baseZ + z;
				for (int y = 0; y < CHUNK_HEIGHT; ++y) {

					boolean solid = y < 6 || y > CHUNK_HEIGHT - 10 || density(wx, y, wz) > 0.0D;
					if (solid) {
						blocks[base + y] = under;
					} else {
						blocks[base + y] = (y < WATER_LEVEL) ? water : air;
					}
				}
			}
		}
	}

	private void replaceBiomeBlocks(int par1, int par2, BlockState[] blocks) {
		short stoneGrassCanGen = CHUNK_HEIGHT - 64;
		short sandCanGen = CHUNK_HEIGHT / 8 - 1;
		double d0 = 0.03125D;
		this.grassNoise = this.grassAndStoneNoiseGen.generateNoiseOctaves(
				this.grassNoise, par1 * 16, par2 * 16, 0, 16, 16, 1, d0, d0, 1.0D);
		this.stoneNoise = this.grassAndStoneNoiseGen.generateNoiseOctaves(
				this.stoneNoise, par1 * 16, 109, par2 * 16, 16, 1, 16, d0, 1.0D, d0);
		this.understoneExclusivityNoise = this.understoneExclusivityNoiseGen.generateNoiseOctaves(
				this.understoneExclusivityNoise, par1 * 16, par2 * 16, 0, 16, 16, 1,
				d0 * 2.0D, d0 * 2.0D, d0 * 2.0D);

		BlockState under = ((org.lightningcraft.blocks.base.BlockVariant) LCBlocks.stoneBlock).getStateFromMeta(BlockStone.UNDER);

		final int surfaceAirDepth = 3;

		for (int k = 0; k < 16; ++k) {
			for (int l = 0; l < 16; ++l) {
				boolean flag = this.grassNoise[k + l * 16] + this.dimRNG.nextDouble() * 0.2D > 0.0D;
				boolean flag1 = this.stoneNoise[k + l * 16] + this.dimRNG.nextDouble() * 0.4D > 0.35D;
				int i1 = (int) (this.understoneExclusivityNoise[k + l * 16] / 3.0D + 3.0D
						+ this.dimRNG.nextDouble() * 0.25D);
				int j1 = -1;
				BlockState block = under;
				BlockState block1 = block;

				for (int y = CHUNK_HEIGHT - 1; y >= 0; --y) {
					int l1 = (l * 16 + k) * CHUNK_HEIGHT + y;

					if (y < (CHUNK_HEIGHT - 1) - this.dimRNG.nextInt(5) && y > this.dimRNG.nextInt(5)) {
						BlockState block2 = blocks[l1];

						if (block2 != null && !block2.isAir()) {
							if (block2 == under) {
								if (j1 == -1) {
									if (i1 <= 0) {
										block = Blocks.AIR.defaultBlockState();
										block1 = under;
									} else if (isExposedSurface(blocks, l1, y, surfaceAirDepth)) {
										block = under;
										block1 = block;

										if (flag1) {

											block = LCBlocks.corruptStone.defaultBlockState();
											block1 = under;
										}

										if (flag) {
											block = Blocks.GRASS_BLOCK.defaultBlockState();
											block1 = Blocks.DIRT.defaultBlockState();
										}
									}

									if (block1 == Blocks.DIRT.defaultBlockState() && y <= sandCanGen) {
										block1 = LCBlocks.underSand.defaultBlockState();
									}

									if (y < stoneGrassCanGen && block.isAir()) {
										if (y <= sandCanGen) {
											block = LCBlocks.underSand.defaultBlockState();
										} else {
											block = Blocks.ICE.defaultBlockState();
										}
									}

									j1 = i1;

									if (y >= stoneGrassCanGen - 1) {
										blocks[l1] = block;
									} else if (y + 1 < CHUNK_HEIGHT
											&& (blocks[l1 + 1] == null || blocks[l1 + 1].isAir())) {
										blocks[l1] = block;
									} else {
										blocks[l1] = block1;
									}
								} else if (j1 > 0) {
									--j1;
									blocks[l1] = block1;
								}
							}
						} else {
							j1 = -1;
						}
					} else {
						blocks[l1] = Blocks.BEDROCK.defaultBlockState();
					}
				}
			}
		}
	}

	private double[] initializeNoiseField(double[] par1, int par2, int par3, int par4,
			int par5, int par6, int par7) {
		if (par1 == null) {
			par1 = new double[par5 * par6 * par7];
		}

		double d0 = 684.412D;
		double d1 = 2053.236D;
		this.noiseData4 = this.noiseGen6.generateNoiseOctaves(this.noiseData4, par2, par3, par4, par5, 1, par7,
				1.0D, 0.0D, 1.0D);
		this.noiseData5 = this.noiseGen7.generateNoiseOctaves(this.noiseData5, par2, par3, par4, par5, 1, par7,
				100.0D, 0.0D, 100.0D);
		this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, par2, par3, par4, par5, par6, par7,
				d0 / 80.0D, d1 / 60.0D, d0 / 80.0D);
		this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, par2, par3, par4, par5, par6, par7,
				d0, d1, d0);
		this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, par2, par3, par4, par5, par6, par7,
				d0, d1, d0);

		int k1 = 0;
		int l1 = 0;
		double[] adouble1 = new double[par6];

		for (int i2 = 0; i2 < par6; ++i2) {
			adouble1[i2] = Math.cos(i2 * Math.PI * 6.0D / par6) * 2.0D;
			double d2 = i2;

			if (i2 > par6 / 2) {
				d2 = par6 - 1 - i2;
			}

			if (d2 < 4.0D) {
				d2 = 4.0D - d2;
				adouble1[i2] -= d2 * d2 * d2 * 10.0D;
			}
		}

		for (int i2 = 0; i2 < par5; ++i2) {
			for (int k2 = 0; k2 < par7; ++k2) {
				double d3 = (this.noiseData4[l1] + 256.0D) / 512.0D;

				if (d3 > 1.0D) {
					d3 = 1.0D;
				}

				double d4 = 0.0D;
				double d5 = this.noiseData5[l1] / 8000.0D;

				if (d5 < 0.0D) {
					d5 = -d5;
				}

				d5 = d5 * 3.0D - 3.0D;

				if (d5 < 0.0D) {
					d5 /= 2.0D;

					if (d5 < -1.0D) {
						d5 = -1.0D;
					}

					d5 /= 1.4D;
					d5 /= 2.0D;
					d3 = 0.0D;
				} else {
					if (d5 > 1.0D) {
						d5 = 1.0D;
					}
					d5 /= 6.0D;
				}

				d3 += 0.5D;
				d5 = d5 * par6 / 16.0D;
				++l1;

				for (int j2 = 0; j2 < par6; ++j2) {
					double d6 = 0.0D;
					double d7 = adouble1[j2];
					double d8 = this.noiseData2[k1] * noiseScale() / 512.0D;
					double d9 = this.noiseData3[k1] * noiseScale() / 512.0D;
					double d10 = (this.noiseData1[k1] / 10.0D + 1.0D) / 2.0D;

					if (d10 < 0.0D) {
						d6 = d8;
					} else if (d10 > 1.0D) {
						d6 = d9;
					} else {
						d6 = d8 + (d9 - d8) * d10;
					}

					d6 -= d7;

					if (j2 > par6 - 4) {
						double d11 = (j2 - (par6 - 4)) / 3.0F;
						d6 = d6 * (1.0D - d11) + -10.0D * d11;
					}

					if (j2 < d4) {
						double d11 = (d4 - j2) / 4.0D;

						if (d11 < 0.0D) {
							d11 = 0.0D;
						}
						if (d11 > 1.0D) {
							d11 = 1.0D;
						}
						d6 = d6 * (1.0D - d11) + -10.0D * d11;
					}

					par1[k1] = d6;
					++k1;
				}
			}
		}

		return par1;
	}

	private BlockState[] generateBlocks(int chunkX, int chunkZ) {
		long key = ChunkPos.asLong(chunkX, chunkZ);
		synchronized (this.cacheLock) {
			if (key == this.cachedChunkKey && this.cachedBlocks != null) {
				return this.cachedBlocks;
			}
		}

		this.dimRNG.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
		BlockState[] blocks = new BlockState[CHUNK_HEIGHT * 256];
		Arrays.fill(blocks, Blocks.AIR.defaultBlockState());
		this.initBlocks(chunkX, chunkZ, blocks);
		this.replaceBiomeBlocks(chunkX, chunkZ, blocks);

		this.carveCaves(chunkX, chunkZ, blocks);

		if (!DIAGNOSTIC_DONE) {
			DIAGNOSTIC_DONE = true;
			this.logDiagnostic(blocks);
		}

		synchronized (this.cacheLock) {
			this.cachedChunkKey = key;
			this.cachedBlocks = blocks;
		}
		return blocks;
	}

	private static volatile boolean DIAGNOSTIC_DONE = false;

	private void logDiagnostic(BlockState[] blocks) {
		int air = 0, stone = 0, water = 0, bedrock = 0, other = 0;
		int topNonAir = -1;
		int airBelow32 = 0;
		BlockState under = ((org.lightningcraft.blocks.base.BlockVariant) LCBlocks.stoneBlock)
				.getStateFromMeta(BlockStone.UNDER);
		for (int y = 0; y < CHUNK_HEIGHT; ++y) {
			for (int xz = 0; xz < 256; ++xz) {
				BlockState s = blocks[xz * CHUNK_HEIGHT + y];
				if (s == null || s.isAir()) {
					air++;
					if (y < 32) airBelow32++;
				} else if (s == under) {
					stone++;
					topNonAir = Math.max(topNonAir, y);
				} else if (s.getBlock() == Blocks.WATER) {
					water++;
					topNonAir = Math.max(topNonAir, y);
				} else if (s.getBlock() == Blocks.BEDROCK) {
					bedrock++;
				} else {
					other++;
					topNonAir = Math.max(topNonAir, y);
				}
			}
		}
		org.lightningcraft.ref.Log.logger.info(
				"[Underworld] 地形诊断：空气=" + air + " 幽界岩=" + stone + " 水=" + water
						+ " 基岩=" + bedrock + " 其它=" + other
						+ " | 最高非空 y=" + topNonAir
						+ " | y<32 的空气格=" + airBelow32
						+ " | 期望：地表≈192、有水、空气占比可观");
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState,
			StructureManager structureManager, ChunkAccess chunk) {

		this.ensureNoiseSeeded(randomState);

		int chunkX = chunk.getPos().x;
		int chunkZ = chunk.getPos().z;
		BlockState[] blocks = this.generateBlocks(chunkX, chunkZ);

		BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
		int minY = this.getMinY();

		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				for (int y = 0; y < CHUNK_HEIGHT; ++y) {

					BlockState state = blocks[(x * 16 + z) * CHUNK_HEIGHT + y];
					if (state != null && !state.isAir()) {
						chunk.setBlockState(mpos.set(x, y + minY, z), state, false);
					}
				}
			}
		}

		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState,
			ChunkAccess chunk) {

	}

	@Override
	public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager,
			StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {

	}

	private static boolean structureSetLogged = false;

	@Override
	public void createStructures(net.minecraft.core.RegistryAccess registryAccess,
			net.minecraft.world.level.chunk.ChunkGeneratorStructureState structureState,
			StructureManager structureManager, ChunkAccess chunk,
			net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager templateManager) {
		if (!structureSetLogged) {
			structureSetLogged = true;
			java.util.List<String> ids = new java.util.ArrayList<>();
			for (net.minecraft.core.Holder<net.minecraft.world.level.levelgen.structure.StructureSet> h
					: structureState.possibleStructureSets()) {
				ids.add(h.unwrapKey().map(k -> k.location().toString()).orElse("?"));
			}
			org.lightningcraft.ref.Log.logger.info("[LC-STRUCT] 游戏认到的 structure_set 数量="
					+ ids.size() + " -> " + ids);
		}
		super.createStructures(registryAccess, structureState, structureManager, chunk, templateManager);
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion region) {

	}

	@Override
	public int getGenDepth() {
		return CHUNK_HEIGHT;
	}

	@Override
	public int getSeaLevel() {
		return WATER_LEVEL;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor,
			RandomState randomState) {
		int chunkX = SectionPos.blockToSectionCoord(x);
		int chunkZ = SectionPos.blockToSectionCoord(z);
		BlockState[] blocks = this.generateBlocks(chunkX, chunkZ);
		int lx = x & 15;
		int lz = z & 15;

		for (int y = CHUNK_HEIGHT - 3; y >= 0; --y) {
			BlockState state = blocks[(lx * 16 + lz) * CHUNK_HEIGHT + y];
			if (state == null || state.isAir()) {
				continue;
			}
			BlockState up1 = blocks[(lx * 16 + lz) * CHUNK_HEIGHT + y + 1];
			BlockState up2 = blocks[(lx * 16 + lz) * CHUNK_HEIGHT + y + 2];
			if ((up1 == null || up1.isAir()) && (up2 == null || up2.isAir())) {
				return y + 1 + this.getMinY();
			}
		}
		return this.getMinY();
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState randomState) {
		int chunkX = SectionPos.blockToSectionCoord(x);
		int chunkZ = SectionPos.blockToSectionCoord(z);
		BlockState[] blocks = this.generateBlocks(chunkX, chunkZ);
		int lx = x & 15;
		int lz = z & 15;

		BlockState[] column = new BlockState[CHUNK_HEIGHT];
		for (int y = 0; y < CHUNK_HEIGHT; ++y) {
			BlockState state = blocks[(lz * 16 + lx) * CHUNK_HEIGHT + y];
			column[y] = state == null ? Blocks.AIR.defaultBlockState() : state;
		}
		return new NoiseColumn(this.getMinY(), column);
	}

	@Override
	public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos pos) {
		info.add("[LightningCraft] Underworld generator, height=" + CHUNK_HEIGHT);
	}

	@Override
	public BiomeSource getBiomeSource() {
		return super.getBiomeSource();
	}

	@SuppressWarnings("unused")
	private static Holder<net.minecraft.world.level.biome.Biome> unusedHolderRef() {
		return null;
	}

}
