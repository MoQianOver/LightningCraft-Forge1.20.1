package org.lightningcraft.items;

import org.lightningcraft.util.LCStack;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.util.StackHelper;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.ref.Metal.Rod;

public class ItemGoldenClub extends ItemGolfClub {

	public ItemGoldenClub() {
		super(new Item.Properties().durability(18));
	}

	@Override
	public boolean isValidRepairItem(ItemStack a, ItemStack b) {
		ItemStack repairWith = LCStack.of(LCItems.rod, 0, Rod.GOLD);
		if(StackHelper.areItemStacksEqualForCrafting(repairWith, b)){
			return true;
		} else {
			return false;
		}
	}

}
