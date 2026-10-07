package org.lightningcraft.integration.tconstruct;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.lightningcraft.events.PlayerEvents;
import org.lightningcraft.init.LCFluids;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.util.SkyUtils;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitRepair extends AbstractTrait {

	public TraitRepair() {
		super(RefStrings.MODID + ":repair", LCFluids.moltenMystic.getColor());
	}

	@Override
	public void onUpdate(ItemStack tool, Level level, Entity entity, int itemSlot, boolean isSelected) {
		if(entity instanceof Player && SkyUtils.canWriteItemNBT(tool, (Player)entity)) {
			int damage = tool.getDamageValue();

			if(damage > 0) {
				if(entity.ticksExisted % Mth.floor(PlayerEvents.repairTime) == 0) {
					tool.setDamageValue(damage - 1);
				}
			}
		}
	}

}
