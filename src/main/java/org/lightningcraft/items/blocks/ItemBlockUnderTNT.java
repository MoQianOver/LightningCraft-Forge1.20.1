package org.lightningcraft.items.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.blocks.BlockUnderTNT;

public class ItemBlockUnderTNT extends ItemBlockMeta {

	public ItemBlockUnderTNT(Block block) {
		super(block);
	}

	@Override
	@net.minecraftforge.api.distmarker.OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)
	public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
			java.util.List<net.minecraft.network.chat.Component> list,
			net.minecraft.world.item.TooltipFlag flag) {
		list.add(net.minecraft.network.chat.Component.translatable(
				stack.getDamageValue() >= 8
						? "tooltip.lightningcraft.tnt_explodes"
						: "tooltip.lightningcraft.tnt_decorative"));
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		switch(stack.getDamageValue() % 8) {
		case BlockUnderTNT.MYSTIC:
			return Rarity.EPIC;
		case BlockUnderTNT.LIGHTNING:
			return Rarity.RARE;
		default:
			return Rarity.UNCOMMON;
		}
	}

	@Override
	public String getDescriptionId(ItemStack stack) {

		return this.getBlock().getDescriptionId() + "_" + (stack.getDamageValue() % 8);
	}

	@Override
	public void addCreativeTabEntries() {
		for (int i = 0; i < BlockUnderTNT.nVariants; ++i) {
			org.lightningcraft.creativetabs.LCCreativeTab.addBlockEntry(
					org.lightningcraft.util.LCStack.of(this, 1, i));
			org.lightningcraft.creativetabs.LCCreativeTab.addBlockEntry(
					org.lightningcraft.util.LCStack.of(this, 1, i + 8));
		}
	}

}
