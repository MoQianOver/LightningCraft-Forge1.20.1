package org.lightningcraft.util;

import net.minecraft.network.chat.Component;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.items.ItemBattery;
import org.lightningcraft.ref.LCText;

public class InventoryLE {

	public static final double repairSpeedModifier = 2.25;
	public static final double breakUsage = 1/5D;
	public static final double energyUsage = 1/50D;
	public static final double strikeUsage = 1/5D;

	public static class LECharge {
		private double power;

		public LECharge(double charge) {
			power = charge;
		}

		public LECharge() {
			power = 0;
		}

		public double getCharge() {
			return power;
		}

		public void setCharge(double charge) {
			power = charge;
		}

	}

	@OnlyIn(Dist.CLIENT)
	public static boolean appendHoverText(@Nonnull ItemStack stack, Level level, List<Component> list, TooltipFlag flag, LECharge charge) {
		list.add(Component.literal(LCText.getInventoryLEUserLore()));

		Player player = Minecraft.getInstance().player;
		boolean charged = hasLESource(player);
		charge.setCharge(getAvailablePower(player));

		String lpLore;
		if (!charged) {
			lpLore = LCText.secSign + "c" + Component.translatable("tooltip.lightningcraft.no_le_source").getString();
		} else if (charge.getCharge() > 0) {
			lpLore = LCText.secSign + "b" + Component.translatable("tooltip.lightningcraft.le_available",
					LCText.df.format(charge.getCharge())).getString();
		} else {
			lpLore = LCText.secSign + "c" + Component.translatable("tooltip.lightningcraft.no_le_charge").getString();
		}
		list.add(Component.literal(lpLore));

		return charged;
	}

	public static double getAvailablePower(Player user) {
		if(hasLESource(user)) {
			int invPos = 0;
			double power = 0;
			IntList avoid = new IntList();
			while((invPos = LCMisc.posInInventory(user, LCItems.battery, avoid)) >= 0) {

				if(invPos >= 0) {
					ItemStack battery = user.getInventory().items.get(invPos);
					power = Math.max(power, ItemBattery.getStoredPower(battery));
					avoid.add(invPos);
				}
			}
			return power;
		}
		return 0;
	}

	public static boolean hasLESource(Player user) {
		return user != null && LCMisc.posInInventory(user, LCItems.battery) >= 0;
	}

	public static @Nonnull ItemStack getLESource(Player user, double lpNeeded) {
		if(hasLESource(user)) {
			int invPos = 0;
			IntList avoid = new IntList();
			while((invPos = LCMisc.posInInventory(user, LCItems.battery, avoid)) >= 0) {

				if(invPos >= 0) {
					ItemStack battery = user.getInventory().items.get(invPos);

					if(ItemBattery.getStoredPower(battery) >= lpNeeded) {
						return battery;
					} else {
						avoid.add(invPos);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static void hitEntity(@Nonnull ItemStack stack, LivingEntity entity, LivingEntity user, double normalDamage, boolean maxDamage) {
		if(user instanceof Player) {
			Player player = (Player)user;
			DamageSource source = player.damageSources().playerAttack(player);
			float damage = 0;
			double charge = getAvailablePower(player);
			boolean sneaking = player.isShiftKeyDown() && maxDamage;

			if(!sneaking) {
				damage = (float)Math.min(charge, normalDamage);

			} else {
				damage = (float)Math.min(charge, 50);
			}

			if(damage > 0) {
				double cost = damage * strikeUsage;
				ItemStack battery = getLESource(player, cost);
				ItemBattery.addStoredPower(battery, -cost);
			}

			entity.hurt(source, damage);
		}
	}

	public static float getEfficiency(@Nonnull ItemStack tool, float baseEff) {
		float newEff;
		double charge;
		if(tool.hasTag()) {
			charge = tool.getTag().getDouble("availablePower");
		} else {
			charge = 0;
		}
		double multiplier = (LCItems.mysticMat.getSpeed() - 2) / baseEff;
		double take = Math.min(charge / breakUsage, multiplier);

			if(charge > 0) {
				newEff = baseEff * (float)take;
			} else {
				newEff = baseEff;
			}

		return newEff;
	}

	public static void updateToolPower(@Nonnull ItemStack tool, Player player) {
		if(!tool.hasTag()) tool.setTag(new CompoundTag());
		if(SkyUtils.canWriteItemNBT(tool, player) && tool.getTag().getDouble("availablePower") != getAvailablePower(player)) {
			tool.getTag().putDouble("availablePower", getAvailablePower(player));
		}
	}

	public static void onBlockBreak(@Nonnull ItemStack tool, Player player, float baseEff) {
		double charge = getAvailablePower(player);
		double multiplier = (LCItems.mysticMat.getSpeed() - 2) / baseEff;
		double take = Math.min(charge, multiplier) * energyUsage;
		ItemStack battery = getLESource(player, take);
		ItemBattery.addStoredPower(battery, -take);
		tool.getTag().putDouble("availablePower", getAvailablePower(player));
	}

}
