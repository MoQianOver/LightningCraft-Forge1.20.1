package org.lightningcraft.events;

import org.lightningcraft.util.LCStack;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.entities.EntityLCItem;
import org.lightningcraft.init.LCCapabilities;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.recipes.LightningTransformRecipes;
import org.lightningcraft.ref.Material;

public class EntityItemEvents {

	@SubscribeEvent
	public void onEntityItemStruckByLightning(EntityStruckByLightningEvent e) {
		Level level = e.getEntity().level();
		if(!level.isClientSide && !e.getEntity().isRemoved() && e.getEntity() instanceof ItemEntity) {

			ItemEntity item = (ItemEntity)e.getEntity();
			JointList<ItemStack> input = new JointList().join(item.getItem());
			JointList<ItemEntity> activeItems = new JointList().join(item);

			for(Entity t : level.getEntitiesOfClass(net.minecraft.world.entity.Entity.class, item.getBoundingBox().inflate(2.0D))) {
				if(!!t.isRemoved() && t instanceof ItemEntity && t != item && t.distanceTo(item) <= 2) {
					ItemEntity et = (ItemEntity)t;
					input.add(et.getItem());
					activeItems.add(et);
				}
			}

			ItemStack out = LightningTransformRecipes.instance().getTransformResult(input);
			if(out.isEmpty()) return;

			for(ItemEntity ent : activeItems) ent.discard();

			ItemEntity entityitem = new EntityLCItem(org.lightningcraft.init.LCEntities.LC_ITEM.get(), level, item.getX(), item.getY(), item.getZ(), out);
			level.addFreshEntity(entityitem);
		}
	}

	@SubscribeEvent
	public void onUpgradedTileBreak(BreakEvent e) {
		if(!e.getLevel().isClientSide()) {
			BlockEntity t = e.getLevel().getBlockEntity(e.getPos());
			if(t != null && t.getCapability(LCCapabilities.LIGHTNING_UPGRADABLE).isPresent() &&
					t.getCapability(LCCapabilities.LIGHTNING_UPGRADABLE).orElse(null).isUpgraded()) {
				Block.popResource((net.minecraft.server.level.ServerLevel) e.getLevel(), e.getPos(), LCStack.of(LCItems.material, 1, Material.UPGRADE));
			}
		}
	}

}
