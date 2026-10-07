package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import org.lightningcraft.blocks.BlockUnderTNT;
import org.lightningcraft.init.LCNetwork;
import org.lightningcraft.network.MessageSyncTNT;

public class EntityLCTNTPrimed extends Entity {

	public int fuse;
	public int variant;
	private LivingEntity tntPlacedBy;
	private boolean firstTick = false;

	public EntityLCTNTPrimed(EntityType<EntityLCTNTPrimed> type, Level level ) {
		super(type, level);
		this.fuse = 20;
	}

	public EntityLCTNTPrimed(EntityType<EntityLCTNTPrimed> type, Level level, int variant, double x, double y, double z, LivingEntity placer) {
		this(org.lightningcraft.init.LCEntities.LC_TNT_PRIMED.get(), level);
		this.setPos(x, y, z);
		float f = (float)(Math.random() * Math.PI * 2.0D);
		this.setDeltaMovement(-((float)Math.sin(f)) * 0.02F, 0.20000000298023224D, -((float)Math.cos(f)) * 0.02F);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.variant = variant;
		this.tntPlacedBy = placer;
		this.fuse = 20;

	}

	@Override
	protected void defineSynchedData() {}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isRemoved();
	}

	protected void explode() {
		float f = 22f;
		int i = 3;
		if(variant == BlockUnderTNT.MYSTIC) {
			for(int j = -i; j <= i; j += i) {
				this.level().explode(this, this.getX() - i, this.getY() + j, this.getZ() - i, f, Level.ExplosionInteraction.TNT);
				this.level().explode(this, this.getX() + i, this.getY() + j, this.getZ() - i, f, Level.ExplosionInteraction.TNT);
				this.level().explode(this, this.getX() + i, this.getY() + j, this.getZ() + i, f, Level.ExplosionInteraction.TNT);
				this.level().explode(this, this.getX() - i, this.getY() + j, this.getZ() + i, f, Level.ExplosionInteraction.TNT);
			}
		} else {
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), f, Level.ExplosionInteraction.TNT);
		}
	}

	@Override
	public void tick() {
		if(!this.level().isClientSide && !firstTick) {
			LCNetwork.CHANNEL.send(net.minecraftforge.network.PacketDistributor.NEAR.with(() -> new net.minecraftforge.network.PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 1024.0D, this.level().dimension())), new MessageSyncTNT(this));
			firstTick = true;
		}

		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.03999999910593033D, this.getDeltaMovement().z);
		this.move(MoverType.SELF, this.getDeltaMovement());
		this.setDeltaMovement(this.getDeltaMovement().x * 0.9800000190734863D, this.getDeltaMovement().y, this.getDeltaMovement().z);
		this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y * 0.9800000190734863D, this.getDeltaMovement().z);
		this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z * 0.9800000190734863D);

		if (this.onGround())
		{
			this.setDeltaMovement(this.getDeltaMovement().x * 0.699999988079071D, this.getDeltaMovement().y, this.getDeltaMovement().z);
			this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z * 0.699999988079071D);
			this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y * -0.5D, this.getDeltaMovement().z);
		}

		if (this.fuse-- <= 0)
		{
			this.discard();

			if (!this.level().isClientSide)
			{
				this.explode();
			}
		}
		else
		{
			this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putByte("Fuse", (byte)this.fuse);
		tag.putByte("Variant", (byte)this.variant);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.fuse = tag.getByte("Fuse");
		this.variant = tag.getByte("Variant");
	}

	public LivingEntity getTntPlacedBy() {
		return this.tntPlacedBy;
	}

}
