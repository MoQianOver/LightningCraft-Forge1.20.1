package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.UseOnContext;

import net.minecraft.world.item.Item;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.items.base.ItemLC;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.registry.ClientRegistryHelper;
import org.lightningcraft.tiles.TileEntityLightningReceiver;
import org.lightningcraft.tiles.TileEntityLightningTransmitter;

public class ItemWirelessMarker extends ItemLC {

	public ItemWirelessMarker() {
		super(Rarity.COMMON, new Item.Properties().stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		InteractionHand hand = context.getHand();
		BlockEntity tile = level.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);

		if (!stack.hasTag()) stack.setTag(new CompoundTag());

		if(tile != null && tile instanceof TileEntityLightningTransmitter) {
			TileEntityLightningTransmitter tx = (TileEntityLightningTransmitter)tile;

			boolean hasMark = stack.getTag().getBoolean("hasMark");
			int txX, txY, txZ;

			if(!hasMark) {
				if(!level.isClientSide) {
					hasMark = true;
					txX = tx.getX();
					txY = tx.getY();
					txZ = tx.getZ();
					stack.getTag().putBoolean("hasMark", hasMark);
					stack.getTag().putInt("txX", txX);
					stack.getTag().putInt("txY", txY);
					stack.getTag().putInt("txZ", txZ);
				}
				return InteractionResult.SUCCESS;
			}

		} else if(tile != null && tile instanceof TileEntityLightningReceiver) {
			TileEntityLightningReceiver rx = (TileEntityLightningReceiver)tile;

			boolean hasMark = stack.getTag().getBoolean("hasMark");
			int txX = stack.getTag().getInt("txX");
			int txY = stack.getTag().getInt("txY");
			int txZ = stack.getTag().getInt("txZ");

			if(hasMark) {
				if(!level.isClientSide) {
					rx.txPos = new BlockPos(txX, txY, txZ);
					rx.setChanged();
					stack.getTag().putBoolean("hasMark", hasMark);
				}
				return InteractionResult.SUCCESS;
			}

		}
		return InteractionResult.FAIL;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity player, int par4, boolean par5) {
		if(!level.isClientSide) {
			if(stack.getDamageValue() == 0 && stack.hasTag() && stack.getTag().getBoolean("hasMark")) {
				stack.setDamageValue(1);
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {

		String lore1, lore2;
		boolean addLore2 = true;
		if(stack.getDamageValue() == 1) {
			lore1 = LCText.secSign + "6" + Component.translatable("tooltip.lightningcraft.marker_marked").getString();
			if(stack.hasTag() && stack.getTag().getBoolean("hasMark")) {
				int txX = stack.getTag().getInt("txX");
				int txY = stack.getTag().getInt("txY");
				int txZ = stack.getTag().getInt("txZ");
				lore2 = Component.translatable("tooltip.lightningcraft.marker_pos", txX, txY, txZ).getString();
			} else {
				lore2 = "";
				addLore2 = false;
			}
			list.add(Component.literal(lore1));
		} else {
			lore2 = LCText.secSign + "7" + Component.translatable("tooltip.lightningcraft.marker_unmarked").getString();
		}
		if(addLore2) list.add(Component.literal(lore2));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		if(stack.getDamageValue() == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		if(stack.getDamageValue() == 1) {
			return Rarity.RARE;
		} else {
			return Rarity.UNCOMMON;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerRender() {

	}

}