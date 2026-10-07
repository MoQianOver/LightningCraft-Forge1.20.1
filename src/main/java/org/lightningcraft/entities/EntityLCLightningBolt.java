package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;

public class EntityLCLightningBolt extends LightningBolt {

    private int lightningState;

    public long boltVertex;

    private int boltLivingTime;

    private boolean doSetFire;

    public EntityLCLightningBolt(EntityType<EntityLCLightningBolt> type, Level level) {
		super(type, level);
	}

	public EntityLCLightningBolt(EntityType<EntityLCLightningBolt> type, Level level, double x, double y, double z, boolean setFire)
    {
    	super(type, level);
		this.setPos(x, y, z);
        this.moveTo(x, y, z, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.random.nextLong();
        this.boltLivingTime = this.random.nextInt(3) + 1;
        this.doSetFire = setFire;
        BlockPos blockpos = this.blockPosition();

        if (setFire && !this.level().isClientSide && this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOFIRETICK) &&
        		(this.level().getDifficulty() == Difficulty.NORMAL || this.level().getDifficulty() == Difficulty.HARD) && this.level().isAreaLoaded(blockpos, 10)) {
            int i = Mth.floor(x);
            int j = Mth.floor(y);
            int k = Mth.floor(z);

            BlockState state1 = this.level().getBlockState(new BlockPos(i, j, k));
            if (state1.isAir() && Blocks.FIRE.defaultBlockState().canSurvive(level, new BlockPos(i, j, k)))
            {
                this.level().setBlock(new BlockPos(i, j, k), Blocks.FIRE.defaultBlockState(), 3);
            }

            for (i = 0; i < 4; ++i)
            {
                j = Mth.floor(x) + this.random.nextInt(3) - 1;
                k = Mth.floor(y) + this.random.nextInt(3) - 1;
                int l = Mth.floor(z) + this.random.nextInt(3) - 1;

                BlockState state2 = this.level().getBlockState(new BlockPos(j, k, l));
                if (state2.isAir() && Blocks.FIRE.defaultBlockState().canSurvive(level, new BlockPos(j, k, l)))
                {
                    this.level().setBlock(new BlockPos(j, k, l), Blocks.FIRE.defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
	public void tick()
    {
        if (this.lightningState == 2)
        {

        	this.level().playSound(null, getX(), getY(), getZ(),
        			SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 3.0F, 2.8F + this.random.nextFloat() * 0.2F);
        	this.level().playSound(null, getX(), getY(), getZ(),
        			SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.lightningState;

        if (this.lightningState < 0)
        {
            if (this.boltLivingTime == 0)
            {
                this.discard();
            }
            else if (this.lightningState < -this.random.nextInt(10))
            {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.random.nextLong();
                BlockPos blockpos = this.blockPosition();

                if (doSetFire && !this.level().isClientSide && this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOFIRETICK) &&
                		(this.level().getDifficulty() == Difficulty.NORMAL || this.level().getDifficulty() == Difficulty.HARD) && this.level().isAreaLoaded(blockpos, 10)) {
                    int i = Mth.floor(this.getX());
                    int j = Mth.floor(this.getY());
                    int k = Mth.floor(this.getZ());

                    BlockState state = this.level().getBlockState(new BlockPos(i, j, k));
                    if (state.isAir() && Blocks.FIRE.defaultBlockState().canSurvive(this.level(), new BlockPos(i, j, k)))
                    {
                        this.level().setBlock(new BlockPos(i, j, k), Blocks.FIRE.defaultBlockState(), 3);
                    }
                }
            }
        }

        if (this.lightningState >= 0)
        {
            if (this.level().isClientSide)
            {
                this.level();
            }
            else
            {
                double d0 = 3.0D;
                List list = this.level().getEntities(this,
                		new AABB(this.getX() - d0, this.getY() - d0, this.getZ() - d0, this.getX() + d0, this.getY() + 6.0D + d0, this.getZ() + d0));

                for (int l = 0; l < list.size(); ++l)
                {
                    Entity entity = (Entity)list.get(l);
                    if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, this))
                        entity.thunderHit((net.minecraft.server.level.ServerLevel) this.level(), this);
                }
            }
        }
    }
}
