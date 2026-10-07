package org.lightningcraft.ref;

import java.util.function.Supplier;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class LCArmorMaterial implements ArmorMaterial {

	public static final int SLOT_BOOTS = 0;
	public static final int SLOT_LEGS = 1;
	public static final int SLOT_CHEST = 2;
	public static final int SLOT_HELM = 3;

	private final String name;
	private final int durability;
	private final int[] reductionAmounts;
	private final int enchantmentValue;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;

	private Supplier<Ingredient> repairIngredient = () -> Ingredient.EMPTY;

	public LCArmorMaterial(String name, int durability, int[] reductionAmounts, int enchantmentValue,
			SoundEvent equipSound, float toughness) {
		this(name, durability, reductionAmounts, enchantmentValue, equipSound, toughness, 0.0F);
	}

	public LCArmorMaterial(String name, int durability, int[] reductionAmounts, int enchantmentValue,
			SoundEvent equipSound, float toughness, float knockbackResistance) {
		this.name = name;
		this.durability = durability;
		this.reductionAmounts = reductionAmounts;
		this.enchantmentValue = enchantmentValue;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
	}

	public LCArmorMaterial setRepair(ItemStack repair) {
		this.repairIngredient = () -> repair.isEmpty() ? Ingredient.EMPTY : Ingredient.of(repair);
		return this;
	}

	@Override
	public int getDurabilityForType(ArmorItem.Type type) {
		return durability;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type type) {
		switch (type) {
			case HELMET:     return reductionAmounts[SLOT_HELM];
			case CHESTPLATE: return reductionAmounts[SLOT_CHEST];
			case LEGGINGS:   return reductionAmounts[SLOT_LEGS];
			case BOOTS:      return reductionAmounts[SLOT_BOOTS];
			default:         return 0;
		}
	}

	@Override
	public int getEnchantmentValue() {
		return enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound() {
		return equipSound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredient.get();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return knockbackResistance;
	}

	public static SoundEvent defaultEquipSound() {
		return SoundEvents.ARMOR_EQUIP_GOLD;
	}
}
