package org.lightningcraft.init;

import java.awt.Color;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.lightningcraft.potions.PotionCustom;

public class LCPotions {

	public static void mainRegistry() {
		registerPotions();
	}

	public static MobEffect demonFriend;
	public static Potion demonFriendNormal, demonFriendExtended;
	public static final int demonFriendTicks = 18000;
	public static final int demonFriendExtTicks = 30000;

	private static void registerPotions() {

		demonFriend = new PotionCustom("demon_warding", false, new Color(255,255,0).getRGB());
		LCRegistry.MOB_EFFECTS.register("demon_warding", () -> demonFriend);

		demonFriendNormal = new Potion("demon_warding_normal",
				new MobEffectInstance(demonFriend, demonFriendTicks));
		demonFriendExtended = new Potion("demon_warding_extended",
				new MobEffectInstance(demonFriend, demonFriendExtTicks));
		LCRegistry.POTIONS.register("demon_warding_normal", () -> demonFriendNormal);
		LCRegistry.POTIONS.register("demon_warding_extended", () -> demonFriendExtended);
	}

	public static ItemStack getPotionWithType(Potion type) {
		ItemStack potion = new ItemStack(Items.POTION);
		return PotionUtils.setPotion(potion, type);
	}

}