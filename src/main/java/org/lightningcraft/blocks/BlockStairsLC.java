package org.lightningcraft.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.lightningcraft.api.registry.ILightningCraftBlock;
import org.lightningcraft.init.LCBlocks;

public class BlockStairsLC extends StairBlock implements ILightningCraftBlock {

	public BlockStairsLC(Block block) {
		super(block.defaultBlockState(), BlockBehaviour.Properties.copy(block));
	}

	public BlockStairsLC() {
		this(LCBlocks.stoneBlock);
	}

}
