package org.lightningcraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.api.registry.ILightningCraftBlock;

public class BlockLogLC extends RotatedPillarBlock implements ILightningCraftBlock {

	public BlockLogLC() {
		super(BlockBehaviour.Properties.copy(Blocks.OAK_LOG));

		this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y));
	}

	public BlockState getStateFromMeta(int meta) {
		Direction.Axis axis;
		switch (meta & 12) {
			case 4:  axis = Direction.Axis.X; break;
			case 8:  axis = Direction.Axis.Z; break;
			default: axis = Direction.Axis.Y; break;
		}
		return this.defaultBlockState().setValue(AXIS, axis);
	}

	public int getMetaFromState(BlockState state) {
		switch (state.getValue(AXIS)) {
			case X: return 4;
			case Z: return 8;
			default: return 0;
		}
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return new ItemStack(this);
	}

}
