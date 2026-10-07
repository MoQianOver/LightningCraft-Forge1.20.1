package org.lightningcraft.tiles.ifaces;
import net.minecraft.core.Direction;

import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.tiles.TileEntityBase;

public interface ISidedInventoryLC extends WorldlyContainer {

	@Override
	public default boolean stillValid(Player player) {
		TileEntityBase tile = (TileEntityBase) this;
		if (tile.getLevel() == null) return false;
		return tile.getLevel().getBlockEntity(tile.getBlockPos()) != tile ? false :
			player.distanceToSqr(tile.getX() + 0.5D, tile.getY() + 0.5D, tile.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public default int getMaxStackSize() {
		return 64;
	}

	@Override
	public default boolean canPlaceItemThroughFace(int index, ItemStack stack, @javax.annotation.Nullable Direction direction) {
		return this.canPlaceItem(index, stack);
	}

	@Override
	public default boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		return true;
	}

	@Override
	public default void startOpen(Player p) {}

	@Override
	public default void stopOpen(Player p) {}

	@Override
	public default void clearContent() {}

	@Override
	public default boolean isEmpty() {
		for (int i = 0; i < this.getContainerSize(); i++) {
			if (!this.getItem(i).isEmpty()) return false;
		}
		return true;
	}

}
