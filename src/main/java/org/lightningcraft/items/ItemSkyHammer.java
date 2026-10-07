package org.lightningcraft.items;

import net.minecraft.world.entity.ai.attributes.Attribute;

import net.minecraft.world.item.Tier;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlot;
import com.google.common.collect.Multimap;
import org.lightningcraft.util.SkyUtils;

public class ItemSkyHammer extends ItemHammer {

	public ItemSkyHammer(Tier mat) {
		super(mat);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		return SkyUtils.withToolSpeedModifier(this, equipmentSlot, super.getDefaultAttributeModifiers(equipmentSlot), BASE_ATTACK_SPEED_UUID, attackSpeed);
	}

}
