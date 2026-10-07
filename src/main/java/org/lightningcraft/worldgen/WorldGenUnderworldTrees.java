package org.lightningcraft.worldgen;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.lightningcraft.init.LCBlocks;

public class WorldGenUnderworldTrees extends Feature<NoneFeatureConfiguration> {

	public static final Codec<WorldGenUnderworldTrees> CODEC = Codec.unit(WorldGenUnderworldTrees::new);

	private final int minTreeHeight;

	private final boolean vinesGrow;

	private final BlockState metaWood;

	private final BlockState metaLeaves;

	public WorldGenUnderworldTrees() {
		this(6);
	}

	public WorldGenUnderworldTrees(int minTreeHeight) {
		super(NoneFeatureConfiguration.CODEC);
		this.minTreeHeight = minTreeHeight;
		this.metaWood = LCBlocks.woodLog.defaultBlockState();
		this.metaLeaves = Blocks.OAK_LEAVES.defaultBlockState();
		this.vinesGrow = true;
	}

	private boolean isReplaceable(LevelAccessor level, BlockPos pos) {
		BlockState s = level.getBlockState(pos);
		return s.isAir() || s.canBeReplaced() || s.is(BlockTags.LEAVES);
	}

	private static boolean isAirLeavesOrVine(BlockState state) {
		return state.isAir() || state.is(BlockTags.LEAVES) || state.is(Blocks.VINE);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel worldIn = context.level();
		RandomSource rand = context.random();
		BlockPos position = context.origin();

		int i = rand.nextInt(3) + this.minTreeHeight;
		boolean flag = true;

		int maxY = worldIn.getMinBuildHeight() + worldIn.getHeight();

		if (position.getY() >= 1 && position.getY() + i + 1 <= maxY) {
			for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
				int k = 1;

				if (j == position.getY()) {
					k = 0;
				}

				if (j >= position.getY() + 1 + i - 2) {
					k = 2;
				}

				BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

				for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
					for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
						if (j >= worldIn.getMinBuildHeight() && j < maxY) {
							if (!this.isReplaceable(worldIn, mpos.set(l, j, i1))) {
								flag = false;
							}
						} else {
							flag = false;
						}
					}
				}
			}

			if (!flag) {
				return false;
			}

			BlockPos down = position.below();
			BlockState state1 = worldIn.getBlockState(down);

			boolean isSoil = state1.canSustainPlant(worldIn, down, Direction.UP,
					(net.minecraftforge.common.IPlantable) Blocks.OAK_SAPLING);

			if (!(isSoil && position.getY() < maxY - i - 1)) {
				return false;
			}

			int k2 = 3;
			int l2 = 0;

			for (int i3 = position.getY() - k2 + i; i3 <= position.getY() + i; ++i3) {
				int i4 = i3 - (position.getY() + i);
				int j1 = l2 + 1 - i4 / 2;

				for (int k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1) {
					int l1 = k1 - position.getX();

					for (int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; ++i2) {
						int j2 = i2 - position.getZ();

						if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i4 != 0) {
							BlockPos blockpos = new BlockPos(k1, i3, i2);
							BlockState state = worldIn.getBlockState(blockpos);

							if (isAirLeavesOrVine(state)) {
								this.setBlock(worldIn, blockpos, this.metaLeaves);
							}
						}
					}
				}
			}

			for (int j3 = 0; j3 < i; ++j3) {
				BlockPos upN = position.above(j3);
				BlockState state2 = worldIn.getBlockState(upN);

				if (isAirLeavesOrVine(state2)) {
					this.setBlock(worldIn, upN, this.metaWood);

					if (this.vinesGrow && j3 > 0) {
						if (rand.nextInt(3) > 0 && worldIn.isEmptyBlock(position.offset(-1, j3, 0))) {
							this.placeVine(worldIn, position.offset(-1, j3, 0), VineBlock.EAST);
						}
						if (rand.nextInt(3) > 0 && worldIn.isEmptyBlock(position.offset(1, j3, 0))) {
							this.placeVine(worldIn, position.offset(1, j3, 0), VineBlock.WEST);
						}
						if (rand.nextInt(3) > 0 && worldIn.isEmptyBlock(position.offset(0, j3, -1))) {
							this.placeVine(worldIn, position.offset(0, j3, -1), VineBlock.SOUTH);
						}
						if (rand.nextInt(3) > 0 && worldIn.isEmptyBlock(position.offset(0, j3, 1))) {
							this.placeVine(worldIn, position.offset(0, j3, 1), VineBlock.NORTH);
						}
					}
				}
			}

			if (this.vinesGrow) {
				for (int k3 = position.getY() - 3 + i; k3 <= position.getY() + i; ++k3) {
					int j4 = k3 - (position.getY() + i);
					int k4 = 2 - j4 / 2;
					BlockPos.MutableBlockPos mbpos = new BlockPos.MutableBlockPos();

					for (int l4 = position.getX() - k4; l4 <= position.getX() + k4; ++l4) {
						for (int i5 = position.getZ() - k4; i5 <= position.getZ() + k4; ++i5) {
							mbpos.set(l4, k3, i5);

							if (worldIn.getBlockState(mbpos).is(BlockTags.LEAVES)) {
								BlockPos blockpos2 = mbpos.west();
								BlockPos blockpos3 = mbpos.east();
								BlockPos blockpos4 = mbpos.north();
								BlockPos blockpos1 = mbpos.south();

								if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).isAir()) {
									this.placeVineDown(worldIn, blockpos2, VineBlock.EAST);
								}
								if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).isAir()) {
									this.placeVineDown(worldIn, blockpos3, VineBlock.WEST);
								}
								if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).isAir()) {
									this.placeVineDown(worldIn, blockpos4, VineBlock.SOUTH);
								}
								if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos1).isAir()) {
									this.placeVineDown(worldIn, blockpos1, VineBlock.NORTH);
								}
							}
						}
					}
				}
			}

			return true;
		}

		return false;
	}

	private void placeVine(LevelAccessor level, BlockPos pos, BooleanProperty face) {
		this.setBlock(level, pos, Blocks.VINE.defaultBlockState().setValue(face, Boolean.TRUE));
	}

	private void placeVineDown(LevelAccessor level, BlockPos pos, BooleanProperty face) {
		this.placeVine(level, pos, face);
		int i = 4;

		for (BlockPos p = pos.below(); level.getBlockState(p).isAir() && i > 0; --i) {
			this.placeVine(level, p, face);
			p = p.below();
		}
	}

}
