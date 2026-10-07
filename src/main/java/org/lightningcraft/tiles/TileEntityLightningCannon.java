package org.lightningcraft.tiles;
import org.lightningcraft.init.LCEntities;
import net.minecraft.world.level.ClipContext;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.init.LCTileEntities;

import java.util.UUID;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.entities.EntityLCElectricAttack;
import org.lightningcraft.util.WorldUtils;

public class TileEntityLightningCannon extends TileEntityLightningUser {

	public static final int maxIdleTicks = 400;
	public static final int minIdleTicks = 40;
	public static final int eliteIdleTicks = 20;
	public static final double lePerAttack = 20;
	public static final double lePerAttackElite = 10;
	public static final int NULL_ERROR = -0x10000;

	private int ticksUntilNextAttack;
	private int currentTicks;
	private double tileX, tileY, tileZ;
	public EnumMode mode = EnumMode.DEFAULT;

	public static enum EnumMode {
		NONE(0, "Cannon Inactive"),
		MOBS(1, "Hostile Mobs Only"),
		ALL(2, "All Non-Player Entities");

		private static EnumMode DEFAULT = NONE;
		private int id;
		private String name;

		private EnumMode(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.id;
		}

		public static EnumMode assignMode(int id) {
			for(EnumMode m : EnumMode.values()) {
				if(m.id == id) return m;
			}
			return DEFAULT;
		}

	}

	public TileEntityLightningCannon(BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_CANNON.get(), pos, state);
		this.ticksUntilNextAttack = reload();
		this.currentTicks = 0;
	}

	@OnlyIn(Dist.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 16384.0D;
	}

	private int reload() {
		try {
			if(this.getBlockMetadata() < 2) {
				return random.nextInt(maxIdleTicks - minIdleTicks) + minIdleTicks;
			} else {
				return eliteIdleTicks;
			}
		} catch (NullPointerException e) {
			return NULL_ERROR;
		}
	}

	public double getAttackEnergy() {
		if(this.getBlockMetadata() < 2) {
			return lePerAttack;
		} else {
			return lePerAttackElite;
		}
	}

	public void tick() {
		if(!this.level.isClientSide) {

			tileX = getX() + 0.5;
			tileY = getY() + 0.5;
			tileZ = getZ() + 0.5;
			int meta = this.getBlockMetadata();

			LivingEntity target = null;
			boolean doAttack = false;
			boolean hasCell = false;
			boolean drawPower = false;

			if(this.ticksUntilNextAttack == NULL_ERROR) this.ticksUntilNextAttack = reload();
			this.currentTicks++;

			if(meta > 0) hasCell = this.hasLPCell();

			if(this.currentTicks > this.ticksUntilNextAttack) {
				doAttack = true;
				if(meta == 0) {
					target = WorldUtils.getClosestVulnerablePlayer(this.level, tileX, tileY, tileZ, 32.0D, new JointList<UUID>());
				} else {
					if(hasCell && this.canDrawCellPower(getAttackEnergy())) {
						drawPower = true;
						switch(mode) {
						case MOBS:
							target = (LivingEntity)WorldUtils.getClosestVulnerableEntityOfType(this.level, tileX, tileY, tileZ, 32.0D,
									Enemy.class);
							break;
						case ALL:
							target = (LivingEntity)WorldUtils.getClosestVulnerableEntityOfType(this.level, tileX, tileY, tileZ, 32.0D,
									Mob.class);
							break;
						default:
						}
					}
				}
			}

			if(doAttack) {
				if(target != null && this.canEntityBeSeen(target)) {
					EntityLCElectricAttack bolt = new EntityLCElectricAttack(LCEntities.LC_ELECTRIC_ATTACK.get(), level, tileX, tileY, tileZ, target);
					this.level.addFreshEntity(bolt);
					this.level.playSound(null, tileX, tileY, tileZ, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 3.0F, 0.1F);
					if(drawPower) this.drawCellPower(getAttackEnergy());
				}

				this.ticksUntilNextAttack = reload();
				this.currentTicks = 0;
				this.setChanged();
			}
		}
	}

	public void rotateOperatingMode() {
		this.mode = EnumMode.assignMode((mode.id + 1) % EnumMode.values().length);
		this.setChanged();
	}

	public boolean canEntityBeSeen(Entity e) {
		return this.level.clip(new ClipContext(new Vec3(tileX + 1, tileY, tileZ + 0), new Vec3(e.getX(), e.getY() + e.getEyeHeight(), e.getZ()), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null)) == null ||
				this.level.clip(new ClipContext(new Vec3(tileX + 0, tileY, tileZ + 1), new Vec3(e.getX(), e.getY() + e.getEyeHeight(), e.getZ()), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null)) == null ||
				this.level.clip(new ClipContext(new Vec3(tileX - 1, tileY, tileZ + 0), new Vec3(e.getX(), e.getY() + e.getEyeHeight(), e.getZ()), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null)) == null ||
				this.level.clip(new ClipContext(new Vec3(tileX + 0, tileY, tileZ - 1), new Vec3(e.getX(), e.getY() + e.getEyeHeight(), e.getZ()), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null)) == null;

	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);

		this.ticksUntilNextAttack = tagCompound.getInt("ticksUntilNextAttack");
		this.currentTicks = tagCompound.getInt("currentTicks");

		this.mode = EnumMode.assignMode(tagCompound.getInt("operatingMode"));
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);

		tagCompound.putInt("ticksUntilNextAttack", ticksUntilNextAttack);
		tagCompound.putInt("currentTicks", currentTicks);

		tagCompound.putInt("operatingMode", this.mode.id);
	}

}
