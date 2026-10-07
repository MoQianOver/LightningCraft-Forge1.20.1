package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.lightningcraft.ref.RefStrings;

public class EntityUnderworldCreeper extends Creeper {

	public EntityUnderworldCreeper(EntityType<EntityUnderworldCreeper> type, Level worldIn ) {
		super(type, worldIn);

		CompoundTag tag = new CompoundTag();
		byte radius = 6;
		tag.putByte("ExplosionRadius", radius);
		this.readAdditionalSaveData(tag);
	}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return net.minecraft.world.entity.monster.Creeper.createAttributes()
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.3D)
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0D);
	}

}