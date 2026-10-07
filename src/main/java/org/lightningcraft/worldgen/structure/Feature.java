package org.lightningcraft.worldgen.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.lightningcraft.blocks.base.BlockVariant;
import org.lightningcraft.util.WeightedRandomChestContent;

public abstract class Feature extends StructurePiece {

	protected LootChestGroup lootChests;

	protected LootChestGroup loot() {
		if (this.lootChests == null) {
			this.lootChests = this.createLootChests();
		}
		return this.lootChests;
	}

	protected LootChestGroup createLootChests() {
		return null;
	}

	protected int chestCenterLocalX = Integer.MIN_VALUE;
	protected int chestCenterLocalZ = Integer.MIN_VALUE;

	private net.minecraft.core.Direction chestFacing(int x, int z, int meta) {
		if (this.chestCenterLocalX == Integer.MIN_VALUE) {
			return HORIZONTALS_112[Math.abs(meta) % 4];
		}
		int wx = this.getWorldX(x, z);
		int wz = this.getWorldZ(x, z);
		int cx = this.getWorldX(this.chestCenterLocalX, this.chestCenterLocalZ);
		int cz = this.getWorldZ(this.chestCenterLocalX, this.chestCenterLocalZ);
		int dx = cx - wx;
		int dz = cz - wz;
		if (Math.abs(dx) >= Math.abs(dz) && dx != 0) {
			return dx > 0 ? net.minecraft.core.Direction.EAST : net.minecraft.core.Direction.WEST;
		}
		if (dz != 0) {
			return dz > 0 ? net.minecraft.core.Direction.SOUTH : net.minecraft.core.Direction.NORTH;
		}
		return HORIZONTALS_112[Math.abs(meta) % 4];
	}

	private static final net.minecraft.core.Direction[] HORIZONTALS_112 = {
			net.minecraft.core.Direction.NORTH, net.minecraft.core.Direction.EAST,
			net.minecraft.core.Direction.SOUTH, net.minecraft.core.Direction.WEST };

	protected int scatteredFeatureSizeX;

	protected int scatteredFeatureSizeY;

	protected int scatteredFeatureSizeZ;
	protected int structY = -1;

	protected int spawnMinY;
	protected int spawnMaxY;

	protected Feature(StructurePieceType type, int genDepth, BoundingBox box) {
		super(type, genDepth, box);
	}

	protected Feature(StructurePieceType type, CompoundTag tag) {
		super(type, tag);
	}

	protected Feature(StructurePieceType type, Direction dir, int x, int y, int z, int sx, int sy, int sz) {
		super(type, 0, makeBox(dir, x, y, z, sx, sy, sz));
		this.setOrientation(dir);
		this.scatteredFeatureSizeX = sx;
		this.scatteredFeatureSizeY = sy;
		this.scatteredFeatureSizeZ = sz;
	}

	public static Direction randomDir(RandomSource rand) {
		return Direction.Plane.HORIZONTAL.getRandomDirection(rand);
	}

	private static BoundingBox makeBox(Direction dir, int x, int y, int z, int sx, int sy, int sz) {
		return switch (dir) {
			case NORTH, SOUTH -> new BoundingBox(x, y, z, x + sx - 1, y + sy - 1, z + sz - 1);
			default -> new BoundingBox(x, y, z, x + sz - 1, y + sy - 1, z + sx - 1);
		};
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("Width", this.scatteredFeatureSizeX);
		tag.putInt("Height", this.scatteredFeatureSizeY);
		tag.putInt("Depth", this.scatteredFeatureSizeZ);
		tag.putInt("HPos", this.structY);
		if (this.lootChests != null) {
			this.lootChests.writeToNBT(tag);
		}
	}

	protected void readFeatureData(CompoundTag tag) {
		this.scatteredFeatureSizeX = tag.getInt("Width");
		this.scatteredFeatureSizeY = tag.getInt("Height");
		this.scatteredFeatureSizeZ = tag.getInt("Depth");
		this.structY = tag.getInt("HPos");
		if (this.lootChests != null) {
			this.lootChests.readFromNBT(tag);
		}
	}

	protected boolean generateStructureChestContents(WorldGenLevel level, BoundingBox box, RandomSource rand,
			int x, int y, int z, int meta, int chestIndex) {

		if (this.lootChests == null) {
			return false;
		}
		return generateStructureChestContents(level, box, rand, x, y, z, meta,
				loot().getChestContents(chestIndex), loot().getStackCount(rand, chestIndex),
				loot().getIsTrapped(chestIndex));
	}

	protected boolean generateStructureChestContents(WorldGenLevel level, BoundingBox box, RandomSource rand,
			int x, int y, int z, int meta) {
		if (this.lootChests == null) {
			return false;
		}
		return generateStructureChestContents(level, box, rand, x, y, z, meta,
				loot().getChestContents(), loot().getStackCount(rand), false);
	}

	protected boolean generateStructureChestContents(WorldGenLevel level, BoundingBox box, RandomSource rand,
			int x, int y, int z, int meta, List<WeightedRandomChestContent> chest, int count, boolean isTrapped) {

		if (chest == null) {
			return false;
		}
		int i1 = this.getWorldX(x, z);
		int j1 = this.getWorldY(y);
		int k1 = this.getWorldZ(x, z);
		BlockPos pos = new BlockPos(i1, j1, k1);
		Block chestBlock = isTrapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST;

		if (!this.box().isInside(pos)) {
			return false;
		}

		if (level.getBlockState(pos).getBlock() != chestBlock) {

			BlockState chestState = chestBlock.defaultBlockState();
			if (chestBlock instanceof net.minecraft.world.level.block.ChestBlock) {
				chestState = chestState.setValue(net.minecraft.world.level.block.ChestBlock.FACING,
						this.chestFacing(x, z, meta));
			}
			level.setBlock(pos, chestState, 2);
			BlockEntity tileentity = level.getBlockEntity(pos);

			if (tileentity instanceof ChestBlockEntity) {
				WeightedRandomChestContent.generateChestContents(rand, chest, (ChestBlockEntity) tileentity, count);
			}

			return true;
		} else {
			return false;
		}
	}

	protected boolean generateStructureChestContents(WorldGenLevel level, BoundingBox box, RandomSource rand,
			int x, int y, int z, int meta, List<WeightedRandomChestContent> chest, int count) {
		return generateStructureChestContents(level, box, rand, x, y, z, meta, chest, count, false);
	}

	private static final ThreadLocal<BoundingBox> CURRENT_CHUNK_BOX = new ThreadLocal<>();

	private BoundingBox box() {
		BoundingBox b = CURRENT_CHUNK_BOX.get();
		return b != null ? b : this.boundingBox;
	}

	protected void addBlock(WorldGenLevel level, int x, int y, int z, int blockId, int meta) {

		if (blockId == 0) {
			int i1 = this.getWorldX(x, z);
			int j1 = this.getWorldY(y);
			int k1 = this.getWorldZ(x, z);
			BlockPos pos = new BlockPos(i1, j1, k1);
			if (this.box().isInside(pos)) {
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				this.dbgPlaced++;
			} else {
				this.dbgRejected++;
			}
			return;
		}
		throw new IllegalArgumentException(
				"1.20.1 has no numeric block ids; addBlock(..., int, int) cannot resolve id " + blockId);
	}

	protected void addBlock(WorldGenLevel level, int x, int y, int z, Block block, int meta) {
		boolean ignoreBox = false;
		if (meta < 0) {
			meta = 0;
			ignoreBox = true;
		}
		this.placeBlockAtCurrentPosition(level, block, meta, x, y, z, this.box(), ignoreBox);
	}

	protected void addBlock(WorldGenLevel level, int x, int y, int z, BlockState state) {
		int i1 = this.getWorldX(x, z);
		int j1 = this.getWorldY(y);
		int k1 = this.getWorldZ(x, z);
		BlockPos pos = new BlockPos(i1, j1, k1);
		if (this.box().isInside(pos)) {
			level.setBlock(pos, state, 2);
			this.dbgPlaced++;
		} else {
			this.dbgRejected++;
		}
	}

	protected void placeBlockAtCurrentPosition(WorldGenLevel level, Block b, int meta, int x, int y, int z,
			BoundingBox box, boolean ignoreBox) {
		int i1 = this.getWorldX(x, z);
		int j1 = this.getWorldY(y);
		int k1 = this.getWorldZ(x, z);
		BlockPos pos = new BlockPos(i1, j1, k1);

		if (ignoreBox || box.isInside(pos)) {

			BlockState state = (b instanceof BlockVariant variant) ? variant.getStateFromMeta(meta) : b.defaultBlockState();
			level.setBlock(pos, state, 2);
			this.dbgPlaced++;
		} else {
			this.dbgRejected++;
		}
	}

	protected void addSpawner(WorldGenLevel level, int x, int y, int z, EntityType<?> entityType) {

		addBlock(level, x, y, z, Blocks.SPAWNER, -1);

		int i1 = this.getWorldX(x, z);
		int j1 = this.getWorldY(y);
		int k1 = this.getWorldZ(x, z);
		BlockPos pos = new BlockPos(i1, j1, k1);

		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof SpawnerBlockEntity spawner) {

			spawner.setEntityId(entityType, RandomSource.create());
		}
	}

	protected abstract boolean findSpawnPosition(WorldGenLevel level, BoundingBox box, int yoff);

	@Override
	public void postProcess(WorldGenLevel level, net.minecraft.world.level.StructureManager structureManager,
			net.minecraft.world.level.chunk.ChunkGenerator generator, RandomSource rand, BoundingBox box,
			net.minecraft.world.level.ChunkPos chunkPos, BlockPos pos) {

		this.dbgPlaced = 0;
		this.dbgRejected = 0;

		if (level instanceof net.minecraft.server.level.WorldGenRegion region) {
			net.minecraft.world.level.ChunkPos center = region.getCenter();
			int cx0 = Math.max(box.minX(), center.getMinBlockX());
			int cy0 = Math.max(box.minY(), level.getMinBuildHeight());
			int cz0 = Math.max(box.minZ(), center.getMinBlockZ());
			int cx1 = Math.min(box.maxX(), center.getMaxBlockX());
			int cy1 = Math.min(box.maxY(), level.getMaxBuildHeight() - 1);
			int cz1 = Math.min(box.maxZ(), center.getMaxBlockZ());
			if (cx0 > cx1 || cy0 > cy1 || cz0 > cz1) {

				return;
			}

			box = new BoundingBox(cx0, cy0, cz0, cx1, cy1, cz1);
		}

		BoundingBox prevBox = CURRENT_CHUNK_BOX.get();
		CURRENT_CHUNK_BOX.set(box);
		try {
			this.addComponentParts(level, rand, box);
		} finally {
			if (prevBox != null) {
				CURRENT_CHUNK_BOX.set(prevBox);
			} else {
				CURRENT_CHUNK_BOX.remove();
			}
		}
		org.lightningcraft.ref.Log.logger.info("[LC-STRUCT] postProcess "
				+ this.getClass().getSimpleName() + " chunk=" + chunkPos
				+ " box=" + box + " structY=" + this.structY
				+ " 写入=" + this.dbgPlaced + " 越界丢弃=" + this.dbgRejected);
	}

	protected int dbgPlaced;
	protected int dbgRejected;

	protected abstract boolean addComponentParts(WorldGenLevel level, RandomSource rand, BoundingBox box);

	protected Direction getCoordBaseMode() {
		return this.getOrientation();
	}

	protected void setCoordBaseMode(Direction dir) {
		this.setOrientation(dir);
	}

	protected void addSpawner(WorldGenLevel level, int x, int y, int z, Class<?> entityClass) {
		for (net.minecraft.world.entity.EntityType<?> type : net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE) {
			if (type.getBaseClass() == entityClass) {
				this.addSpawner(level, x, y, z, type);
				return;
			}
		}
	}

}
