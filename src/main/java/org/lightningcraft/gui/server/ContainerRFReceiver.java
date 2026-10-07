package org.lightningcraft.gui.server;
import org.lightningcraft.init.LCMenus;

import net.minecraft.world.entity.player.Inventory;

import org.lightningcraft.tiles.TileEntityEnergy;

public class ContainerRFReceiver extends ContainerRFProvider {

	public ContainerRFReceiver(int windowId, Inventory player, net.minecraft.core.BlockPos pos) {
		super(windowId, player, pos);
		TileEntityEnergy tile = LCMenus.tileAt(player, pos);
	}

}
