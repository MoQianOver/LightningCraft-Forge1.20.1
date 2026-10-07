package org.lightningcraft.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.ref.LCArmorMaterial;

public class ArmorHelper {

	public static final ArmorItem.Type TYPE_BOOTS = ArmorItem.Type.BOOTS;
	public static final ArmorItem.Type TYPE_LEGS = ArmorItem.Type.LEGGINGS;
	public static final ArmorItem.Type TYPE_CHEST = ArmorItem.Type.CHESTPLATE;
	public static final ArmorItem.Type TYPE_HELM = ArmorItem.Type.HELMET;

	public static final int EQUIPPED_BOOTS = 1;
	public static final int EQUIPPED_LEGS = 2;
	public static final int EQUIPPED_CHEST = 3;
	public static final int EQUIPPED_HELM = 4;

	public static final int WORN_BOOTS = 0;
	public static final int WORN_LEGS = 1;
	public static final int WORN_CHEST = 2;
	public static final int WORN_HELM = 3;

	private static final Map<ArmorMaterial, ItemStack> matMap = new HashMap<>();

	public static ArmorItem.Type toArmorType(EquipmentSlot slot) {
		switch (slot) {
			case HEAD:  return ArmorItem.Type.HELMET;
			case CHEST: return ArmorItem.Type.CHESTPLATE;
			case LEGS:  return ArmorItem.Type.LEGGINGS;
			case FEET:  return ArmorItem.Type.BOOTS;
			default:    throw new IllegalArgumentException("Not an armor slot: " + slot);
		}
	}

	public static void setRepairStack(ArmorMaterial mat, @Nonnull ItemStack repair) {
		matMap.put(mat, repair);
		if (mat instanceof LCArmorMaterial lcMat) {
			lcMat.setRepair(repair);
		}
	}

	public static @Nonnull ItemStack getRepairStack(ArmorMaterial mat) {
		ItemStack cached = matMap.get(mat);
		if (cached != null) return cached;
		try {
			ItemStack[] items = mat.getRepairIngredient().getItems();
			return items.length > 0 ? items[0] : ItemStack.EMPTY;
		} catch (Exception e) {
			return ItemStack.EMPTY;
		}
	}

	public static boolean entityHasFullArmor(LivingEntity user, Class<? extends ArmorItem> type) {
		boolean flag = false;
		if (user != null) {
			flag = !user.getItemBySlot(EquipmentSlot.HEAD).isEmpty() &&
					user.getItemBySlot(EquipmentSlot.HEAD).getItem().getClass() == type;
			flag &= !user.getItemBySlot(EquipmentSlot.CHEST).isEmpty() &&
					user.getItemBySlot(EquipmentSlot.CHEST).getItem().getClass() == type;
			flag &= !user.getItemBySlot(EquipmentSlot.LEGS).isEmpty() &&
					user.getItemBySlot(EquipmentSlot.LEGS).getItem().getClass() == type;
			flag &= !user.getItemBySlot(EquipmentSlot.FEET).isEmpty() &&
					user.getItemBySlot(EquipmentSlot.FEET).getItem().getClass() == type;
		}
		return flag;
	}

	public static boolean playerHasFullArmor(Player user, Class<? extends ArmorItem> type) {
		return entityHasFullArmor(user, type);
	}

	public static ArmorMaterial addArmorMaterial(String enumName, String textureName, int durability,
			int[] reductionAmounts, int enchantability, SoundEvent soundOnEquip, float toughness) {
		return new LCArmorMaterial(textureName, durability, reductionAmounts,
				enchantability, soundOnEquip, toughness);
	}

}
