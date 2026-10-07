package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCSoundEvents;
import org.lightningcraft.ref.Log;
import org.lightningcraft.ref.RefStrings;

public class EntityUnderworldGhast extends Ghast {

	public EntityUnderworldGhast(EntityType<EntityUnderworldGhast> type, Level worldIn ) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		Goal remove = null;
		for(WrappedGoal task : this.goalSelector.getAvailableGoals()) {

			String simpleName = task.getGoal().getClass().getSimpleName();
			if(simpleName.equals("GhastShootFireballGoal") || simpleName.equals("AIFireballAttack")) {
				remove = task.getGoal();
				break;
			}
		}
		if(remove != null) {
			this.goalSelector.removeGoal(remove);
			this.goalSelector.addGoal(7, new AIUnderworldFireballAttack(this));
		} else {
			Log.logger.error(this.getName() + " failed to override Ghast fireball AI.");
		}
	}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return net.minecraft.world.entity.monster.Ghast.createAttributes()
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0D)
				.add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 150.0D)
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 1.4D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		if(!LCConfig.useVanillaGhastSounds) {
			return LCSoundEvents.underworldGhastMoan;
		} else {
			return super.getAmbientSound();
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource src) {
		if(!LCConfig.useVanillaGhastSounds) {
			return LCSoundEvents.underworldGhastHurt;
		} else {
			return super.getHurtSound(src);
		}
	}

	@Override
	protected SoundEvent getDeathSound() {
		if(!LCConfig.useVanillaGhastSounds) {
			return LCSoundEvents.underworldGhastDeath;
		} else {
			return super.getDeathSound();
		}
	}

	@Override
	protected float getSoundVolume() {
		return super.getSoundVolume() * 0.75F;
	}

	@Override
	public float getVoicePitch() {
		if(!LCConfig.useVanillaGhastSounds) {
			return super.getVoicePitch() * 1.125F;
		} else {
			return super.getVoicePitch() * 1.25F;
		}
	}

	public static class AIUnderworldFireballAttack extends Goal {

		private EntityUnderworldGhast parentEntity;
		public int attackTimer;

		public AIUnderworldFireballAttack(EntityUnderworldGhast ghast) {
			this.parentEntity = ghast;
		}

		@Override
		public boolean canUse() {
			return this.parentEntity.getTarget() != null;
		}

		@Override
		public void start() {
			this.attackTimer = 0;
		}

		@Override
		public void stop() {
			this.parentEntity.setAggressive(false);
		}

		@Override
		public void tick() {
			LivingEntity entitylivingbase = this.parentEntity.getTarget();
			double d0 = 64.0D;

			if (entitylivingbase.distanceToSqr(this.parentEntity) < d0 * d0 && this.parentEntity.hasLineOfSight(entitylivingbase)) {
				Level level = this.parentEntity.level();
				++this.attackTimer;

				if (this.attackTimer == 10) {
					if(!LCConfig.useVanillaGhastSounds) {
						level.playSound(null, this.parentEntity.blockPosition(), LCSoundEvents.underworldGhastWarn, SoundSource.HOSTILE,
								10.0F, (level.random.nextFloat() - this.parentEntity.random.nextFloat()) * 0.2F + 1.0F);
					} else {
						level.levelEvent((Player)null, 1015, this.parentEntity.blockPosition(), 0);
					}
				}

				if (this.attackTimer == 20) {
					double d1 = 4.0D;
					Vec3 vec3d = this.parentEntity.getLookAngle();
					double d2 = entitylivingbase.getX() - (this.parentEntity.getX() + vec3d.x * d1);
					double d3 = entitylivingbase.getBoundingBox().minY + entitylivingbase.getBbHeight() / 2.0F -
							(0.5D + this.parentEntity.getY() + this.parentEntity.getBbHeight() / 2.0F);
					double d4 = entitylivingbase.getZ() - (this.parentEntity.getZ() + vec3d.z * d1);
					level.levelEvent((Player)null, 1016, this.parentEntity.blockPosition(), 0);
					LargeFireball entitylargefireball = new LargeFireball(net.minecraft.world.entity.EntityType.FIREBALL, level);

					entitylargefireball.setPos(this.parentEntity.getX() + vec3d.x * d1, this.parentEntity.getY() + this.parentEntity.getBbHeight() / 2.0F + 0.5D, this.parentEntity.getZ() + vec3d.z * d1);
					level.addFreshEntity(entitylargefireball);
					this.attackTimer = -30;
				}
			} else if (this.attackTimer > 0) {
				--this.attackTimer;
			}

			this.parentEntity.setAggressive(this.attackTimer > 10);
		}
	}

}