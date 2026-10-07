package org.lightningcraft.recipes;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.lightningcraft.blocks.BlockSlabLC;
import org.lightningcraft.init.LCBlocks;

public class LCFuelHandler {

	public static int getBurnTime(ItemStack fuel) {
		Item item = fuel.getItem();
		Block block = Block.byItem(item);
		int meta = fuel.getDamageValue();

		if(block == LCBlocks.slabBlock && meta == BlockSlabLC.UNDER_PLANK) {
			return 150;
		}

		return 0;
	}

}
