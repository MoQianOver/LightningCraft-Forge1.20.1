package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.capabilities.ILightningUpgradable;
import org.lightningcraft.blocks.PortalUnderworld;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCCapabilities;
import org.lightningcraft.items.base.ItemMeta;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.ref.Material;
import org.lightningcraft.util.Effect;

public class ItemMaterial extends ItemMeta {

	public ItemMaterial() {
		super(Material.count, false);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		switch(stack.getDamageValue()) {
		case Material.DEMON_BLOOD:
			list.add(Component.literal(LCText.getDemonBloodLore()));
			break;
		case Material.UNDER_CHARGE:
			list.add(Component.literal(LCText.getUnderworldChargeLore()));
			break;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Rarity getRarity(ItemStack stack) {
		int type = stack.getDamageValue();
		if(type == Material.ICHOR || type == Material.DIVINE_DUST) {
			return Rarity.EPIC;
		} else if(type >= Material.DEMON_BLOOD) {
			return Rarity.RARE;
		} else {
			return Rarity.COMMON;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isFoil(ItemStack stack) {
		int i = stack.getDamageValue();
		return i == Material.NETHER_NUGGET || i == Material.ENSORCELLED
				|| i == Material.ICHOR || i == Material.DIVINE_DUST;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();

		switch(stack.getDamageValue()) {

		case Material.UPGRADE: {

			BlockEntity tile = level.getBlockEntity(pos);
			if (!LCConfig.upgradeEnabled || tile == null) {
				return InteractionResult.FAIL;
			}

			ILightningUpgradable cap = tile.getCapability(LCCapabilities.LIGHTNING_UPGRADABLE).orElse(null);
			if (cap == null || cap.isUpgraded()) {
				return InteractionResult.FAIL;
			}
			InteractionResult r = cap.onLightningUpgrade(stack, player, level, pos,
					context.getHand(), context.getClickedFace(),
					(float) context.getClickLocation().x, (float) context.getClickLocation().y,
					(float) context.getClickLocation().z);
			if (r == InteractionResult.SUCCESS && !level.isClientSide) {
				stack.setCount(stack.getCount() - 1);
				tile.setChanged();

			}
			return r;
		}

		case Material.UNDER_CHARGE:
			if (!level.isClientSide) {
				if (PortalUnderworld.ignitePortal(level, pos.above())) {
					if (!player.getAbilities().instabuild) stack.setCount(stack.getCount() - 1);
					Effect.lightning(level, pos.getX(), pos.getY() + 1, pos.getZ());
					if (stack.getCount() <= 0) stack = ItemStack.EMPTY;
				}
			}
			return InteractionResult.SUCCESS;

		default:
			return super.useOn(context);
		}
	}

}
