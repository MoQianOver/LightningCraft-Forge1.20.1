package org.lightningcraft.util;
import org.lightningcraft.init.LCEntities;

import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.lightningcraft.entities.EntityLCLightningBolt;
import org.lightningcraft.init.LCNetwork;
import org.lightningcraft.init.LCParticles;
import org.lightningcraft.network.MessageSpawnParticle;

public class Effect {

	private static Random random = new Random();

	public static boolean lightningGen(Level level, BlockPos pos) {
		if(!level.isClientSide) {
			 EntityLCLightningBolt lightning = new EntityLCLightningBolt(LCEntities.LC_LIGHTNING_BOLT.get(), level, pos.getX(), pos.getY(), pos.getZ(), false);
			 lightning.setPos(pos.getX(), pos.getY() - 0.5, pos.getZ());
			 level.addFreshEntity(lightning);
			 return true;
		}
		return false;
	}

	public static boolean lightning(Level level, double x, double y, double z, boolean isRandom, double range)
	{
		if(!level.isClientSide) {
			double xoff;
			double zoff;
			if(isRandom) {
				xoff = random.nextDouble() * range * 2 - range;
				zoff = random.nextDouble() * range * 2 - range;
			} else {
				xoff = 0;
				zoff = 0;
			}

			 x += xoff;
			 z += zoff;

			 if(isRandom) {
				 Integer yy = WorldUtils.getOpenSurface(level, (int)x, (int)z, (int)y, 10);
				 if(yy == null) return false;
				 y = yy;
			 }

			 EntityLCLightningBolt lightning = new EntityLCLightningBolt(LCEntities.LC_LIGHTNING_BOLT.get(), level, x, y, z, true);
			 lightning.setPos(x, y - 0.5, z);
			 level.addFreshEntity(lightning);
			 return true;
		}
		return false;
	}

    public static boolean lightning(Level level, double x, double y, double z){
		return lightning(level, x, y, z, false, 0);
	}

	public static boolean lightning(Entity entity, boolean isRandom, double range)
	{
		if(!entity.level().isClientSide) {
			double xoff;
			double zoff;
			if(isRandom) {
				xoff = random.nextDouble() * range*2 - range;
				zoff = random.nextDouble() * range*2 - range;
			} else {
				xoff = 0;
				zoff = 0;
			}

			 Level level = entity.level();
			 double x = entity.getX() + xoff;
			 double z = entity.getZ() + zoff;
			 double y;

			 if(isRandom) {
				 Integer yy = WorldUtils.getOpenSurface(level, (int)x, (int)z, (int)entity.getY(), 10);
				 if(yy == null) return false;
				 y = yy;
			 } else {
				 y = entity.getY();
			 }

			 EntityLCLightningBolt lightning = new EntityLCLightningBolt(LCEntities.LC_LIGHTNING_BOLT.get(), level, x, y, z, true);
			 lightning.setPos(x, y - 0.5, z);
			 level.addFreshEntity(lightning);
			 return true;
		}
		return false;
	}

    public static boolean lightning(Entity entity, boolean isRandom){
		return lightning(entity, isRandom, 16);
	}

    public static void specialSword(String name, Level level, double x, double y, double z) {
    	for(int i = 0; i < LCParticles.swordParticleCount; i++) {
			float xoff = (random.nextFloat() - 0.5F) * LCParticles.swordParticleSpread;
			float yoff = (random.nextFloat() + 0.0F)  * LCParticles.swordParticleSpread;
			float zoff = (random.nextFloat() - 0.5F)  * LCParticles.swordParticleSpread;

		}
    }

    public static void ender(Level level, double x, double y, double z, double tx, double ty, double tz, boolean sound2x) {
    	short nParticles = 64;
    	float width = 1;
    	float height = 2;

        for (int l = 0; l < nParticles; ++l)
        {
            double d6 = l / (nParticles - 1.0D);
            float f = (random.nextFloat() - 0.5F) * 0.2F;
            float f1 = (random.nextFloat() - 0.5F) * 0.2F;
            float f2 = (random.nextFloat() - 0.5F) * 0.2F;
            double d7 = tx + (x - tx) * d6 + (random.nextDouble() - 0.5D) * width * 2.0D;
            double d8 = ty + (y - ty) * d6 + random.nextDouble() * height;
            double d9 = tz + (z - tz) * d6 + (random.nextDouble() - 0.5D) * width * 2.0D;

        }

		float pitch = 0.9F + random.nextFloat() * 0.2F;
		level.playSound(null, x , y, z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, pitch);
		if(sound2x) level.playSound(null, tx , ty, tz, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, pitch);

    }

    public static void ender(Level level, double x, double y, double z, double tx, double ty, double tz) {
    	ender(level, x, y, z, tx, ty, tz, true);
    }

}
