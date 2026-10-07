package org.lightningcraft.blocks.base;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.tiles.TileEntityBase;

public abstract class BlockContainerLC extends BlockLC implements EntityBlock {

	protected RenderShape renderShape;

	protected final Random random = new Random();

	public BlockContainerLC(Block parent, float hardness, float resistance, RenderShape renderShape) {
		this(parent, hardness, resistance, renderShape, false);
	}

	public BlockContainerLC(Block parent, float hardness, float resistance, RenderShape renderShape,
			boolean noOcclusion) {
		super(parent, hardness, resistance, noOcclusion);
		this.renderShape = renderShape;
	}

	public BlockContainerLC(Block parent, float hardness, float resistance) {
		this(parent, hardness, resistance, RenderShape.MODEL);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return createNewTileEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {
		return (lvl, pos, st, be) -> {
			if (be instanceof TileEntityBase base) {
				base.tick();
			}
		};
	}

	public abstract BlockEntity createNewTileEntity(BlockPos pos, BlockState state);

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return renderShape;
	}

	@Override
	public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
		super.triggerEvent(state, level, pos, id, param);
		BlockEntity tileentity = level.getBlockEntity(pos);
		return tileentity == null ? false : tileentity.triggerEvent(id, param);
	}

	@Override
	public ItemStack getCloneItemStack(net.minecraft.world.level.BlockGetter level, BlockPos pos, BlockState state) {
		return new ItemStack(this);
	}

}
