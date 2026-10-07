package org.lightningcraft.blocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.lightningcraft.LightningCraft;
import org.lightningcraft.gui.LCGuiHandler;
import org.lightningcraft.tiles.TileEntityLightningMiner;

public class BlockLightningMiner extends BlockLightningBreaker {

	@Override
	public BlockEntity createNewTileEntity(BlockPos pos, BlockState state) {
		return new TileEntityLightningMiner(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult side) {
		LCGuiHandler.open(player, LCGuiHandler.lightningMinerGui, pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		BlockEntity tileentity = worldIn.getBlockEntity(pos);

		Containers.dropContents(worldIn, pos, (Container)tileentity);
		worldIn.updateNeighbourForOutputSignal(pos, this);

		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

	@Override
	public Object[] getItemClassArgs() {
		return new Object[]{Rarity.RARE};
	}

}
