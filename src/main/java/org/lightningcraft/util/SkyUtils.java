package org.lightningcraft.util;

import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.common.collect.ImmutableMultimap;

import java.util.Random;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.google.common.collect.Multimap;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import org.lightningcraft.api.IMysticGear;
import org.lightningcraft.init.LCDimensions;
import org.lightningcraft.init.LCPotions;

public class SkyUtils {

	public static final int skyRepairTime = 5 * 20;
	static final Random random = new Random();

	public static boolean canWriteItemNBT(@Nonnull ItemStack stack, Player player) {
		return !(player.swinging && (player.getItemInHand(InteractionHand.MAIN_HAND) == stack || player.getItemInHand(InteractionHand.OFF_HAND) == stack));
	}

	public static void damageStack(int damage, @Nonnull ItemStack stack, LivingEntity user, EquipmentSlot slot, boolean zeroMax) {
		int cdamage = stack.getDamageValue();
		stack.hurtAndBreak(damage, user, e -> {});
		if(!stack.isEmpty()) {
			boolean comp2 = zeroMax ? cdamage + damage >= stack.getMaxDamage() : cdamage + damage > stack.getMaxDamage();
			if(comp2) user.setItemSlot(slot, ItemStack.EMPTY);
		}
	}

	public static void damageStack(int damage, @Nonnull ItemStack stack, LivingEntity user, EquipmentSlot slot) {
		damageStack(damage, stack, user, slot, false);
	}

	public static final double SWORD_ATTACK_SPEED = -2.4;

	public static Multimap<Attribute, AttributeModifier> withToolSpeedModifier(Item item,
			net.minecraft.world.entity.EquipmentSlot slot,
			Multimap<Attribute, AttributeModifier> original, UUID speedmod, double currentSpeed) {

		if (slot != net.minecraft.world.entity.EquipmentSlot.MAINHAND) {
			return original;
		}

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		original.forEach((attribute, modifier) -> {
			if (attribute != Attributes.ATTACK_SPEED) builder.put(attribute, modifier);
		});
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(speedmod, "Tool modifier",
				currentSpeed + (item instanceof IMysticGear ? 0.6 : 0.3), AttributeModifier.Operation.ADDITION));
		return builder.build();
	}

	public static boolean isPlayerInsolent(ServerPlayer player) {
		return ( player.level().dimension() == LCDimensions.UNDERWORLD_KEY)
				&& player.getEffect(LCPotions.demonFriend) == null;
	}

}
