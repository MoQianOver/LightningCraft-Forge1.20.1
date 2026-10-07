package org.lightningcraft.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class LivingSoulStealEvent extends LivingHurtEvent {

	public static DamageSource soulSteal(LivingEntity entity) {
		return entity.damageSources().magic();
	}

	public LivingSoulStealEvent(LivingEntity entity, float amount) {
		super(entity, soulSteal(entity), amount);
	}

}
