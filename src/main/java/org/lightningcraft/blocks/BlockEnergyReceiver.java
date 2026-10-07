package org.lightningcraft.blocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.lightningcraft.LightningCraft;
import org.lightningcraft.blocks.base.BlockEnergy;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.gui.LCGuiHandler;
import org.lightningcraft.tiles.TileEntityEnergyReceiver;

public class BlockEnergyReceiver extends BlockEnergy {

	public BlockEnergyReceiver() {
		super(Blocks.IRON_BLOCK, 10, 100);
	}

	@Override
	public BlockEntity createNewTileEntity(BlockPos pos, BlockState state) {
		return new TileEntityEnergyReceiver(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult side) {
		LCGuiHandler.open(player, LCGuiHandler.lightningRFtoLEGui, pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	public int getMaxRFPerTick() {
		return TileEntityEnergyReceiver.rfPerTick;
	}

	@Override
	public int getRFLERatio() {
		return LCConfig.RFtoLEConversion * 10;
	}

}
