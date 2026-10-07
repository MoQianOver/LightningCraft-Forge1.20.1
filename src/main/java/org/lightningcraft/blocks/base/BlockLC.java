package org.lightningcraft.blocks.base;

import org.lightningcraft.util.LCStack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.api.registry.ILightningCraftBlock;

public class BlockLC extends Block implements ILightningCraftBlock {

	public BlockLC(Block parent, float hardness, float resistance) {
		this(parent, hardness, resistance, false);
	}

	public BlockLC(Block parent, float hardness, float resistance, boolean noOcclusion) {
		super(makeProperties(parent, hardness, resistance, noOcclusion));
	}

	private static BlockBehaviour.Properties makeProperties(Block parent, float hardness, float resistance,
			boolean noOcclusion) {
		BlockState parentState = parent.defaultBlockState();

		BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
				.mapColor(parent.defaultMapColor())
				.sound(parent.getSoundType(parentState))
				.strength(hardness, resistance);

		if (parentState.requiresCorrectToolForDrops()) {
			props.requiresCorrectToolForDrops();
		}
		if (parentState.isRandomlyTicking()) {
			props.randomTicks();
		}
		if (parentState.getLightEmission() > 0) {
			int emission = parentState.getLightEmission();
			props.lightLevel(state -> emission);
		}
		if (noOcclusion || !parentState.canOcclude()) {
			props.noOcclusion();
		}
		if (parentState.isAir()) {
			props.air();
		}

		return props;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return new ItemStack(this);
	}

}
