package org.lightningcraft.blocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.item.BlockItem;

import org.lightningcraft.util.LCStack;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.lightningcraft.LightningCraft;
import org.lightningcraft.blocks.base.BlockContainerLCMeta;
import org.lightningcraft.gui.LCGuiHandler;
import org.lightningcraft.items.blocks.ItemBlockWireless;
import org.lightningcraft.tiles.TileEntityLightningReceiver;
import org.lightningcraft.tiles.TileEntityLightningTransmitter;

public class BlockWireless extends BlockContainerLCMeta {

	public static final int nTransmitters = 3;
	public static final int nReceivers = 3;
	public static final int nSubBlocks = nTransmitters + nReceivers;

	public BlockWireless() {

		super(Blocks.IRON_BLOCK, nSubBlocks, 5, 30, net.minecraft.world.level.block.RenderShape.MODEL, false, true);
	}

	@Override
	public BlockEntity createNewTileEntity(BlockPos pos, BlockState state) {

		int meta = getMetaFromState(state);
		WirelessSpecs params = new WirelessSpecs();
		if(getTileEntityPerformance(meta, params)) {
			return new TileEntityLightningTransmitter(params.range, params.quality, pos, state);
		} else {
			return new TileEntityLightningReceiver(params.rolloff, pos, state);
		}
	}

	public boolean getTileEntityPerformance(int meta, WirelessSpecs params) {

		if(meta < nTransmitters) {
			switch(meta) {
			case 0:
				params.range = 32;
				params.quality = 0.45;
				break;
			case 1:
				params.range = 48;
				params.quality = 0.65;
				break;
			case 2:
				params.range = 64;
				params.quality = 0.95;
				break;
			default:
				params.range = 0;
				params.quality = 1d;
				break;
			}
			return true;
		} else {
			switch(meta - nTransmitters) {
			case 0:
				params.rolloff = 0.00025;
				break;
			case 1:
				params.rolloff = 0.00015;
				break;
			case 2:
				params.rolloff = 0.000075;
				break;
			default:
				params.rolloff = 1d;
				break;
			}
			return false;
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult side) {
		if(this.getMetaFromState(state) >= nTransmitters) {
			LCGuiHandler.open(player, LCGuiHandler.lightningRXGui, pos);
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}

	public static class WirelessSpecs {
		public double range;
		public double quality;
		public double rolloff;
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockWireless.class;
	}

}
