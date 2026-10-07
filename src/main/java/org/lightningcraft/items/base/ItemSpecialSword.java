package org.lightningcraft.items.base;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import org.lightningcraft.util.SkyUtils;

public abstract class ItemSpecialSword extends ItemSwordLC {

	protected final double attackSpeed;

	public ItemSpecialSword(Tier mat, double attackSpeedOffset) {
		super(mat, Rarity.RARE);
		this.attackSpeed = SkyUtils.SWORD_ATTACK_SPEED + attackSpeedOffset;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		super.getDefaultAttributeModifiers(slot).forEach((attribute, modifier) -> {
			if (!(slot == EquipmentSlot.MAINHAND && attribute == Attributes.ATTACK_SPEED)) {
				builder.put(attribute, modifier);
			}
		});

		if (slot == EquipmentSlot.MAINHAND) {
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(
					BASE_ATTACK_SPEED_UUID, "Tool modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
		}

		return builder.build();
	}

}
