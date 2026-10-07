package org.lightningcraft.events;

import java.util.Random;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lightningcraft.entities.EntityLCZombie;
import org.lightningcraft.init.LCEnchantments;
import org.lightningcraft.items.ItemBlazeSword;
import org.lightningcraft.items.ItemChargedSword;
import org.lightningcraft.items.ItemEnderSword;
import org.lightningcraft.items.ItemFeatherSword;
import org.lightningcraft.items.ItemHammer;
import org.lightningcraft.items.ItemIceSword;
import org.lightningcraft.items.ItemMysticArmor;
import org.lightningcraft.items.ItemSoulSword;
import org.lightningcraft.items.ItemZombieSword;
import org.lightningcraft.items.base.ItemSpecialSword;
import org.lightningcraft.util.Effect;
import org.lightningcraft.util.IntList;
import org.lightningcraft.util.WorldUtils;

public class CombatEvents {

	private static final Random random = new Random();

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		if(event.getEntity() instanceof LivingEntity && !event.getEntity().level().isClientSide) {
			handleLightningSwordStrike(event);
			handleSpecialStrike(event);
			handleElecAuraHurt(event);
		}
	}

	public void handleLightningSwordStrike(LivingHurtEvent event) {

		LivingEntity target = (LivingEntity)event.getEntity();
		if(target == null || target.isInvulnerableTo(event.getSource())) return;
		LivingEntity user = null;
		if(event.getSource().getDirectEntity() instanceof LivingEntity) user = (LivingEntity)event.getSource().getDirectEntity();
		if(user == null) return;
		ItemStack weapon = user.getItemInHand(InteractionHand.MAIN_HAND); if(weapon.isEmpty()) return;

		int j = EnchantmentHelper.getItemEnchantmentLevel(LCEnchantments.handOfThor, weapon);
		int m = LCEnchantments.handOfThor.getMaxLevel();
		Item w = weapon.getItem();

		double init = w instanceof ItemChargedSword ? (w instanceof ItemHammer ? 0.5 : 0.15) : 0;
		double var = w instanceof ItemChargedSword ? (w instanceof ItemHammer ? 0.5 : 0.3) : 0.1;
		if(!user.isShiftKeyDown() && random.nextDouble() < (init + var * j / m)) {
			Effect.lightning(target, false);
			weapon.hurtAndBreak(2, user, e -> {});
		}
	}

	public void handleElecAuraHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(event.getSource().getDirectEntity() instanceof LivingEntity) {
				LivingEntity attacker = (LivingEntity) event.getSource().getDirectEntity();

				if(!attacker.isInvulnerableTo(event.getSource())) {
					IntList ench = new IntList();
					ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
					ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
					ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					int lv = 0, a = 0;
					int damageHelm = 0, damageChest = 0, damageLegs = 0, damageBoots = 0;
					if(helm != null) {
						damageHelm = EnchantmentHelper.getItemEnchantmentLevel(LCEnchantments.elecAura, helm);
						ench.add(damageHelm);
						if(helm.getItem() instanceof ItemMysticArmor) ++a;
					}
					if(chest != null) {
						damageChest = EnchantmentHelper.getItemEnchantmentLevel(LCEnchantments.elecAura, chest);
						ench.add(damageChest);
						if(chest.getItem() instanceof ItemMysticArmor) ++a;
					}
					if(legs != null) {
						damageLegs = EnchantmentHelper.getItemEnchantmentLevel(LCEnchantments.elecAura, legs);
						ench.add(damageLegs);
						if(legs.getItem() instanceof ItemMysticArmor) ++a;
					}
					if(boots != null) {
						damageBoots = EnchantmentHelper.getItemEnchantmentLevel(LCEnchantments.elecAura, boots);
						ench.add(damageBoots);
						if(boots.getItem() instanceof ItemMysticArmor) ++a;
					}

					for(int i : ench) {
						lv += i;
					}

					if(lv > 0) {

						if(random.nextDouble() <= lv * 2/100D) {
							Effect.lightning(attacker, false);
							if(damageHelm > 0) helm.hurtAndBreak(4 * damageHelm, player, e -> {});
							if(damageChest > 0) chest.hurtAndBreak(4 * damageChest, player, e -> {});
							if(damageLegs > 0) legs.hurtAndBreak(4 * damageLegs, player, e -> {});
							if(damageBoots > 0) boots.hurtAndBreak(4 * damageBoots, player, e -> {});

						}
					}
				}
			}
		}
	}

	public void handleSpecialStrike(LivingHurtEvent event) {

		LivingEntity target = (LivingEntity)event.getEntity();
		Level level = target.level();
		Entity src = event.getSource().getDirectEntity();
		float nextHealth = target.getHealth() - event.getAmount();

		if(event.getAmount() <= 0 || target.isInvulnerableTo(event.getSource())) return;

		if(src != null && src instanceof LivingEntity) {
			LivingEntity user = (LivingEntity) src;
			ItemStack sword = user.getItemInHand(InteractionHand.MAIN_HAND);

			if(sword != null && sword.getItem() instanceof ItemSpecialSword) {
				Item w = sword.getItem();
				String effect = null;

				if(w instanceof ItemSoulSword) {
					effect = "soulSteal";

					float health = target.getHealth();
					float steal = health * 0.15F;
					LivingSoulStealEvent soulEvent = new LivingSoulStealEvent(target, steal);
					if(!MinecraftForge.EVENT_BUS.post(soulEvent)) {
						steal = soulEvent.getAmount();
						nextHealth -= steal;
						target.setHealth(health - steal);
						user.heal(steal * 0.2515F * (random.nextFloat() + 1F));
					}

				} else if(w instanceof ItemZombieSword && !(target instanceof EntityLCZombie && ((EntityLCZombie)target).getOwner() == user)) {
					effect = "zombieSummon";

					Zombie zombie = new EntityLCZombie(org.lightningcraft.init.LCEntities.HELPER_ZOMBIE.get(), level, target, user);
					zombie.setPos(target.getX() + random.nextFloat() - 0.5F, target.getY(), target.getZ() + random.nextFloat() - 0.5F);
					level.addFreshEntity(zombie);
					zombie.playAmbientSound();

				} else if(w instanceof ItemFeatherSword) {
					effect = "featherAttack";

					float knockbackModifier = 10F;
					target.push(-Mth.sin(user.getYRot() *
							(float)Math.PI / 180.0F) * knockbackModifier  * 0.5F,
							0.1D, Mth.cos(user.getYRot() *
									(float)Math.PI / 180.0F) * knockbackModifier  * 0.5F);
					target.setDeltaMovement(target.getDeltaMovement().x * 0.6D, target.getDeltaMovement().y, target.getDeltaMovement().z);
					target.setDeltaMovement(target.getDeltaMovement().x, target.getDeltaMovement().y, target.getDeltaMovement().z * 0.6D);

				} else if(w instanceof ItemEnderSword) {
					effect = "enderAttack";

					float distance = 2.5F + random.nextFloat();
					double posX, posZ;
					Integer posY;
					posX = target.getX() + (Mth.sin(target.getYHeadRot() * (float)Math.PI / 180.0F) * distance);
					posZ = target.getZ() - (Mth.cos(target.getYHeadRot() * (float)Math.PI / 180.0F) * distance);
					posY = WorldUtils.getOpenSurface(level, (int)posX, (int)posZ, (int)target.getY(), 6);

					if(posY != null) {

						Effect.ender(level, user.getX(), user.getY(), user.getZ(), posX, posY, posZ, true);

						user.setYRot(target.getYHeadRot());
						user.setYHeadRot(target.getYHeadRot());
						user.teleportTo(posX, posY, posZ);
					}

				} else if(w instanceof ItemBlazeSword) {
					effect = "blazeAttack";

					target.setSecondsOnFire(3600 / 20);

				} else if(w instanceof ItemIceSword) {
					effect = "iceAttack";

					target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5));
					target.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 128));
				}

				if(effect != null) Effect.specialSword(effect, level, target.getX(), target.getY(), target.getZ());

			}
		}
	}

}
