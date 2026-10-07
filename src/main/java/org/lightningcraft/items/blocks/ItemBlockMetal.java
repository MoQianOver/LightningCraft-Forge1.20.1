package org.lightningcraft.items.blocks;

import org.lightningcraft.ref.Metal.Ingot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;

public class ItemBlockMetal extends ItemBlockMeta {

	public ItemBlockMetal(Block block) {
		super(block);
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		switch(stack.getDamageValue()) {
		case Ingot.ELEC:
			return Rarity.UNCOMMON;
		case Ingot.SKY:
			return Rarity.RARE;
		case Ingot.MYSTIC:
			return Rarity.EPIC;
		default:
			return super.getRarity(stack);
		}
	}

}
