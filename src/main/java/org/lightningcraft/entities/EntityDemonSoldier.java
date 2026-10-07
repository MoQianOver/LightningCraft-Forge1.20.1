package org.lightningcraft.entities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Level;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCDimensions;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.init.LCPotions;
import org.lightningcraft.init.LCSoundEvents;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.util.SkyUtils;

public class EntityDemonSoldier extends Monster {

	protected boolean isTough;

	public EntityDemonSoldier(EntityType<EntityDemonSoldier> type, Level level, boolean isTough) {
		this(type, level);
		this.isTough = isTough;
	}

	public EntityDemonSoldier(EntityType<EntityDemonSoldier> type, Level level ) {
		super(type, level);
		applyDimensionAttributes();

		this.isTough = false;

		if(this.random.nextDouble() <= 0.01) {
			this.isTough = true;
		}

		this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

	}

	@Override
	public boolean isNoAi() {
	   return false;
	}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0D)
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.2D)
				.add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 5.0D);
	}

	private void applyDimensionAttributes() {
		if (this.level().dimension() == LCDimensions.UNDERWORLD_KEY || LCConfig.demonSoldiersFullPower) {
			this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(60.0D);
			this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue(0.4D);
			this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).setBaseValue(7.0D);
		} else {
			this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(40.0D);
			this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
			this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if(!this.level().isClientSide) {

			if(this.getTarget() == null) {
				Player p = (Player)this.findPlayerToAttack();

				if(p != null) {
					try {
						this.setTarget(p);
						p.sendSystemMessage(Component.literal(
								"[Demon Soldier] " + LCText.secSign + "4" + LCText.secSign + "oYou will pay for your insolence." + LCText.secSign + "r"));
					} catch(Exception e) {}
				}
			} else {

				if(this.getTarget() instanceof Player && ((Player)this.getTarget()).getAbilities().invulnerable) {
					this.setTarget(null);
				}
			}

			if(this.tickCount % 40 == 0 && this.getEffect(LCPotions.demonFriend) != null) {
				this.setTarget(null);
				this.hurt(this.level().damageSources().magic(), 0.5F);
			}

		}
	}

    protected Entity findPlayerToAttack() {
    	ServerPlayer p = (ServerPlayer)this.level().getNearestPlayer(this, 16.0D);
        return p != null && !LCConfig.demonSoldiersAlwaysNeutral && !p.getAbilities().invulnerable && this.hasLineOfSight(p)
        		&& SkyUtils.isPlayerInsolent(p) ? p : null;
    }

	@Override
    public boolean hurt(DamageSource source, float damage) {
		boolean flag = super.hurt(source, damage);
		if(!this.level().isClientSide) {
			Entity p = source.getDirectEntity();

			if(this.getHealth() > 0 && p != null && p instanceof ServerPlayer &&
					this.getTarget() != p && !((ServerPlayer)p).getAbilities().invulnerable) {
				try {
					((ServerPlayer)p).sendSystemMessage(Component.literal(
							"[Demon Soldier] " + LCText.secSign + "4" + LCText.secSign + "oWhat a fool." + LCText.secSign + "r"));
					this.setTarget((ServerPlayer)p);
				} catch(Exception e) {}
			}

			if(this.getHealth() <= 0 && p instanceof Player) {

			}
		}
    	return flag;
    }

	@Override
	public SpawnGroupData finalizeSpawn(net.minecraft.world.level.ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, net.minecraft.world.entity.MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData livingdata, @javax.annotation.Nullable net.minecraft.nbt.CompoundTag spawnTag) {
		super.finalizeSpawn(levelAccessor, difficulty, spawnType, livingdata, spawnTag);
		this.armDemon();
		return livingdata;
	}

	protected void armDemon() {

		JointList<ItemStack> swords = new JointList().join(
			new ItemStack(LCItems.soulSword),
			new ItemStack(LCItems.zombieSword),
			new ItemStack(LCItems.featherSword),
			new ItemStack(LCItems.enderSword),
			new ItemStack(LCItems.blazeSword),
			new ItemStack(LCItems.iceSword)
		);

		int sel = this.random.nextInt(swords.size());
		ItemStack sword = isTough ? new ItemStack(LCItems.skySword) : swords.get(sel);
		sword.enchant(Enchantments.FIRE_ASPECT, 2);
		this.setItemInHand(InteractionHand.MAIN_HAND, sword);

		if(isTough) {
			this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
			this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
			this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
			this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
		}
	}

    @Override
	public int getArmorValue() {
        int i = super.getArmorValue() + 8;
        if (i > 23) {
            i = 23;
        }
        return i;
    }

    @Override
	protected SoundEvent getAmbientSound() {
        return LCSoundEvents.demonSoldierIdle;
    }

    @Override
	protected SoundEvent getHurtSound(DamageSource src) {
    	return LCSoundEvents.demonSoldierHurt;
    }

    @Override
	protected SoundEvent getDeathSound() {
    	return LCSoundEvents.demonSoldierDeath;
    }

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.isTough = tag.getBoolean("isTough");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("isTough", this.isTough);
	}

}