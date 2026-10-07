package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import org.lightningcraft.ref.RefMisc;

import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class EntityLCItem extends ItemEntity {

	public EntityLCItem(EntityType<EntityLCItem> type, Level level) {
		super(type, level);
	}

	public EntityLCItem(EntityType<EntityLCItem> type, Level worldIn, double x, double y, double z, ItemStack stack) {
		super(type, worldIn);
		this.setPos(x, y, z);
		this.setItem(stack);
        ObfuscationReflectionHelper.setPrivateValue(ItemEntity.class, this, 500, RefMisc.DEV ? "health" : "field_70291_e");
	}

	@Override
	public void thunderHit(net.minecraft.server.level.ServerLevel level, LightningBolt lightningBolt) {}

}
