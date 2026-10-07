package org.lightningcraft.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class LCEnchantment extends Enchantment {

	public LCEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}

	public abstract String getUnlocalizedName();

	public Enchantment setRegistryNameImplicit() {
		return this;
	}

}
