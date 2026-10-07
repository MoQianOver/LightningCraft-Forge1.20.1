package org.lightningcraft.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class ToolHelper {

	private static final Map<Tier, ItemStack> matMap = new HashMap<>();

	public static void setRepairStack(Tier mat, @Nonnull ItemStack repair) {
		matMap.put(mat, repair);
		if (mat instanceof org.lightningcraft.ref.LCTier lcTier) {
			lcTier.setRepair(repair);
		}
	}

	public static @Nonnull ItemStack getRepairStack(Tier mat) {
		ItemStack cached = matMap.get(mat);
		if (cached != null) return cached;
		try {
			ItemStack[] items = mat.getRepairIngredient().getItems();
			return items.length > 0 ? items[0] : ItemStack.EMPTY;
		} catch (Exception e) {
			return ItemStack.EMPTY;
		}
	}

}
