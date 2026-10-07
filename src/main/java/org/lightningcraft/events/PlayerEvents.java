package org.lightningcraft.events;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.Mth;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.IKineticGear;
import org.lightningcraft.api.IMysticGear;
import org.lightningcraft.api.IPotionEffectProvider;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.blocks.PortalUnderworld;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCDimensions;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.init.LCPotions;
import org.lightningcraft.items.ItemBattery;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.util.InventoryLE;
import org.lightningcraft.util.SkyUtils;

public class PlayerEvents {

	public static final int repairTime = 5 * 20;
	static final float walkBoostSky = 0.35123F;
	static final float walkBoostMystic = 0.55123F;
	static final float stepBoost = 1.01789F;
	static final UUID speedBootsID = UUID.fromString("caac9ed0-a4a7-42c1-af76-0482f06f34d8");

	@SubscribeEvent
	public void onPlayerUpdate(LivingTickEvent e) {
		if(e.getEntity() instanceof Player) {
			Player player = (Player)e.getEntity();

			if (!player.level().isClientSide) {
				ServerPlayer mp = (ServerPlayer)player;

				handleRepair(mp);

				handleServerPotionEffects(mp);

				handleTeleportation(mp);
			}

			onStepBootsWear(player);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onPlayerRenderTick(RenderTickEvent e) {
		Player player = Minecraft.getInstance().player; if(player == null) return;

		handleClientPotionEffects(player);
	}

	private void handleClientPotionEffects(Player player) {

		if(player.level().dimension() == LCDimensions.UNDERWORLD_KEY) {
			if(player.hasEffect(MobEffects.NIGHT_VISION)) {
				player.removeEffect(MobEffects.NIGHT_VISION);
			}
		}
	}

	private void handleServerPotionEffects(Player player) {

		for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack s = player.getInventory().getItem(i);
			if(!s.isEmpty() && s.getItem() instanceof IPotionEffectProvider) {
				IPotionEffectProvider item = (IPotionEffectProvider)s.getItem();
				if(item.canApplyEffect(s, player, i)) {
					for(MobEffectInstance e : item.getEffects(s, player, i)) {
						player.addEffect(e);
					}
				}
			}
		}

		if(player.level().dimension() == LCDimensions.UNDERWORLD_KEY) {
			if(player.hasEffect(MobEffects.NIGHT_VISION)) {
				player.removeEffect(MobEffects.NIGHT_VISION);
			}
		}

		if (player.hasEffect(LCPotions.demonFriend) && player.getEffect(LCPotions.demonFriend).getDuration() == 0) {
			player.removeEffect(LCPotions.demonFriend);
		}
	}

	private void handleTeleportation(ServerPlayer mp) {
		if(PortalUnderworld.portalStatus.checkCooldown(mp) && PortalUnderworld.portalStatus.getPortal(mp)) {
			PortalUnderworld.doTeleport(mp);
			PortalUnderworld.portalStatus.resetCooldown(mp);
		} else {
			PortalUnderworld.portalStatus.setPortal(mp, false);
		}
	}

	private void onStepBootsWear(Player player) {
		boolean isMystic = false;
		AttributeInstance attributeinstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
		AttributeModifier mod = new AttributeModifier(speedBootsID, "skyBootsSpeedModifier", walkBoostSky, AttributeModifier.Operation.MULTIPLY_TOTAL);
		AttributeModifier mystic = new AttributeModifier(speedBootsID, "mysticBootsSpeedModifier", walkBoostMystic, AttributeModifier.Operation.MULTIPLY_TOTAL);
		if(player.getItemBySlot(EquipmentSlot.FEET) != null) {
			if(player.getItemBySlot(EquipmentSlot.FEET).getItem() == LCItems.skyBoots) {

			} else if(player.getItemBySlot(EquipmentSlot.FEET).getItem() == LCItems.mysticBoots) {
				isMystic = true;
				mod = mystic;
			} else {
				removeAttributes(player, attributeinstance, mod);
				mod = null;
			}
			if(mod != null) {
				if(attributeinstance.getModifier(speedBootsID) == null) attributeinstance.addTransientModifier(mod);
				if(!player.isShiftKeyDown()) {
					player.setMaxUpStep(stepBoost * (isMystic ? 2F : 1F));
				} else {
					player.setMaxUpStep(0.5f);
				}
			}
		} else {
			removeAttributes(player, attributeinstance, mod);
		}
	}

	private void removeAttributes(Player player, AttributeInstance instance, AttributeModifier mod) {
		if(instance.getModifier(speedBootsID) != null) instance.removeModifier(mod);

		player.setMaxUpStep(0.5F);
	}

	private void handleRepair(Player player) {
		JointList<ItemStack> inv = new JointList<ItemStack>().join(player.getInventory().items).join(player.getInventory().armor);
		for(ItemStack stack : inv) {
			if(stack != null) {
				if(LCConfig.autoRepair && stack.getItem() instanceof IMysticGear) {
					doAutoRepair(player, stack, 1);
				} else if(stack.getItem() instanceof IKineticGear) {
					doKineticRepair(player, stack, InventoryLE.repairSpeedModifier);
				}
			}
		}
	}

	private boolean doAutoRepair(Player player, ItemStack stack, double speed) {
		if(SkyUtils.canWriteItemNBT(stack, player)) {
			int damage = stack.getDamageValue();

			if(damage > 0) {
				if(player.tickCount % Mth.floor(repairTime / speed) == 0) {
					stack.setDamageValue(damage - 1);
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	private boolean doKineticRepair(Player player, ItemStack stack, double speed) {
		ItemStack battery = InventoryLE.getLESource(player, InventoryLE.energyUsage);
		if(battery != null && doAutoRepair(player, stack, speed)) {
			ItemBattery.addStoredPower(battery, -InventoryLE.energyUsage);
			return true;
		}
		return false;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onTooltipShow(ItemTooltipEvent e) {
		if(e.getItemStack() != null) {
			showAutoRepairTooltip(e.getItemStack(), e.getToolTip());
		}
	}

	private void showAutoRepairTooltip(ItemStack stack, List<net.minecraft.network.chat.Component> list) {
		if(stack.getItem() instanceof IMysticGear) {
			list.add(1, net.minecraft.network.chat.Component.literal(LCText.getAutoRepairLore()));
		}
	}

	@SubscribeEvent
	public void onLivingFall(net.minecraftforge.event.entity.living.LivingFallEvent event) {
		if (event.getEntity() instanceof net.minecraft.world.entity.Mob
				&& event.getEntity().level().dimension().equals(org.lightningcraft.init.LCDimensions.UNDERWORLD_KEY)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onEntityJoinUnderworld(net.minecraftforge.event.entity.EntityJoinLevelEvent event) {
		if (!(event.getLevel() instanceof net.minecraft.server.level.ServerLevel server)
				|| !server.dimension().equals(org.lightningcraft.init.LCDimensions.UNDERWORLD_KEY)
				|| !(event.getEntity() instanceof net.minecraft.world.entity.Mob)) {
			return;
		}
		net.minecraft.world.entity.Entity e = event.getEntity();

		if (e.getY() > 200.0D) {
			event.setCanceled(true);
			return;
		}

		if (server.isClientSide()) {
			return;
		}
		net.minecraft.core.BlockPos below = e.blockPosition().below();
		if (!server.getBlockState(below).isSolidRender(server, below)) {
			event.setCanceled(true);
		}
	}
}
