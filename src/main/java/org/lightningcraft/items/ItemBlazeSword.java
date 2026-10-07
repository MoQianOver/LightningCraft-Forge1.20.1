package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Tier;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.items.base.ItemSpecialSword;
import org.lightningcraft.ref.LCText;

public class ItemBlazeSword extends ItemSpecialSword {

	public ItemBlazeSword(Tier mat) {
		super(mat, 0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		list.add(Component.literal(LCText.getBlazeSwordLore()));
	}

}
