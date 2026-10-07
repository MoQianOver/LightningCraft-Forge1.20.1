package org.lightningcraft.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.lightningcraft.tiles.TileEntityEnergy;

public class ForgeEnergyUtils {

	public static void handleAdjacentEnergy(TileEntityEnergy origin) {
		for (Direction face : Direction.values()) {

			BlockEntity adjacent = origin.getLevel().getBlockEntity(origin.getBlockPos().relative(face));
			if (adjacent == null || adjacent instanceof TileEntityEnergy) {
				continue;
			}

			Direction swap = face.getOpposite();

			IEnergyStorage provider = origin.getCapability(ForgeCapabilities.ENERGY, face).orElse(null);
			IEnergyStorage receiver = adjacent.getCapability(ForgeCapabilities.ENERGY, swap).orElse(null);
			if (provider == null || receiver == null) {
				continue;
			}

			int calc = provider.extractEnergy(Integer.MAX_VALUE, true);
			if (calc > 0 && provider.canExtract() && receiver.canReceive()) {
				int realRF = receiver.receiveEnergy(calc, false);
				provider.extractEnergy(realRF, false);
			}
		}
	}

}
