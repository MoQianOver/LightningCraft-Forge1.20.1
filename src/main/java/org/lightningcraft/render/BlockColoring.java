package org.lightningcraft.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.registry.ILightningCraftBlock;
import org.lightningcraft.blocks.BlockAirTerminal;
import org.lightningcraft.blocks.BlockWireless;
import org.lightningcraft.blocks.base.BlockVariant;
import org.lightningcraft.ref.Metal.Ingot;
import org.lightningcraft.ref.Metal.Rod;

@OnlyIn(Dist.CLIENT)
public class BlockColoring implements BlockColor, ItemColor {

	private static final int DEFAULT = 0xFFFFFF;
	private static final BlockColoring instance = new BlockColoring();

	public static void registerBlock(ILightningCraftBlock block) {
		Minecraft.getInstance().getBlockColors().register(instance, (Block) block);
		if(block.getItemClass() != null) {
			Minecraft.getInstance().getItemColors().register(instance, (Block) block);
		}
	}

	@Override
	public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
		Block block = state.getBlock();
		int meta = BlockVariant.getMeta(state);
		if(block instanceof BlockAirTerminal) {
			return Rod.getColorFromMeta(meta);
		} else if(block instanceof BlockWireless) {
			return Ingot.getColorFromMeta(meta % 3);
		} else {
			return DEFAULT;
		}
	}

	@Override
	public int getColor(ItemStack stack, int tintIndex) {
		Block block = Block.byItem(stack.getItem());
		if(block != Blocks.AIR && block instanceof BlockVariant variant) {
			return getColor(variant.getStateFromMeta(stack.getDamageValue()), null, null, tintIndex);
		} else {
			return DEFAULT;
		}
	}

}