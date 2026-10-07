package org.lightningcraft.enchantments;

import org.lightningcraft.ref.RefStrings;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;

public class EnchantmentHandOfThor extends LCEnchantment {

	public EnchantmentHandOfThor(Rarity rarity) {
		super(rarity, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}

	@Override
	public String getUnlocalizedName() {
		return "hand_of_thor";
	}

	@Override
	public int getMinCost(int par1) {
		switch(par1) {
		case 0:
			return 15;
		case 1:
			return 22;
		default:
			return 30;
		}
	}

	@Override
	public int getMaxCost(int par1) {
		switch(par1) {
		case 0:
			return 21;
		case 1:
			return 29;
		default:
			return 30;
		}
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

}
