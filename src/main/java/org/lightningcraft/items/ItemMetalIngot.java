package org.lightningcraft.items;

import org.lightningcraft.items.base.ItemMeta;
import org.lightningcraft.ref.Metal.Ingot;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;

public class ItemMetalIngot extends ItemMeta {

	public ItemMetalIngot() {
		super(Ingot.count);
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
