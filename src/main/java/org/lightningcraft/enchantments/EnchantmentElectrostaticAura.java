package org.lightningcraft.enchantments;

import org.lightningcraft.ref.RefStrings;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;

public class EnchantmentElectrostaticAura extends LCEnchantment {

	public EnchantmentElectrostaticAura(Rarity rarity) {
		super(rarity, EnchantmentCategory.ARMOR, new EquipmentSlot[]
				{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
	}

	@Override
	public String getUnlocalizedName() {
		return "elec_aura";
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
