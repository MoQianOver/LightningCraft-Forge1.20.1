package org.lightningcraft.api;

import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;

public interface IPotionEffectProvider {

	public boolean canApplyEffect(ItemStack stack, Player player, int invPosition);

	public List<MobEffectInstance> getEffects(ItemStack stack, Player player, int invPosition);

}
