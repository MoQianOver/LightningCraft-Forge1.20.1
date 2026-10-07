package org.lightningcraft.blocks.base;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.lightningcraft.integration.energy.EnergyApiHelper;
import org.lightningcraft.items.blocks.ItemBlockCoFH;

public abstract class BlockEnergy extends BlockContainerLC {

	public BlockEnergy(Block parent, float hardness, float resistance) {
		super(parent, hardness, resistance);
	}

	@Override
	public boolean showInCreativeTab() {
		return EnergyApiHelper.rfOrTeslaLoaded;
	}

	public abstract int getMaxRFPerTick();

	public abstract int getRFLERatio();

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockCoFH.class;
	}

}
