package org.lightningcraft.api.capabilities.implementation;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import org.lightningcraft.api.capabilities.ILightningUpgradable;

public class BaseLightningUpgradable implements ILightningUpgradable, INBTSerializable<CompoundTag> {

	private boolean isUpgraded;

	@Override
	public InteractionResult onLightningUpgrade(ItemStack stack, Player player, Level level, BlockPos pos,
			InteractionHand hand, Direction facing, float hitX, float hitY, float hitZ) {
		this.setUpgraded(true);
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean isUpgraded() {
		return isUpgraded;
	}

	@Override
	public void setUpgraded(boolean upgraded) {
		isUpgraded = upgraded;
	}

	@Override
	public void deserializeNBT(CompoundTag tagCompound) {
		this.isUpgraded = tagCompound.getBoolean("isUpgraded");
	}

	@Override
	public CompoundTag serializeNBT() {
		return serializeNBT(new CompoundTag());
	}

	public CompoundTag serializeNBT(CompoundTag existingTag) {
		existingTag.putBoolean("isUpgraded", this.isUpgraded);
		return existingTag;
	}

}
