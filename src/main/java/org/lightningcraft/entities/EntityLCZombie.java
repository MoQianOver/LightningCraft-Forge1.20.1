package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.lightningcraft.ref.RefMisc;
import org.lightningcraft.util.WorldUtils;

public class EntityLCZombie extends Zombie {

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return net.minecraft.world.entity.monster.Zombie.createAttributes();
	}

	private LivingEntity target;
	private LivingEntity owner;
	private UUID targetID;
	private UUID ownerID;
	private boolean isTargetPlayer;
	private boolean isOwnerPlayer;

	private static final int stateREADNBT = 1;
	private static final int stateTRIED = 2;
	private static final int stateDONE = 3;
	private int loadstate = 0;
	private int targetTries = 0;
	private int ownerTries = 0;
	private int tries = 0;
	private static final int tryLimit = 32;

	public EntityLCZombie(EntityType<EntityLCZombie> type, Level level ) {
		super(type, level);
	}

	public EntityLCZombie(EntityType<EntityLCZombie> type, Level level, LivingEntity target, LivingEntity owner) {
		super(type, level);
		this.target = target; this.targetID = target.getUUID();
		this.owner = owner; this.ownerID = owner.getUUID();
		this.isTargetPlayer = (target instanceof Player);
		this.isOwnerPlayer = (owner instanceof Player);
		this.removeTargetTasks();
		this.setTargetTask();
		this.loadstate = stateDONE;
	}

	private void removeTargetTasks() {

		Set<WrappedGoal> entries = this.targetSelector.getAvailableGoals();
		Iterator<WrappedGoal> iter = entries.iterator();
		while(iter.hasNext()){
			WrappedGoal e = iter.next();
			iter.remove();
		}
	}

	private void setTargetTask() {

		if(target != null) {
			this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		}
	}

	private void getData() {

		if(this.loadstate == stateREADNBT) this.removeTargetTasks();

		if(this.targetID == null && this.ownerID == null) {
			this.loadstate = stateDONE;
			return;
		}

		if(this.target == null)	this.target = this.targetID != null ? WorldUtils.getEntityLivingBaseFromUUID(this.level(), targetID, isTargetPlayer) : null;
		if(this.owner == null)	this.owner = this.ownerID != null ? WorldUtils.getEntityLivingBaseFromUUID(this.level(), ownerID, isOwnerPlayer): null;

		if(this.target == null && this.owner == null) {
			this.loadstate = stateTRIED;
			this.tries++;
			if(tries >= tryLimit) this.loadstate = stateDONE;
		} else {

			if(isTargetPlayer && this.target == null && targetTries < tryLimit) {
				targetTries++;
				this.loadstate = stateTRIED;
				return;
			}
			if(isOwnerPlayer && this.owner == null && ownerTries < tryLimit) {
				ownerTries++;
				this.loadstate = stateTRIED;
				return;
			}

			this.loadstate = stateDONE;
			this.setTargetTask();
		}
	}

	@Override
	public void tick() {
		super.tick();

		if(!this.level().isClientSide) {

			if((loadstate == stateREADNBT || loadstate == stateTRIED) && target == null || owner == null) this.getData();

			if(target != null && isTargetPlayer && ((Player)target).getAbilities().instabuild) {
				target = null;
			}

			if(target != null && target.isRemoved() == false) {
				this.setTarget(target);
				this.setLastHurtByMob(target);
			} else {

				if(loadstate == stateDONE) {

					this.die();
				}
			}
		}
	}

	@Override
	public boolean doHurtTarget(Entity victim) {
		if(!victim.level().isClientSide()) {
			if(victim instanceof LivingEntity) {

				Class t = LivingEntity.class;
				String recentlyHit = RefMisc.DEV ? "recentlyHit" : "field_70718_bc";
				try {
					Field playerHit = t.getDeclaredField(recentlyHit);
					playerHit.setAccessible(true);
					playerHit.setInt(victim, 60);
				} catch (NoSuchFieldException e) {
					System.out.println("Could not set recentlyHit on attacked entity: field does not exist");
				} catch (IllegalAccessException e) {
					System.out.println("Could not set recentlyHit on attacked entity: field is not accessible");
				}
			}
	    }
		return super.doHurtTarget(victim);
	}

	public LivingEntity getOwner() {
		return this.owner;
	}

	private void die() {
		if(this.getHealth() > 0) this.playSound(SoundEvents.ZOMBIE_DEATH, 1.0F, 1.0F);
		this.setHealth(0);
	}

	@Override
    public int getExperienceReward() {
        return 0;
    }

	@Override
    protected boolean shouldDropLoot() {
        return false;
    }

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		String targetStr = tag.getString("target");
		String ownerStr = tag.getString("owner");
		this.targetID = UUID.fromString(targetStr);
		this.ownerID = UUID.fromString(ownerStr);

		this.isTargetPlayer = tag.getBoolean("isTargetPlayer");
		this.isOwnerPlayer = tag.getBoolean("isOwnerPlayer");

		this.target = null;
		this.owner = null;

		this.loadstate = stateREADNBT;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);

		if(this.target != null) {
			tag.putString("target", this.targetID.toString());
			tag.putBoolean("isTargetPlayer", this.isTargetPlayer);
		}
		if(this.owner != null) {
			tag.putString("owner", this.ownerID.toString());
			tag.putBoolean("isOwnerPlayer", this.isOwnerPlayer);
		}
	}
}
