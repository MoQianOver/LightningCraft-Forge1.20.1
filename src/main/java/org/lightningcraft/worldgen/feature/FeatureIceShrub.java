package org.lightningcraft.worldgen.feature;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.init.LCBlocks;

public class FeatureIceShrub extends Feature<NoneFeatureConfiguration> {

	public FeatureIceShrub(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		BlockPos position = context.origin();

		int guard = 0;
		while (position.getY() > level.getMinBuildHeight() && guard++ < 256) {
			BlockState state = level.getBlockState(position);
			if (!state.isAir() && !state.is(Blocks.OAK_LEAVES)) {
				break;
			}
			position = position.below();
		}

		BlockState under = ((org.lightningcraft.blocks.base.BlockVariant) LCBlocks.stoneBlock)
				.getStateFromMeta(BlockStone.UNDER);
		if (level.getBlockState(position) != under) {
			return false;
		}

		BlockState ice = Blocks.PACKED_ICE.defaultBlockState();
		BlockPos base = position.above();
		level.setBlock(base, ice, 2);

		for (int y = base.getY(); y <= base.getY() + 2; ++y) {
			int j = y - base.getY();
			int k = 2 - j;
			for (int x = base.getX() - k; x <= base.getX() + k; ++x) {
				int dx = x - base.getX();
				for (int z = base.getZ() - k; z <= base.getZ() + k; ++z) {
					int dz = z - base.getZ();
					if (Math.abs(dx) != k || Math.abs(dz) != k || rand.nextInt(2) != 0) {
						BlockPos pos = new BlockPos(x, y, z);
						if (level.getBlockState(pos).canBeReplaced()) {
							level.setBlock(pos, ice, 2);
						}
					}
				}
			}
		}
		return true;
	}
}