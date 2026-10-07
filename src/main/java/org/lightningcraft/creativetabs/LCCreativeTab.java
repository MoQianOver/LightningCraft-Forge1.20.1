package org.lightningcraft.creativetabs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class LCCreativeTab {

	private LCCreativeTab() {}

	public static final List<ItemStack> BLOCK_ENTRIES = new ArrayList<>();

	public static final List<ItemStack> ITEM_ENTRIES = new ArrayList<>();

	private static ItemStack blocksIcon = ItemStack.EMPTY;
	private static ItemStack itemsIcon = ItemStack.EMPTY;

	public static final CreativeModeTab BLOCKS = CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.lcBlocks"))
			.icon(() -> blocksIcon)
			.displayItems((params, output) -> BLOCK_ENTRIES.forEach(output::accept))
			.build();

	public static final CreativeModeTab ITEMS = CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.lcItems"))
			.icon(() -> itemsIcon)
			.displayItems((params, output) -> ITEM_ENTRIES.forEach(output::accept))
			.build();

	public static void register() {
		org.lightningcraft.init.LCRegistry.CREATIVE_TABS.register("blocks", () -> BLOCKS);
		org.lightningcraft.init.LCRegistry.CREATIVE_TABS.register("items", () -> ITEMS);
	}

	public static void addBlockEntry(ItemStack stack) {
		BLOCK_ENTRIES.add(stack);
	}

	public static void addItemEntry(ItemStack stack) {
		ITEM_ENTRIES.add(stack);
	}

	public static void setBlocksIcon(ItemStack stack) {
		blocksIcon = stack;
	}

	public static void setItemsIcon(ItemStack stack) {
		itemsIcon = stack;
	}
}
