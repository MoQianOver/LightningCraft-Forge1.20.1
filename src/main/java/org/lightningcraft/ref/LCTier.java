package org.lightningcraft.ref;

import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class LCTier implements Tier {

	private final String name;
	private final int level;
	private final int uses;
	private final float speed;
	private final float attackDamageBonus;
	private final int enchantmentValue;

	private Supplier<Ingredient> repairIngredient = () -> Ingredient.EMPTY;

	public LCTier(String name, int level, int uses, float speed, float attackDamageBonus, int enchantmentValue) {
		this.name = name;
		this.level = level;
		this.uses = uses;
		this.speed = speed;
		this.attackDamageBonus = attackDamageBonus;
		this.enchantmentValue = enchantmentValue;
	}

	public LCTier setRepair(ItemStack repair) {
		this.repairIngredient = () -> repair.isEmpty() ? Ingredient.EMPTY : Ingredient.of(repair);
		return this;
	}

	public String getName() {
		return name;
	}

	@Override
	public int getUses() {
		return uses;
	}

	@Override
	public float getSpeed() {
		return speed;
	}

	@Override
	public float getAttackDamageBonus() {
		return attackDamageBonus;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getEnchantmentValue() {
		return enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredient.get();
	}

	@Override
	public String toString() {
		return "LCTier[" + name + "]";
	}
}
