package org.lightningcraft.items.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;

public class ItemSlabLC extends BlockItem {

	public ItemSlabLC(Block block, SlabBlock single, SlabBlock db) {
		super(block, new Item.Properties());
	}

	public ItemSlabLC(Block block) {
		super(block, new Item.Properties());
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return super.getDescriptionId(stack) + "_" + (stack.getDamageValue() % 8);
	}

	@Override
	public int getBurnTime(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.item.crafting.RecipeType<?> recipeType) {
		int t = org.lightningcraft.recipes.LCFuelHandler.getBurnTime(stack);
		return t > 0 ? t : -1;
	}}
