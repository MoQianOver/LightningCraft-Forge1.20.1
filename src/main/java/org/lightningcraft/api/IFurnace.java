package org.lightningcraft.api;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public interface IFurnace {

	boolean isBurning(BlockState state, BlockGetter level, BlockPos pos);

	void setBurning(BlockState state, BlockGetter level, BlockPos pos, boolean burning);

}
