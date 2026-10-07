package org.lightningcraft.items.blocks;

import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.IInventoryLEUser;
import org.lightningcraft.blocks.BlockLightningCell;

public class ItemBlockLightningCell extends ItemBlockMetal {

	private BlockLightningCell block;

	public ItemBlockLightningCell(Block block) {
		super(block);
		this.block = (BlockLightningCell)block;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		list.add(Component.translatable("tooltip.lightningcraft.capacity", block.getMaxPower(block.getStateFromMeta(stack.getDamageValue()))));
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		if(stack.getDamageValue() >= 3) {
			return IInventoryLEUser.ILERarity;
		} else {
			return super.getRarity(stack);
		}
	}

}
