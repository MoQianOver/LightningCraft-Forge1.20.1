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
import org.lightningcraft.blocks.BlockWireless;
import org.lightningcraft.blocks.BlockWireless.WirelessSpecs;

public class ItemBlockWireless extends ItemBlockMeta {

	private BlockWireless block;

	public ItemBlockWireless(Block block) {
		super(block);
		this.block = (BlockWireless)block;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		int meta = stack.getDamageValue();
		WirelessSpecs params = new WirelessSpecs();
		if(block.getTileEntityPerformance(meta, params)) {
			list.add(Component.translatable("tooltip.lightningcraft.quality", (int)(params.quality * 100D)));
			list.add(Component.translatable("tooltip.lightningcraft.range", (int)(params.range)));
		} else {
			list.add(Component.translatable("tooltip.lightningcraft.rolloff", params.rolloff * 100D));
		}
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		switch(stack.getDamageValue() % BlockWireless.nTransmitters) {
		case 0:
			return Rarity.UNCOMMON;
		case 1:
			return Rarity.RARE;
		case 2:
			return Rarity.EPIC;
		default:
			return super.getRarity(stack);
		}
	}

}
