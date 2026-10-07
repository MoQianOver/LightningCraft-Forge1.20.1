package org.lightningcraft.api.capabilities;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ILightningUpgradable {

	public InteractionResult onLightningUpgrade(ItemStack stack, Player player, Level level, BlockPos pos,
			InteractionHand hand, Direction facing, float hitX, float hitY, float hitZ);

	public boolean isUpgraded();

	public void setUpgraded(boolean upgraded);

}
