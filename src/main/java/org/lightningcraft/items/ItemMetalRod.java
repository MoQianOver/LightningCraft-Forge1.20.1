package org.lightningcraft.items;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.items.base.ItemMeta;
import org.lightningcraft.ref.Metal.Rod;

public class ItemMetalRod extends ItemMeta {

	public ItemMetalRod() {
		super(Rod.count);
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		switch(stack.getDamageValue()) {
		case Rod.ELEC:
			return Rarity.UNCOMMON;
		case Rod.SKY:
			return Rarity.RARE;
		case Rod.MYSTIC:
			return Rarity.EPIC;
		default:
			return super.getRarity(stack);
		}
	}

}
