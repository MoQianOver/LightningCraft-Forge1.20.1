package org.lightningcraft.items.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.lightningcraft.creativetabs.LCCreativeTab;
import org.lightningcraft.util.LCStack;

public class ItemBlockMeta extends BlockItem {

	private final int subItemCount;

	public ItemBlockMeta(Block block) {
		this(block, 1);
	}

	public ItemBlockMeta(Block block, int subItemCount) {
		super(block, new Item.Properties());
		this.subItemCount = subItemCount;
	}

	public int getSubItemCount() {
		return subItemCount;
	}

	public void addCreativeTabEntries() {
		for (int meta = 0; meta < subItemCount; meta++) {
			LCCreativeTab.addBlockEntry(LCStack.of(this, 1, meta));
		}
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return super.getDescriptionId(stack) + "_" + stack.getDamageValue();
	}

}
