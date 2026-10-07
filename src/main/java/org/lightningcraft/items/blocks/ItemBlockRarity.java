package org.lightningcraft.items.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

public class ItemBlockRarity extends BlockItem {

	private final Rarity rarity;

	public ItemBlockRarity(Block block, Rarity rarity) {
		super(block, new Item.Properties());
		this.rarity = rarity;
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return rarity;
	}

}
