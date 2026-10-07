package org.lightningcraft.worldgen.feature;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.init.LCBlocks;

public class FeatureUnderworldLight extends Feature<NoneFeatureConfiguration> {

	public FeatureUnderworldLight(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		BlockPos origin = context.origin();
		BlockState under = ((org.lightningcraft.blocks.base.BlockVariant) LCBlocks.stoneBlock)
				.getStateFromMeta(BlockStone.UNDER);
		boolean placed = false;

		for (int i = 0; i < 64; ++i) {
			BlockPos pos = origin.offset(rand.nextInt(8) - rand.nextInt(8),
					rand.nextInt(4) - rand.nextInt(4),
					rand.nextInt(8) - rand.nextInt(8));
			if (level.isEmptyBlock(pos) && level.getBlockState(pos.below()) == under) {
				level.setBlock(pos.below(), LCBlocks.lightBlock.defaultBlockState(), 2);
				placed = true;
			}
		}
		return placed;
	}
}