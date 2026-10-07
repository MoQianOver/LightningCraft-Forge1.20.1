package org.lightningcraft.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.init.LCTileEntities;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.items.ItemBattery;
import org.lightningcraft.util.IntList;
import org.lightningcraft.util.LCMisc;
import org.lightningcraft.util.WorldUtils;

public class TileEntityChargingPlate extends TileEntityLightningUser {

	public TileEntityChargingPlate(BlockPos pos, BlockState state) {
		super(LCTileEntities.CHARGING_PLATE.get(), pos, state);
	}

	public static final double chargeRate = 0.25F;

	public void tick() {
		if(!this.level.isClientSide && this.hasLPCell()) {
			if(this.getBlockMetadata() != 0) {

				Player user = WorldUtils.getClosestPlayer(level, getX(), getY(), getZ(), 1.5);
				if(user != null && LCMisc.posInInventory(user, LCItems.battery) >= 0 && this.tileCell.storedPower > 0) {
					int invPos = 0;
					IntList avoid = new IntList();
					while((invPos = LCMisc.posInInventory(user, LCItems.battery, avoid)) >= 0) {

						if(invPos >= 0) {
							ItemStack battery = user.getInventory().items.get(invPos);

							if(ItemBattery.getStoredPower(battery) < ItemBattery.getMaxPower(battery)) {

								if(this.drawCellPower(chargeRate)) {

									ItemBattery.addStoredPower(battery, chargeRate);

									if(ItemBattery.getStoredPower(battery) > ItemBattery.getMaxPower(battery)) {
										ItemBattery.setStoredPower(battery, ItemBattery.getMaxPower(battery));
									}
								}
								break;
							} else {
								avoid.add(invPos);
							}
						}
					}
				}

			}
		}
	}

}
