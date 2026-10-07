package org.lightningcraft.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class LCStack {

	private LCStack() {}

	public static ItemStack of(ItemLike item, int count, int meta) {
		ItemStack stack = new ItemStack(item, count);
		stack.setDamageValue(meta);
		return stack;
	}

	public static ItemStack of(ItemLike item, int meta) {
		return of(item, 1, meta);
	}

	public static ItemStack copyOf(ItemStack stack, int meta) {
		ItemStack copy = stack.copy();
		copy.setDamageValue(meta);
		return copy;
	}
}
