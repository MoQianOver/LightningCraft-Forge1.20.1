package org.lightningcraft.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.IInventoryLEUser;

public interface ISimpleLEUser extends IInventoryLEUser {

	@Override
	public default double getAvailablePower(Player invOwner) {
		return InventoryLE.getAvailablePower(invOwner);
	}

	@Override
	public default boolean hasLESource(Player invOwner) {
		return InventoryLE.hasLESource(invOwner);
	}

	@Override
	public default ItemStack getLESource(Player invOwner, double leNeeded) {
		return InventoryLE.getLESource(invOwner, leNeeded);
	}

}
