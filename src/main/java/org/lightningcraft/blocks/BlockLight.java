package org.lightningcraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.blocks.base.BlockMeta;

public class BlockLight extends BlockMeta {

	public static final int nVariants = 5;

	public static final int UNDER = 0;
	public static final int UNDER_LAMP = 1;
	public static final int UNDER_LAMP_FANCY = 2;

	public static final int UNDER_LAMP_FANCY_2 = 3;
	public static final int UNDER_LAMP_FANCY_3 = 4;

	public BlockLight() {
		super(Blocks.STONE, nVariants, 10, 100, false);
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		return 15;
	}

}
