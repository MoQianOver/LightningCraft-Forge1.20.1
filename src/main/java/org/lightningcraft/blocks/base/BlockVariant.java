package org.lightningcraft.blocks.base;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface BlockVariant {

	IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 15);

	int getSubBlockCount();

	default BlockState getStateFromMeta(int meta) {
		int max = Math.max(0, getSubBlockCount() - 1);
		return ((Block) this).defaultBlockState().setValue(VARIANT, Math.max(0, Math.min(meta, max)));
	}

	default int getMetaFromState(BlockState state) {
		return state.getValue(VARIANT);
	}

	static int getMeta(BlockState state) {
		return state.hasProperty(VARIANT) ? state.getValue(VARIANT) : 0;
	}

}
