package org.lightningcraft.blocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.item.BlockItem;

import org.lightningcraft.ref.Lang;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.lightningcraft.LightningCraft;
import org.lightningcraft.blocks.base.BlockContainerLCMeta;
import org.lightningcraft.gui.LCGuiHandler;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.items.blocks.ItemBlockLightningCell;
import org.lightningcraft.tiles.TileEntityLightningCell;

public class BlockLightningCell extends BlockContainerLCMeta {

	public static final int nCells = 4;

	private double[] maxPower;

	public BlockLightningCell() {
		super(LCBlocks.metalBlock, nCells, 6.5f, 40.0f);
		maxPower = new double[]{1000, 5000, 20000, 100000};
	}

	@Override
	public BlockEntity createNewTileEntity(BlockPos pos, BlockState state) {

		int meta = getMetaFromState(state) % nCells;
		return new TileEntityLightningCell(maxPower[meta], cellNameOf(state), meta >= 3, pos, state);
	}

	public static String cellNameOf(BlockState state) {

		int meta = org.lightningcraft.blocks.base.BlockVariant.getMeta(state) % nCells;
		return Lang.tr(state.getBlock().getDescriptionId() + "_" + meta);
	}

	public int getMaxPower(BlockState state) {
		return (int)maxPower[getMetaFromState(state) % nCells];
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult side) {
		LCGuiHandler.open(player, LCGuiHandler.lightningCellGui, pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockLightningCell.class;
	}

}