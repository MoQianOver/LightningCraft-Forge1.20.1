package org.lightningcraft.blocks;

import net.minecraft.world.level.block.Block;

import net.minecraft.world.item.BlockItem;

import com.google.common.base.Predicate;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.lightningcraft.blocks.base.BlockContainerLC;
import org.lightningcraft.items.blocks.ItemBlockRarity;
import org.lightningcraft.tiles.TileEntityLightningBreaker;

public class BlockLightningBreaker extends BlockContainerLC {

	private static final DirectionProperty dir = DirectionProperty.create("facing", new Predicate<Direction>() {
		@Override
		public boolean apply(Direction facing) {
			return facing != null;
		}
	});

	public BlockLightningBreaker() {
		super(Blocks.IRON_BLOCK, 3.0f, 30.0f);
		this.registerDefaultState(this.stateDefinition.any().setValue(dir, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(dir);
	}

	@Override
	public BlockEntity createNewTileEntity(BlockPos pos, BlockState state) {
		return new TileEntityLightningBreaker(pos, state);
	}

		public BlockState getStateFromMeta(int meta) {
	    return defaultBlockState().setValue(dir, Direction.from3DDataValue(meta));
	}

		public int getMetaFromState(BlockState state) {
		return state.getValue(dir).get3DDataValue();
	}

	@Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity player, ItemStack stack) {

        Direction facing = Direction.getNearest(
                player.getX() - (pos.getX() + 0.5D),
                player.getY() - (pos.getY() + 0.5D),
                player.getZ() - (pos.getZ() + 0.5D));
        int rotation = facing.ordinal();
        level.setBlock(pos, this.getStateFromMeta(rotation), 2);
        super.setPlacedBy(level, pos, state, player, stack);
    }

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockRarity.class;
	}

	@Override
	public Object[] getItemClassArgs() {
		return new Object[]{Rarity.UNCOMMON};
	}

}
