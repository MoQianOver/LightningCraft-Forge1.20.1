package org.lightningcraft.util;

import org.lightningcraft.blocks.base.BlockVariant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class WorldUtils {

	private static final Block[] blacklist = new Block[]{Blocks.LAVA, Blocks.FIRE, Blocks.LAVA, Blocks.CACTUS};
	static final Random random = new Random();

	public static boolean checkChunksExist(net.minecraft.world.level.LevelReader level, int xFrom, int yFrom, int zFrom, int xTo, int yTo, int zTo) {
		if (yFrom >= 0 && yTo < 256) {
			for (int k1 = xFrom; k1 <= xTo; k1 += 4) {
				for (int l1 = zFrom; l1 <= zTo; l1 += 4) {
					if (!level.hasChunkAt(new BlockPos(k1, (yFrom + yTo) / 2, l1))) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

    public static Vec3i getStructureCenter(BoundingBox box) {
        return new BlockPos(box.minX() + (box.maxX() - box.minX() + 1) / 2, box.minY() + (box.maxY() - box.minY() + 1) / 2, box.minZ() + (box.maxZ() - box.minZ() + 1) / 2);
    }

	public static Player getClosestPlayer(Level level, double x, double y, double z, double distance) {
		double d0 = -1.0D;
		Player entityplayer = null;

		for (int i = 0; i < level.players().size(); ++i)
		{
			Player entityplayer1 = level.players().get(i);

			if (EntitySelector.NO_SPECTATORS.test(entityplayer1))
			{
				double d1 = entityplayer1.distanceToSqr(x, y, z);

				if ((distance < 0.0D || d1 < distance * distance) && (d0 == -1.0D || d1 < d0))
				{
					d0 = d1;
					entityplayer = entityplayer1;
				}
			}
		}

		return entityplayer;
	}

	public static boolean blockMatches(net.minecraft.world.level.BlockGetter level, BlockPos pos, Block block, int meta) {
		BlockState state = level.getBlockState(pos);
		return state.getBlock() == block && block instanceof BlockVariant bv && bv.getMetaFromState(state) == meta;
	}

	public static Block getBlock(net.minecraft.world.level.LevelReader level, int x, int y, int z) {
		return level.getBlockState(new BlockPos(x, y, z)).getBlock();
	}

	public static Player getClosestVulnerablePlayer(Level level, double x, double y, double z, double dist, List<UUID> excludePlayers)
	{
		double d4 = -1.0D;
		Player entityplayer = null;

		for (int i = 0; i < level.players().size(); ++i)
		{
			Player entityplayer1 = level.players().get(i);

			if (entityplayer1 != null && !excludePlayers.contains(entityplayer1.getUUID()) && !entityplayer1.getAbilities().invulnerable && entityplayer1.isAlive())
			{
				double d5 = entityplayer1.distanceToSqr(x, y, z);
				double d6 = dist;

				if (entityplayer1.isShiftKeyDown())
				{
					d6 = dist * 0.800000011920929D;
				}

				if (entityplayer1.isInvisible())
				{
					float f = (float) entityplayer1.getArmorValue() / 20.0F;

					if (f < 0.1F)
					{
						f = 0.1F;
					}

					d6 *= 0.7F * f;
				}

				if ((dist < 0.0D || d5 < d6 * d6) && (d4 == -1.0D || d5 < d4))
				{
					d4 = d5;
					entityplayer = entityplayer1;
				}
			}
		}

		return entityplayer;
	}

	public static Entity getClosestVulnerableEntityOfType(Level level, double x, double y, double z, double dist, Class type, Class... excludes)
	{
		double d4 = -1.0D;
		Entity entity = null;

		List<Entity> loadedEntities = level.getEntitiesOfClass(Entity.class, new AABB(x - dist, y - dist, z - dist, x + dist, y + dist, z + dist));
		for (int i = 0; i < loadedEntities.size(); ++i)
		{
			Entity entity1 = loadedEntities.get(i);

			if (entity1 != null && type.isInstance(entity1) && !entity1.isInvulnerableTo(level.damageSources().generic()) && entity1.isAlive())
			{

				boolean skip = false;
				for(Class c : excludes) {
					if(c.isInstance(entity1)) skip = true;
				}
				if(!skip) {

					double d5 = entity1.distanceToSqr(x, y, z);
					double d6 = dist;

					if (entity1.isShiftKeyDown())
					{
						d6 = dist * 0.800000011920929D;
					}

					if (entity1.isInvisible())
					{
						float f;
						if(entity1 instanceof Player) {
							f = (float) ((Player)entity1).getArmorValue() / 20.0F;
						} else {
							f = 0;
						}

						if (f < 0.1F)
						{
							f = 0.1F;
						}

						d6 *= 0.7F * f;
					}

					if ((dist < 0.0D || d5 < d6 * d6) && (d4 == -1.0D || d5 < d4))
					{
						d4 = d5;
						entity = entity1;
					}
				}
			}
		}

		return entity;
	}

	public static Integer getOpenSurface(net.minecraft.world.level.LevelReader level, int x, int z, int starty, int distance, boolean onSolidBlock) {
		starty += 2;
		for(int y = starty + distance; y >= starty - distance; y--) {
			Block block;
			if(getBlock(level, x, y + 2, z) == Blocks.AIR && getBlock(level, x, y + 1, z) == Blocks.AIR && (block = getBlock(level, x, y, z)) != Blocks.AIR) {
				if(!LCMisc.inArray(block, blacklist) && (onSolidBlock == false || level.getBlockState(new BlockPos(x, y, z)).isSolidRender(level, new BlockPos(x, y, z)))) {
					if(y < Math.max(level.getHeight() - 1, starty)) {
						return y + 1;
					}
				}
			}
		}
		return null;
	}

	public static Integer getOpenSurfaceReverse(net.minecraft.world.level.LevelReader level, int x, int z, int starty, int distance, boolean onSolidBlock) {
		starty += 2;
		for(int y = starty - distance; y <= starty + distance; y++) {
			Block block;
			if(getBlock(level, x, y + 2, z) == Blocks.AIR && getBlock(level, x, y + 1, z) == Blocks.AIR && (block = getBlock(level, x, y, z)) != Blocks.AIR) {
				if(!LCMisc.inArray(block, blacklist) && (onSolidBlock == false || level.getBlockState(new BlockPos(x, y, z)).isSolidRender(level, new BlockPos(x, y, z)))) {
					if(y < Math.max(level.getHeight() - 1, starty)) {
						return y + 1;
					}
				}
			}
		}
		return null;
	}

	public static Integer getOpenSurfaceCentered(net.minecraft.world.level.LevelReader level, int x, int z, int starty, int distance, boolean onSolidBlock) {
		Integer y;
		y = getOpenSurfaceReverse(level, x, z, starty - distance / 2, distance / 2, onSolidBlock);
		if(y == null) y = getOpenSurface(level, x, z, starty + distance / 2, distance / 2, onSolidBlock);

		if (y == null) {
			StringBuilder sb = new StringBuilder();
			int hi = starty + distance + 2;
			int lo = starty - distance - 2;
			for (int yy = hi; yy >= lo; yy -= 8) {
				sb.append(" y=").append(yy).append(":").append(getBlock(level, x, yy, z).getDescriptionId());
			}
			org.lightningcraft.ref.Log.logger.info("[LC-STRUCT] \u627e\u4e0d\u5230\u5730\u8868 x=" + x + " z=" + z
					+ " \u7a97\u53e3=" + lo + ".." + hi + " onSolid=" + onSolidBlock + sb);
		}
		return y;
	}

	public static Integer getOpenSurface(net.minecraft.world.level.LevelReader level, int x, int z, int starty, int distance) {
		return getOpenSurface(level, x, z, starty, distance, false);
	}

	public static Integer getOpenCeiling(net.minecraft.world.level.LevelReader level, int x, int z, int starty, int endy, int airBlocks) {
		for(int y = endy; y >= starty; y--) {
			if(canSpawnAtCeilingPosition(level, x, y - 1, z, airBlocks)) {
				if(!LCMisc.inArray(getBlock(level, x, y, z), blacklist)) {
					if(y < Math.max(level.getHeight() - 1, starty)) {
						return y - 1;
					}
				}
			}
		}
		return null;
	}

	public static boolean canSpawnAtPosition(net.minecraft.world.level.LevelReader level, int x, int y, int z, Block onBlock, int airAbove) {
		if(getBlock(level, x, y - 1, z) == onBlock) {
			for(int i = 0; i < airAbove; i++) {
				if(getBlock(level, x, y + i, z) != Blocks.AIR) return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public static boolean canSpawnAtPosition(net.minecraft.world.level.LevelReader level, int x, int y, int z, int airAbove) {
		if(getBlock(level, x, y - 1, z) != Blocks.AIR) {
			for(int i = 0; i < airAbove; i++) {
				if(getBlock(level, x, y + i, z) != Blocks.AIR) return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public static boolean canSpawnAtPositionLoosely(Level level, int x, int y, int z, int airAbove, int marginDown, int marginUp) {
		for(int j = y - marginDown; j <= y + marginUp; j++) {
			if(canSpawnAtPosition(level, x, j, z, airAbove)) {
				return true;
			}
		}
		return false;
	}

	public static boolean canSpawnAtCeilingPosition(net.minecraft.world.level.LevelReader level, int x, int y, int z, int airBelow) {
		if(getBlock(level, x, y + 1, z) != Blocks.AIR) {
			for(int i = 0; i < airBelow; i++) {
				if(getBlock(level, x, y - i, z) != Blocks.AIR) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static Entity getEntityFromUUID(Level level, UUID id) {

		if (level instanceof ServerLevel serverLevel) {
			return serverLevel.getEntity(id);
		}
		return null;
	}

	public static LivingEntity getEntityLivingBaseFromUUID(Level level, UUID id, boolean isPlayer) {
		Entity e;

		if(!isPlayer) {
			e = getEntityFromUUID(level, id);
		} else {
			e = getEntityPlayerFromUUID(level, id);
		}

		if(e instanceof LivingEntity) {
			return (LivingEntity)e;
		} else {
			return null;
		}
	}

	public static LivingEntity getEntityLivingBaseFromUUID(Level level, UUID id) {
		return getEntityLivingBaseFromUUID(level, id, false);
	}

	public static Player getEntityPlayerFromUUID(Level level, UUID id) {
		for (Player e : level.players()) {
			if (e.getUUID().toString().equals(id.toString())) {
				return e;
			}
		}
		return null;
	}

	public static boolean spawnMobNearPlayer(Player player, Monster mob, double mindist, double maxdist) {

		double dd = maxdist - mindist;
		double dist = mindist + random.nextDouble() * dd;
		float angle = (float)(random.nextDouble() * 2 * Math.PI);

		double x = player.getX() - Mth.sin(angle) * dist;
		double z = player.getZ() + Mth.cos(angle) * dist;
		Integer y = getOpenSurface(player.level(), (int)x, (int)z, (int)player.getY(), 10, true);
		if(y == null) return false;

		if(player.level().getMaxLocalRawBrightness(new BlockPos((int)x, y, (int)z)) <= 7) {
			mob.teleportTo(x, y, z);
			return player.level().addFreshEntity(mob);
		}

		return false;
	}

	public static Vec3 estimateEntityPosition(Entity e, double originX, double originY, double originZ, double speed) {
		double ticks = Math.sqrt(e.distanceToSqr(originX, originY, originZ)) / speed;
		double resultX, resultY, resultZ;
		resultX = e.getDeltaMovement().x * ticks + e.getX();
		resultY = (e.getDeltaMovement().y + 0.0784) * ticks + e.getY();
		resultZ = e.getDeltaMovement().z * ticks + e.getZ();
		return new Vec3(resultX, resultY, resultZ);
	}

}
