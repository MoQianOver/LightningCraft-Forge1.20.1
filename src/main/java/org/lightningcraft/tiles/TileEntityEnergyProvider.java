package org.lightningcraft.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.init.LCTileEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.util.EnergyStorage;

public class TileEntityEnergyProvider extends TileEntityEnergy {

	public static final int maxStorage = 100000;
	public static final int rfPerTick = 1024;

	public TileEntityEnergyProvider(BlockPos pos, BlockState state) {
		super(LCTileEntities.ENERGY_PROVIDER.get(), pos, state);
		storage = new EnergyStorage(maxStorage, 0, rfPerTick);
	}

	public void tick() {
		super.tick();

		if(!level.isClientSide && this.hasLPCell()) {

			if(!level.hasNeighborSignal(pos) &&
					storage.getEnergyStored() <= storage.getMaxEnergyStored() - LCConfig.RFtoLEConversion &&
					this.canDrawCellPower(1)) {
				this.drawCellPower(1);
				storage.modifyEnergyStored(LCConfig.RFtoLEConversion);
				this.setChanged();
			}
		}
	}

	@Override
	public int getEnergyStored(Direction from) {
		return storage.getEnergyStored();
	}

	@Override
	public void setEnergyStored(int energy) {
		storage.setEnergyStored(energy);
	}

	public int getMaxEnergyStored(Direction from) {
		return storage.getMaxEnergyStored();
	}

	public boolean canConnectEnergy(Direction from) {
		return true;
	}

	public int extractEnergy(Direction from, int toExtract, boolean simulate) {
		if(!simulate) this.setChanged();
		return storage.extractEnergy(toExtract, simulate);
	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);
		storage.readFromNBT(tagCompound);
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);
		storage.writeToNBT(tagCompound);
	}

}
