package org.lightningcraft.tiles;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;
import org.lightningcraft.integration.energy.EnergyApiHelper;
import org.lightningcraft.util.EnergyStorage;
import org.lightningcraft.util.ForgeEnergyUtils;

public abstract class TileEntityEnergy extends TileEntityLightningUser {

	protected TileEntityEnergy(net.minecraft.world.level.block.entity.BlockEntityType<?> type, net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
		super(type, pos, state);
	}

	protected static final Capability<IEnergyStorage> STORAGE_CAP = ForgeCapabilities.ENERGY;

	protected EnergyStorage storage;

	public abstract int getEnergyStored(Direction from);

	public abstract void setEnergyStored(int energy);

	protected boolean isClientSidelyPowered() {
		return this.level != null && this.level.hasNeighborSignal(this.getBlockPos());
	}

	public void tick() {
		if(!level.isClientSide) {
			if(EnergyApiHelper.rfLoaded) ForgeEnergyUtils.handleAdjacentEnergy(this);

		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (capability == ForgeCapabilities.ENERGY) {
			return LazyOptional.of(() -> (T) storage);
		}
		return super.getCapability(capability, facing);
	}

}
