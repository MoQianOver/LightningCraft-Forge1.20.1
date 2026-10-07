package org.lightningcraft.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.creativetabs.LCCreativeTab;

public class LCCreativeTabs {

	public static final CreativeModeTab blocks = LCCreativeTab.BLOCKS;
	public static final CreativeModeTab items = LCCreativeTab.ITEMS;

	public static void mainRegistry() {
		LCCreativeTab.register();
	}

	public static void updateCreativeTabs() {
		LCCreativeTab.setBlocksIcon(new ItemStack(LCBlocks.metalBlock));
		LCCreativeTab.setItemsIcon(new ItemStack(LCItems.ingot));
	}

}
