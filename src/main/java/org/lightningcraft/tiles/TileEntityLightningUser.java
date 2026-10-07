package org.lightningcraft.tiles;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public abstract class TileEntityLightningUser extends TileEntityBase {

	protected TileEntityLightningUser(net.minecraft.world.level.block.entity.BlockEntityType<?> type, net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
		super(type, pos, state);
	}

	public TileEntityLightningCell tileCell;
	public double cellPower;
	public double maxPower;
	private double efficiency;

	protected boolean isClientSidelyPowered() {
		return this.level != null && this.level.hasNeighborSignal(this.getBlockPos());
	}

	protected boolean hasLPCell() {
		if(level == null) return false;

		BlockEntity tile;

		for(int x = getX() - 1; x <= getX() + 1; x++) {
			for(int y = getY() - 1; y <= getY() + 1; y++) {
				for(int z = getZ() - 1; z <= getZ() + 1; z++) {

					if(!(x == getX() && y == getY() && z == getZ())) {
						tile = this.level.getBlockEntity(new BlockPos(x, y, z));

						if(tile != null && tile instanceof TileEntityLightningCell) {

							this.tileCell = (TileEntityLightningCell) tile;
							this.cellPower = this.tileCell.storedPower;
							this.maxPower = this.tileCell.maxPower;
							this.setEfficiency(1.0);
							return true;
						}
					}
				}
			}
		}

		if(!(this instanceof TileEntityLightningTransmitter)) {
			for(int x = getX() - 1; x <= getX() + 1; x++) {
				for(int y = getY() - 1; y <= getY() + 1; y++) {
					for(int z = getZ() - 1; z <= getZ() + 1; z++) {

						if(!(x == getX() && y == getY() && z == getZ())) {
							tile = this.level.getBlockEntity(new BlockPos(x, y, z));

							if(tile != null && tile instanceof TileEntityLightningReceiver) {
								TileEntityLightningReceiver rx = (TileEntityLightningReceiver)tile;

								if(rx.tx != null && rx.tx.hasLPCell()) {
									this.tileCell = rx.tx.tileCell;
									this.cellPower = this.tileCell.storedPower;
									this.maxPower = this.tileCell.maxPower;
									this.setEfficiency(rx.efficiency);
									return true;
								}
							}
						}
					}
				}
			}
		}

		this.tileCell = null;
		this.cellPower = 0;
		this.maxPower = -1;
		this.setEfficiency(-1);
		return false;
	}

	protected boolean isRemotelyPowered() {
		return this.getEfficiency() < 1;
	}

	protected boolean canDrawCellPower(double amount) {
		return getEfficiency() > 0 &&
				this.cellPower >= amount / getEfficiency() && this.tileCell.storedPower >= amount / getEfficiency();
	}

	protected boolean canAddCellPower(double amount) {
		return getEfficiency() > 0 &&
				this.cellPower + amount * getEfficiency() <= maxPower && this.tileCell.storedPower + amount * getEfficiency() <= this.tileCell.maxPower;
	}

	public double getActualNeededPower(double amount) {
		if(getEfficiency() > 0) {
			return amount / getEfficiency();
		} else {
			return Double.NaN;
		}
	}

	protected boolean drawCellPower(double amount) {
		if(this.canDrawCellPower(amount)) {
			this.cellPower -= amount / getEfficiency();
			this.tileCell.storedPower -= amount / getEfficiency();
			return true;
		} else {
			return false;
		}
	}

	protected boolean addCellPower(double amount) {
		if(this.canAddCellPower(amount)) {
			this.cellPower += amount * getEfficiency();
			this.tileCell.storedPower += amount * getEfficiency();
			return true;
		} else {
			return false;
		}
	}

	public double getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(double efficiency) {
		this.efficiency = efficiency;
	}

}
