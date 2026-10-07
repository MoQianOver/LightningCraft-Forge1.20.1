package org.lightningcraft.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.lightningcraft.api.registry.ILightningCraftBlock;

public class BlockWallLC extends WallBlock implements ILightningCraftBlock {

	public BlockWallLC(BlockBehaviour.Properties properties) {
		super(properties);
	}

	public BlockWallLC() {
		super(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE_WALL));
	}

}
