package org.lightningcraft.fluids;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;

public class BlockFluidLC extends LiquidBlock {

	public BlockFluidLC(FlowingFluid fluid) {
		super(fluid, BlockBehaviour.Properties.of()
				.mapColor(MapColor.FIRE)
				.replaceable()
				.noCollission()
				.randomTicks()
				.strength(100.0F)
				.lightLevel(state -> 15)
				.noLootTable()
				.liquid());
	}
}
