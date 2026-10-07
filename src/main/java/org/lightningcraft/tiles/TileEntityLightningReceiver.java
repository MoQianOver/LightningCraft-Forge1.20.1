package org.lightningcraft.tiles;

import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.init.LCTileEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class TileEntityLightningReceiver extends TileEntityBase {

	public TileEntityLightningTransmitter tx;
	public BlockPos txPos;
	public double rolloff;
	public double efficiency;
	public double storedPower;
	public double maxPower;
	public boolean outOfRange;

	public TileEntityLightningReceiver(double rolloff, BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_RECEIVER.get(), pos, state);
		this.txPos = new BlockPos(0, -2, 0);
		this.rolloff = rolloff;
	}

	public TileEntityLightningReceiver(BlockPos pos, BlockState state) {
		this(0, pos, state);
	}

	public String getName() {
		int meta = org.lightningcraft.blocks.base.BlockVariant.getMeta(this.getBlockState());
		return org.lightningcraft.ref.Lang.tr(this.getBlockState().getBlock().getDescriptionId() + "_" + meta);
	}

	public void tick() {
		if(!level.isClientSide) {

			BlockEntity tile;
			int txX = txPos.getX();
			int txY = txPos.getY();
			int txZ = txPos.getZ();
			boolean hasTx = true;
			this.outOfRange = false;

			if(txY >= 0 && (tile = level.getBlockEntity(txPos)) != null && tile instanceof TileEntityLightningTransmitter) {
				tx = (TileEntityLightningTransmitter)tile;
				double distanceSq = Math.sqrt(Math.pow(tx.getX() - getX(), 2) + Math.pow(tx.getY() - getY(), 2) + Math.pow(tx.getZ() - getZ(), 2));
				if(distanceSq <= tx.range * tx.range) {
					if(tx.hasLPCell()) {
						this.storedPower = tx.cellPower;
						this.maxPower = tx.maxPower;
						this.efficiency = tx.quality - distanceSq * rolloff;
					} else {
						hasTx = false;
					}
				} else {
					hasTx = false;
					this.outOfRange = true;
				}
			} else {
				hasTx = false;
			}

			if(!hasTx) {
				this.storedPower = 0;
				this.maxPower = -1;
				this.efficiency = -1;
			}

		}
	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);
		int x = tagCompound.getInt("TransmitterX");
		int y = tagCompound.getInt("TransmitterY");
		int z = tagCompound.getInt("TransmitterZ");
		this.txPos = new BlockPos(x, y, z);
		this.storedPower = tagCompound.getDouble("StoredPower");
		this.maxPower = tagCompound.getDouble("MaxPower");
		this.rolloff = tagCompound.getDouble("Rolloff");
		this.efficiency = tagCompound.getDouble("Efficiency");
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);
		tagCompound.putDouble("TransmitterX", txPos.getX());
		tagCompound.putDouble("TransmitterY", txPos.getY());
		tagCompound.putDouble("TransmitterZ", txPos.getZ());
		tagCompound.putDouble("StoredPower", this.storedPower);
		tagCompound.putDouble("MaxPower", this.maxPower);
		tagCompound.putDouble("Rolloff", this.rolloff);
		tagCompound.putDouble("Efficiency", this.efficiency);
	}

}
