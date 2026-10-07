package org.lightningcraft.items.blocks;

import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.blocks.BlockAirTerminal;
import org.lightningcraft.ref.Metal.Rod;

public class ItemBlockAirTerminal extends ItemBlockMeta {

	private BlockAirTerminal block;

	public ItemBlockAirTerminal(Block block) {
		super(block);
		this.block = (BlockAirTerminal)block;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		list.add(Component.translatable("tooltip.lightningcraft.efficiency", (int)(block.getEfficiency(block.getStateFromMeta(stack.getDamageValue())) * 100D)));
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		switch(stack.getDamageValue()) {
		case Rod.ELEC:
			return Rarity.UNCOMMON;
		case Rod.SKY:
			return Rarity.RARE;
		case Rod.MYSTIC:
			return Rarity.EPIC;
		default:
			return super.getRarity(stack);
		}
	}

}
