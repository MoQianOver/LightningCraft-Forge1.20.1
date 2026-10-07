package org.lightningcraft.items;

import net.minecraft.world.item.Item;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.gui.client.GuiLightningGuide;
import org.lightningcraft.init.LCCreativeTabs;
import org.lightningcraft.items.base.ItemMeta;

public class ItemLightningGuide extends ItemMeta {

	public ItemLightningGuide() {
		super(3, false, new boolean[]{false}, new Item.Properties().stacksTo(1));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (level.isClientSide) {
			Minecraft.getInstance().setScreen(new GuiLightningGuide(stack.getDamageValue()));
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack a, Level level, Entity player, int par4, boolean par5) {

	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		switch(stack.getDamageValue()) {
		case 1:
			return Rarity.UNCOMMON;
		case 2:
			return Rarity.RARE;
		default:
			return Rarity.COMMON;
		}
	}

}
