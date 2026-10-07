package org.lightningcraft.init;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import org.lightningcraft.enchantments.EnchantmentElectrostaticAura;
import org.lightningcraft.enchantments.EnchantmentHandOfThor;
import org.lightningcraft.enchantments.LCEnchantment;

public class LCEnchantments {

	public static LCEnchantment handOfThor;
	public static LCEnchantment elecAura;

	public static void mainRegistry() {
		addEnchantments();
		registerEnchantments();
	}

	private static void addEnchantments() {
		handOfThor = new EnchantmentHandOfThor(Rarity.RARE);
		elecAura  = new EnchantmentElectrostaticAura(Rarity.RARE);
	}

	private static void registerEnchantments() {
		LCRegistry.ENCHANTMENTS.register("hand_of_thor", () -> handOfThor);
		LCRegistry.ENCHANTMENTS.register("electrostatic_aura", () -> elecAura);
	}

}