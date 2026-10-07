package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.init.LCParticles;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.util.WorldUtils;

public class EntityLCElectricAttack extends Fireball {

	public double startX, startY, startZ;

	public EntityLCElectricAttack(EntityType<EntityLCElectricAttack> type, Level level )
	{
		super(type, level);
		this.startX = this.getX();
		this.startY = this.getY();
		this.startZ = this.getZ();
	}

	public EntityLCElectricAttack(EntityType<EntityLCElectricAttack> type, Level level, double x, double y, double z, double vx, double vy, double vz)
	{
		super(type, level);
		this.setPos(x, y, z);

		this.xPower = vx;
		this.yPower = vy;
		this.zPower = vz;
		this.startX = x;
		this.startY = y;
		this.startZ = z;
	}

	public EntityLCElectricAttack(EntityType<EntityLCElectricAttack> type, Level level, double x, double y, double z, Entity target)
	{
		this(org.lightningcraft.init.LCEntities.LC_ELECTRIC_ATTACK.get(), level, x, y, z, 0, 0, 0);

		Vec3 result = target != null ? WorldUtils.estimateEntityPosition(target, x, y, z, 0.75D) : null;

		double vx, vy, vz;
		if(target != null) {
			vx = result.x - x;
			vy = result.y + 1 - y;
			vz = result.z - z;
		} else {
			vx = 1.0;
			vy = 1.0;
			vz = 1.0;
		}

        double d6 = Mth.sqrt((float)(vx * vx + vy * vy + vz * vz));
        this.xPower = vx / d6 * 0.125D;
        this.yPower = vy / d6 * 0.125D;
        this.zPower = vz / d6 * 0.125D;

	}

	@Override
	public void tick() {
		super.tick();
		this.clearFire();

		if(this.level().isClientSide) {
			double d = 0.2125;
			double xoff = 0;
			double yoff = 0;
			double zoff = 0;
			for(int i = 0; i < 12; i++) {
				xoff = this.random.nextGaussian();
				yoff = this.random.nextGaussian();
				zoff = this.random.nextGaussian();
				LCParticles.spawnParticle("electricAttack", getX() + (xoff - 0.5) * d, getY() + (yoff - 0.5) * d, getZ() + (zoff - 0.5) * d, 0, 0, 0);
			}
		} else {
			if(this.xPower == 0 && this.yPower == 0 && this.zPower == 0) {
				this.discard();
			}
		}
	}

	@Override
	protected void onHit(HitResult mop) {

		 if (!this.level().isClientSide)
	        {

	            if (((net.minecraft.world.phys.EntityHitResult) mop).getEntity() != null)
	            {

	                if (((net.minecraft.world.phys.EntityHitResult) mop).getEntity().hurt(this.level().damageSources().magic(), 21.0F))
	                {
	                	if(!((net.minecraft.world.phys.EntityHitResult) mop).getEntity().fireImmune()) ((net.minecraft.world.phys.EntityHitResult) mop).getEntity().setSecondsOnFire(2);
	                	this.level().playSound(null, getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 6.0F, 2.0F);
	                }
	            }
	            else
	            {
	                BlockPos pos = ((net.minecraft.world.phys.BlockHitResult) mop).getBlockPos();
	                int i, j, k = i = j = 0;

				 	if(this.level().getBlockState(pos).getBlock() == LCBlocks.lightningCannon) {
				 		return;
				 	}

	                switch (((net.minecraft.world.phys.BlockHitResult) mop).getDirection().get3DDataValue())
	                {
	                    case 0:
	                        --j;
	                        break;
	                    case 1:
	                        ++j;
	                        break;
	                    case 2:
	                        --k;
	                        break;
	                    case 3:
	                        ++k;
	                        break;
	                    case 4:
	                        --i;
	                        break;
	                    case 5:
	                        ++i;
	                }

	                pos = pos.offset(i, j, k);

	                if (this.level().isEmptyBlock(pos))
	                {
	                    this.level().setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
	                }
	            }

	            this.level().playSound(null, getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 6.0F, 2.0F);
	            this.discard();
	        }
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return true;
	}

}
