package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;

import net.minecraft.world.item.Tier;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.Multimap;

import org.lightningcraft.api.IMysticGear;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.items.base.ItemSpadeLC;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.util.SkyUtils;

public class ItemSkySpade extends ItemSpadeLC {

	public ItemSkySpade(Tier mat) {
		super(mat);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		if(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
			if(LCConfig.autoSmelt) list.add(Component.literal(LCText.getAutoSmeltLore()));
			if(this instanceof IMysticGear) list.add(Component.literal(LCText.getFortuneBonusLore()));
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		return SkyUtils.withToolSpeedModifier(this, equipmentSlot, super.getDefaultAttributeModifiers(equipmentSlot), BASE_ATTACK_SPEED_UUID, attackSpeed);
	}

}
