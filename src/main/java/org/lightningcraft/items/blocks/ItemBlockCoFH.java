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
import org.lightningcraft.blocks.base.BlockEnergy;

public class ItemBlockCoFH extends ItemBlockRarity {

	private BlockEnergy block;

	public ItemBlockCoFH(Block block) {
		super(block, Rarity.UNCOMMON);
		this.block = (BlockEnergy)block;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		list.add(Component.translatable("tooltip.lightningcraft.max_transfer", block.getMaxRFPerTick()));
		list.add(Component.translatable("tooltip.lightningcraft.rf_le_ratio", block.getRFLERatio()));
	}

}
