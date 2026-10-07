package org.lightningcraft.blocks;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.item.BlockItem;

import java.util.Random;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.lightningcraft.LightningCraft;
import org.lightningcraft.blocks.base.BlockContainerLCMeta;
import org.lightningcraft.gui.LCGuiHandler;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.items.blocks.ItemBlockLightningCannon;
import org.lightningcraft.ref.Material;
import org.lightningcraft.tiles.TileEntityLightningCannon;

public class BlockLightningCannon extends BlockContainerLCMeta {

	public static final int nCannons = 3;

	public BlockLightningCannon() {
		super(Blocks.IRON_BLOCK, nCannons, 5, 150);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter source, BlockPos pos, CollisionContext context) {
	return Shapes.box(0.2F, 0.0F, 0.2F, 0.8F, 1.0F, 0.8F);
	}

	@Override
	public BlockEntity createNewTileEntity(BlockPos pos, BlockState state) {
		return new TileEntityLightningCannon(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult side) {
		if(getMetaFromState(state) > 0) {
			LCGuiHandler.open(player, LCGuiHandler.lightningCannonGui, pos);
			return InteractionResult.SUCCESS;
		} else {
			return super.use(state, level, pos, player, hand, side);
		}
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockLightningCannon.class;
	}

}
