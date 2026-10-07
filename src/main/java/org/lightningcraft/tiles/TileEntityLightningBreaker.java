package org.lightningcraft.tiles;
import net.minecraft.server.level.ServerLevel;

import org.lightningcraft.init.LCTileEntities;

import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.ForgeEventFactory;

public class TileEntityLightningBreaker extends TileEntityLightningUser {

	public TileEntityLightningBreaker(BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_BREAKER.get(), pos, state);
	}

	public static final int breakModTime = 10;
	private static final double breakLE = 0.3;

	public void tick() {
		if(!level.isClientSide && !level.hasNeighborSignal(pos) && level.getGameTime() % breakModTime == 0 &&
				this.hasLPCell() && this.canDrawCellPower(breakLE)) {
			BlockPos check = this.pos.relative(Direction.from3DDataValue(this.getBlockMetadata()));

			BlockState checkState = level.getBlockState(check);
			Block checkBlock = checkState.getBlock();
			if(checkBlock != null && !checkState.isAir() && checkState.getDestroySpeed(level, check) >= 0) {
				List<ItemStack> drops = Block.getDrops(checkState, (ServerLevel) this.level, check, null);

	            float chance = 1.0F;
	            if(random.nextFloat() <= chance) {
	            	this.drawCellPower(breakLE);
	            	level.destroyBlock(check, true);
	            }
			}
		}
	}

}
