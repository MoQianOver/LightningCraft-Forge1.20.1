package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.ref.RefStrings;

public class EntityUnderworldSlime extends MagmaCube {

    public EntityUnderworldSlime(EntityType<EntityUnderworldSlime> type, Level level ) {
        super(type, level);
    }

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.35D);
	}

    @Override
	public int getArmorValue() {
        return this.getSize() * 3;
    }

    @Override
	protected net.minecraft.core.particles.ParticleOptions getParticleType() {
        return ParticleTypes.ITEM_SNOWBALL;
    }

    @Override
	protected int getJumpDelay() {
        return super.getJumpDelay() / 2;
    }

    @Override
	protected void jumpFromGround() {
        this.setDeltaMovement(this.getDeltaMovement().x, 0.84F + this.getSize() * 0.1F, this.getDeltaMovement().z);

        ForgeHooks.onLivingJump(this);
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier,
            net.minecraft.world.damagesource.DamageSource source) {

        return false;
    }

    @Override
	public float getAttackDamage() {
        return (int) super.getAttackDamage() + 4;
    }
}
