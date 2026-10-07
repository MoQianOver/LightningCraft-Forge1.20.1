package org.lightningcraft.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.init.LCTileEntities;

import net.minecraft.nbt.CompoundTag;

public class TileEntityLightningTransmitter extends TileEntityLightningUser {

	public double range;
	public double quality;

	public TileEntityLightningTransmitter(double range, double quality, BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_TRANSMITTER.get(), pos, state);
		this.range = range;
		this.quality = quality;
	}

	public TileEntityLightningTransmitter(BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_TRANSMITTER.get(), pos, state);}

	public void tick() {}

	@Override
	public boolean hasLPCell() {
		return super.hasLPCell();
	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);
		range = tagCompound.getDouble("Range");
		quality = tagCompound.getDouble("Quality");
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);
		tagCompound.putDouble("Range", range);
		tagCompound.putDouble("Quality", quality);
	}

}
