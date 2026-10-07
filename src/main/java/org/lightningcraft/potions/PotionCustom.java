package org.lightningcraft.potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class PotionCustom extends MobEffect {

	public PotionCustom(String stringID, boolean isBad, int liquidColor) {
		super(isBad ? MobEffectCategory.HARMFUL : MobEffectCategory.BENEFICIAL, liquidColor);
	}

}
