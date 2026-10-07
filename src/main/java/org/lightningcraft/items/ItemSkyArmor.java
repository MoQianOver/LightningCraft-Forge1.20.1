package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.IPotionEffectProvider;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.items.base.ItemArmorLC;
import org.lightningcraft.ref.LCText;

public class ItemSkyArmor extends ItemArmorLC implements IPotionEffectProvider {

	public ItemSkyArmor(ArmorMaterial mat, EquipmentSlot armorType) {
		super(mat, armorType);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		if(stack.getItem() == LCItems.skyHelm) {
			list.add(Component.literal(LCText.getSkyHelmLore()));
		} else if(stack.getItem() == LCItems.skyChest) {
			list.add(Component.literal(LCText.getSkyChestLore()));
		} else if(stack.getItem() == LCItems.skyLegs) {
			list.add(Component.literal(LCText.getSkyLegsLore()));
		} else if(stack.getItem() == LCItems.skyBoots) {
			list.add(Component.literal(LCText.getSkyBootsLore()));
		}
	}

	@Override
	public boolean canApplyEffect(ItemStack stack, Player player, int invPosition) {
		if(player.level().getGameTime() % 20 == 0) {
			if(player.getItemBySlot(EquipmentSlot.HEAD) == stack ||
					player.getItemBySlot(EquipmentSlot.CHEST) == stack ||
					player.getItemBySlot(EquipmentSlot.LEGS) == stack ||
					player.getItemBySlot(EquipmentSlot.FEET) == stack) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<MobEffectInstance> getEffects(ItemStack stack, Player player, int invPosition) {
		JointList<MobEffectInstance> list = new JointList();
		if(stack.getItem() == LCItems.skyHelm) {
			list.join(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, true, false));
		} else if(stack.getItem() == LCItems.skyChest) {
			list.join(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30, 0, true, false));
		} else if(stack.getItem() == LCItems.skyLegs) {
			list.join(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30, 0, true, false));
		} else if(stack.getItem() == LCItems.skyBoots) {
			list.join(new MobEffectInstance(MobEffects.JUMP, 30, 2, true, false));
		}
		return list;
	}

}
