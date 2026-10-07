package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;

public class EntityUnderworldSilverfish extends Silverfish {

	public EntityUnderworldSilverfish(EntityType<EntityUnderworldSilverfish> type, Level level ) {
		super(type, level);

	}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return net.minecraft.world.entity.monster.Silverfish.createAttributes()
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0D)
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.3D)
				.add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 5.0D);
	}

}
