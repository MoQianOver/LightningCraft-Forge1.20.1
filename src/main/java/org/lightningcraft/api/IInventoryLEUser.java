package org.lightningcraft.api;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;

public interface IInventoryLEUser {

	public static final Rarity ILERarity = Rarity.EPIC;

	public static final double repairCost = 0.1;

	public double getAvailablePower(Player invOwner);

	public boolean hasLESource(Player invOwner);

	public @Nonnull ItemStack getLESource(Player invOwner, double leNeeded);

}
