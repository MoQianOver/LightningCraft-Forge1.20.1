package org.lightningcraft.integration.chisel;

import org.lightningcraft.util.LCStack;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import team.chisel.api.carving.ICarvingVariation;

@Optional.Interface(iface = "team.chisel.api.carving.ICarvingVariation", modid = "chisel")
public class CarvingVariation implements ICarvingVariation {

	private Block block;
	private int meta;

	public CarvingVariation(Block block, int meta) {
		this.block = block;
		this.meta = meta;
	}

	@Override
	public Block getBlock() {
		return block;
	}

	@Override
	public BlockState getBlockState() {
		return block.getStateFromMeta(meta);
	}

	@Override
	public ItemStack getStack() {
		return LCStack.of(block, 1, meta);
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
