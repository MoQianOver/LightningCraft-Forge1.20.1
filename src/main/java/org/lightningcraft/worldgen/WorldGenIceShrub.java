package org.lightningcraft.worldgen;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.blocks.base.BlockVariant;
import org.lightningcraft.init.LCBlocks;

public class WorldGenIceShrub extends Feature<NoneFeatureConfiguration> {

	public static final Codec<WorldGenIceShrub> CODEC = Codec.unit(WorldGenIceShrub::new);

	private final BlockState iceMetadata;
	private final BlockState packedIceMetadata;

	public WorldGenIceShrub() {
		super(NoneFeatureConfiguration.CODEC);
		this.packedIceMetadata = Blocks.PACKED_ICE.defaultBlockState();
		this.iceMetadata = Blocks.PACKED_ICE.defaultBlockState();
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel worldIn = context.level();
		RandomSource rand = context.random();
		BlockPos position = context.origin();

		BlockState state;
		do {
			state = worldIn.getBlockState(position);

			if (!state.isAir() && !state.is(net.minecraft.tags.BlockTags.LEAVES)) break;
			position = position.below();
		} while (position.getY() > worldIn.getMinBuildHeight());

		BlockState below = worldIn.getBlockState(position);
		int meta1 = BlockVariant.getMeta(below);

		if (below.getBlock() == LCBlocks.stoneBlock && meta1 == BlockStone.UNDER) {
			position = position.above();
			this.setBlock(worldIn, position, this.packedIceMetadata);

			for (int i = position.getY(); i <= position.getY() + 2; ++i) {
				int j = i - position.getY();
				int k = 2 - j;

				for (int l = position.getX() - k; l <= position.getX() + k; ++l) {
					int i1 = l - position.getX();

					for (int j1 = position.getZ() - k; j1 <= position.getZ() + k; ++j1) {
						int k1 = j1 - position.getZ();

						if (Math.abs(i1) != k || Math.abs(k1) != k || rand.nextInt(2) != 0) {
							BlockPos blockpos = new BlockPos(l, i, j1);

							BlockState target = worldIn.getBlockState(blockpos);
							boolean replaceable = target.isAir()
									|| target.canBeReplaced()
									|| target.getFluidState().is(Fluids.WATER);
							if (replaceable) {
								this.setBlock(worldIn, blockpos, this.iceMetadata);
							}
						}
					}
				}
			}
		}

		return true;
	}

}
