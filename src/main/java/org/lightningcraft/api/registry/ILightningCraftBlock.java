package org.lightningcraft.api.registry;

import net.minecraft.world.item.BlockItem;

public interface ILightningCraftBlock extends ILightningCraftObject {

	public default Class<? extends BlockItem> getItemClass() {
		return BlockItem.class;
	}

	public default Object[] getItemClassArgs() {
		return new Object[0];
	}

}
