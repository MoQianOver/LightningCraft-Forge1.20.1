package org.lightningcraft.worldgen.feature;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.init.LCBlocks;

public class FeatureUnderworldTree extends Feature<NoneFeatureConfiguration> {

	private static final int MIN_TREE_HEIGHT = 6;
	private static final int MAX_VINE_LENGTH = 4;

	public FeatureUnderworldTree(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		BlockPos position = context.origin();

		int height = rand.nextInt(3) + MIN_TREE_HEIGHT;
		int minY = level.getMinBuildHeight();
		int maxY = level.getMaxBuildHeight();

		if (position.getY() < minY + 1 || position.getY() + height + 1 > maxY) {
			return false;
		}

		for (int y = position.getY(); y <= position.getY() + 1 + height; ++y) {
			int r = 1;
			if (y == position.getY()) {
				r = 0;
			}
			if (y >= position.getY() + 1 + height - 2) {
				r = 2;
			}
			for (int x = position.getX() - r; x <= position.getX() + r; ++x) {
				for (int z = position.getZ() - r; z <= position.getZ() + r; ++z) {
					if (y < minY || y >= maxY) {
						return false;
					}
					if (!level.getBlockState(new BlockPos(x, y, z)).canBeReplaced()) {
						return false;
					}
				}
			}
		}

		BlockState below = level.getBlockState(position.below());
		if (!isSoil(below) || position.getY() >= maxY - height - 1) {
			return false;
		}

		BlockState wood = LCBlocks.woodLog.defaultBlockState();
		BlockState leaves = Blocks.OAK_LEAVES.defaultBlockState();

		int crownBase = 3;
		for (int y = position.getY() - crownBase + height; y <= position.getY() + height; ++y) {
			int i4 = y - (position.getY() + height);
			int r = 1 - i4 / 2;
			for (int x = position.getX() - r; x <= position.getX() + r; ++x) {
				int dx = x - position.getX();
				for (int z = position.getZ() - r; z <= position.getZ() + r; ++z) {
					int dz = z - position.getZ();
					if ((Math.abs(dx) != r || Math.abs(dz) != r || (rand.nextInt(2) != 0 && i4 != 0))
							&& level.getBlockState(new BlockPos(x, y, z)).canBeReplaced()) {
						level.setBlock(new BlockPos(x, y, z), leaves, 2);
					}
				}
			}
		}

		for (int j3 = 0; j3 < height; ++j3) {
			BlockPos trunk = position.above(j3);
			BlockState at = level.getBlockState(trunk);
			if (at.isAir() || at.is(Blocks.OAK_LEAVES) || at.canBeReplaced()) {
				level.setBlock(trunk, wood, 2);
				if (j3 > 0) {
					tryVine(level, rand, trunk.west(), Direction.EAST);
					tryVine(level, rand, trunk.east(), Direction.WEST);
					tryVine(level, rand, trunk.north(), Direction.SOUTH);
					tryVine(level, rand, trunk.south(), Direction.NORTH);
				}
			}
		}

		for (int y = position.getY() - 3 + height; y <= position.getY() + height; ++y) {
			int j4 = y - (position.getY() + height);
			int k4 = 2 - j4 / 2;
			for (int x = position.getX() - k4; x <= position.getX() + k4; ++x) {
				for (int z = position.getZ() - k4; z <= position.getZ() + k4; ++z) {
					BlockPos leaf = new BlockPos(x, y, z);
					if (!level.getBlockState(leaf).is(Blocks.OAK_LEAVES)) {
						continue;
					}
					if (rand.nextInt(4) == 0) {
						growVine(level, leaf.west(), Direction.EAST);
					}
					if (rand.nextInt(4) == 0) {
						growVine(level, leaf.east(), Direction.WEST);
					}
					if (rand.nextInt(4) == 0) {
						growVine(level, leaf.north(), Direction.SOUTH);
					}
					if (rand.nextInt(4) == 0) {
						growVine(level, leaf.south(), Direction.NORTH);
					}
				}
			}
		}

		return true;
	}

	private static boolean isSoil(BlockState state) {
		if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)) {
			return true;
		}
		BlockState under = ((org.lightningcraft.blocks.base.BlockVariant) LCBlocks.stoneBlock)
				.getStateFromMeta(BlockStone.UNDER);
		return state == under;
	}

	private void tryVine(WorldGenLevel level, RandomSource rand, BlockPos pos, Direction facing) {
		if (rand.nextInt(3) > 0) {
			growVine(level, pos, facing);
		}
	}

	private void growVine(WorldGenLevel level, BlockPos pos, Direction facing) {
		int i = MAX_VINE_LENGTH;
		BlockPos cur = pos;
		while (i > 0 && level.isEmptyBlock(cur)) {
			level.setBlock(cur, Blocks.VINE.defaultBlockState()
					.setValue(VineBlock.getPropertyForFace(facing), Boolean.TRUE), 2);
			cur = cur.below();
			--i;
		}
	}
}