package org.lightningcraft.items.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;

public class ItemBlockLightningCannon extends ItemBlockMeta {

	public ItemBlockLightningCannon(Block block) {
		super(block);
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		switch(stack.getDamageValue()) {
		case 2:
			return Rarity.EPIC;
		default:
			return Rarity.RARE;
		}
	}

}
