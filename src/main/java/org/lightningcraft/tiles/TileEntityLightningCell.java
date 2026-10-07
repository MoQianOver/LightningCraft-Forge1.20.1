package org.lightningcraft.tiles;
import org.lightningcraft.api.capabilities.ILightningUpgradable;
import javax.annotation.Nullable;
import net.minecraftforge.common.util.LazyOptional;

import org.lightningcraft.init.LCTileEntities;

import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import org.lightningcraft.api.capabilities.implementation.BaseLightningUpgradable;
import org.lightningcraft.blocks.BlockAirTerminal;
import org.lightningcraft.entities.EntityLCLightningBolt;
import org.lightningcraft.init.LCCapabilities;
import org.lightningcraft.ref.Metal.Rod;
import org.lightningcraft.util.Effect;
import org.lightningcraft.util.WeatherUtils;

public class TileEntityLightningCell extends TileEntityBase {

	public double storedPower;
	public double maxPower;
	public double efficiency;
	public int cooldownTime;
	public String cellName;
	public boolean creative;

	private boolean topTierTerminal = false;
	private boolean didCheck = false;

	public TileEntityLightningCell(double mp, String name, boolean creative, BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_CELL.get(), pos, state);
		this.maxPower = mp;
		this.cellName = name;
		if(creative) {
			this.creative = creative;
			this.storedPower = this.maxPower;
		}
	}

	public TileEntityLightningCell(BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_CELL.get(), pos, state);}

	public void tick() {

		EntityLCLightningBolt lightning;
		boolean dosave = false;

		if (level.isClientSide) {
			isAirTerminalPresent();
			return;
		}

		if(this.storedPower > this.maxPower) this.storedPower = this.maxPower;
		if(this.storedPower < 0) this.storedPower = 0;
		if(this.creative) this.storedPower = this.maxPower;

		if(this.cooldownTime > 0) {
			this.cooldownTime--;
			dosave = true;
		}

		if(isAirTerminalPresent()) {

			double chance = 1D/100000D;
			if(this.level.isThundering()) chance = 1D/1000D;
			if(random.nextDouble() <= chance && this.storedPower < this.maxPower - 100D * this.efficiency) {
				Effect.lightningGen(this.level, pos.above());
			}

			AABB box = new AABB(this.getX() - 5, this.getY() - 2, this.getZ() - 5, this.getX() + 5, this.getY() + 4, this.getZ() + 5);
			List<LightningBolt> bolts = WeatherUtils.getLightningBoltsWithinAABB(this.level, box);

			if(!bolts.isEmpty() && this.cooldownTime <= 0 && this.storedPower < this.maxPower - 100D * this.efficiency) {

				for(LightningBolt bolt : bolts) {
					if(bolt.isRemoved()) return;
					bolt.discard();
				}

				this.storedPower += 100D * this.efficiency;
				this.cooldownTime = 40;
				dosave = true;
			}
		}

		if(dosave) this.setChanged();

	}

	private BaseLightningUpgradable upgrade = new BaseLightningUpgradable() {
		@Override
		public InteractionResult onLightningUpgrade(ItemStack stack, Player player, Level level, BlockPos pos,
				InteractionHand hand, Direction facing, float hitX, float hitY, float hitZ) {
			setUpgraded(true);
			maxPower *= 1.5;
			cellName += " (Upgr.)";
			return InteractionResult.SUCCESS;
		}
	};

	private LazyOptional<ILightningUpgradable> upgradeCapability;

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (capability == LCCapabilities.LIGHTNING_UPGRADABLE) {
			if (upgradeCapability == null) upgradeCapability = LazyOptional.of(() -> upgrade);
			return upgradeCapability.cast();
		}
		return super.getCapability(capability, facing);
	}

	public boolean isUpgraded() {
		return upgrade.isUpgraded();
	}

	public void setUpgraded(boolean upgraded) {
		upgrade.setUpgraded(upgraded);
	}

	@Override
	public void saveAdditional(CompoundTag par1) {
		super.saveAdditional(par1);
		par1.putDouble("storedPower", this.storedPower);
		par1.putDouble("maxPower", this.maxPower);
		par1.putInt("cooldownTime", this.cooldownTime);
		par1.putBoolean("topTierCheck", this.didCheck);
		par1.putString("customName", this.cellName);
		par1.putBoolean("isCreative", this.creative);
		upgrade.serializeNBT(par1);
	}

	@Override
	public void load(CompoundTag par1) {
		super.load(par1);
		this.storedPower = par1.getDouble("storedPower");
		this.maxPower = par1.getDouble("maxPower");
		this.cooldownTime = par1.getInt("cooldownTime");
		this.didCheck = par1.getBoolean("topTierCheck");

		this.cellName = org.lightningcraft.blocks.BlockLightningCell.cellNameOf(this.getBlockState());
		this.creative = par1.getBoolean("isCreative");
		upgrade.deserializeNBT(par1);
	}

	public boolean isAirTerminalPresent() {

		this.topTierTerminal = false;
		BlockState state = this.level.getBlockState(getBlockPos().above());
		Block test = state.getBlock();
		int meta = org.lightningcraft.blocks.base.BlockVariant.getMeta(state);
		boolean flag;

		if(test instanceof BlockAirTerminal) {
			flag = true;
			this.efficiency = ((BlockAirTerminal)test).getEfficiency(state);
			if(meta == Rod.MYSTIC) this.topTierTerminal = true;
		} else {
			flag = false;
			this.efficiency = 0;
		}
		return flag;
	}

}
