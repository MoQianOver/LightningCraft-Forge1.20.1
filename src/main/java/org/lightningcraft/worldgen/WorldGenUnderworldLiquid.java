package org.lightningcraft.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.util.WorldUtils;

public class WorldGenUnderworldLiquid extends Feature<WorldGenUnderworldLiquid.Config> {

	public static final Codec<WorldGenUnderworldLiquid> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("liquid").forGetter(f -> f.liquid),
			Codec.BOOL.fieldOf("cancel").forGetter(f -> f.cancel)
	).apply(instance, WorldGenUnderworldLiquid::new));

	private final Block liquid;
	private final boolean cancel;

	public WorldGenUnderworldLiquid(Block liquid, boolean cancel) {
		super(Config.CODEC);
		this.liquid = liquid;
		this.cancel = cancel;
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> context) {
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		BlockPos pos = context.origin();

		BlockState at = level.getBlockState(pos);

		if (!WorldUtils.blockMatches(level, pos.above(), LCBlocks.stoneBlock, BlockStone.UNDER)) {
			return false;
		} else if (!at.isAir()
				&& !WorldUtils.blockMatches(level, pos.above(), LCBlocks.stoneBlock, BlockStone.UNDER)) {
			return false;
		} else {
			int i = 0;

			if (WorldUtils.blockMatches(level, pos.west(), LCBlocks.stoneBlock, BlockStone.UNDER)) ++i;
			if (WorldUtils.blockMatches(level, pos.east(), LCBlocks.stoneBlock, BlockStone.UNDER)) ++i;
			if (WorldUtils.blockMatches(level, pos.north(), LCBlocks.stoneBlock, BlockStone.UNDER)) ++i;
			if (WorldUtils.blockMatches(level, pos.south(), LCBlocks.stoneBlock, BlockStone.UNDER)) ++i;
			if (WorldUtils.blockMatches(level, pos.below(), LCBlocks.stoneBlock, BlockStone.UNDER)) ++i;

			int j = 0;

			if (level.isEmptyBlock(pos.west())) ++j;
			if (level.isEmptyBlock(pos.east())) ++j;
			if (level.isEmptyBlock(pos.north())) ++j;
			if (level.isEmptyBlock(pos.south())) ++j;
			if (level.isEmptyBlock(pos.below())) ++j;

			if ((!this.cancel && i == 4 && j == 1) || i == 5) {
				BlockState iblockstate = this.liquid.defaultBlockState();
				this.setBlock(level, pos, iblockstate);

			}

			return true;
		}
	}

	public static class Config implements net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration {
		public static final Codec<Config> CODEC = Codec.unit(Config::new);
		public static final Config INSTANCE = new Config();
	}

}
