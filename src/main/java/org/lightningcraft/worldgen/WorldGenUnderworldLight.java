package org.lightningcraft.worldgen;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.blocks.base.BlockVariant;
import org.lightningcraft.init.LCBlocks;

public class WorldGenUnderworldLight extends Feature<NoneFeatureConfiguration> {

	public static final Codec<WorldGenUnderworldLight> CODEC = Codec.unit(WorldGenUnderworldLight::new);

	public WorldGenUnderworldLight() {

		super(NoneFeatureConfiguration.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel worldIn = context.level();
		RandomSource rand = context.random();
		BlockPos position = context.origin();

		for (int i = 0; i < 64; ++i) {
			BlockPos blockpos = position.offset(
					rand.nextInt(8) - rand.nextInt(8),
					rand.nextInt(4) - rand.nextInt(4),
					rand.nextInt(8) - rand.nextInt(8));

			Block b = worldIn.getBlockState(blockpos.below()).getBlock();

			int meta = BlockVariant.getMeta(worldIn.getBlockState(blockpos.below()));

			if (worldIn.isEmptyBlock(blockpos)
					&& (b == LCBlocks.stoneBlock && meta == BlockStone.UNDER)
					|| b == Blocks.STONE || b == Blocks.GRASS_BLOCK) {
				this.setBlock(worldIn, blockpos.below(), LCBlocks.lightBlock.defaultBlockState());
			}
		}

		return true;
	}

}
