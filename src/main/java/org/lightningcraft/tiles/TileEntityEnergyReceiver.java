package org.lightningcraft.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.init.LCTileEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.util.Effect;
import org.lightningcraft.util.EnergyStorage;

public class TileEntityEnergyReceiver extends TileEntityEnergy {

	public static final int maxStorage = 100000;
	public static final int rfPerTick = 512;

	public TileEntityEnergyReceiver(BlockPos pos, BlockState state) {
		super(LCTileEntities.ENERGY_RECEIVER.get(), pos, state);
		storage = new EnergyStorage(maxStorage, rfPerTick, 0);
	}

	public void tick() {
		super.tick();

		if(!level.isClientSide && this.hasLPCell() && !this.isClientSidelyPowered()) {

			if(level.getGameTime() % 100 == 0 && !level.hasNeighborSignal(pos) &&
					storage.getEnergyStored() >= LCConfig.RFtoLEConversion * 10 * 100 &&
					this.cellPower < this.maxPower - 100D * this.tileCell.efficiency) {
				Effect.lightningGen(this.level, this.tileCell.getBlockPos().above());
				storage.modifyEnergyStored(-LCConfig.RFtoLEConversion * 10 * 100);
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

	public int receiveEnergy(Direction from, int toReceive, boolean simulate) {
		if(!simulate) this.setChanged();
		return storage.receiveEnergy(toReceive, simulate);
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
