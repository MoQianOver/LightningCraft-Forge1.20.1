package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Tier;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.gui.screens.Screen;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.lightningcraft.api.IKineticGear;
import org.lightningcraft.items.base.ItemSwordLC;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.util.ISimpleLEUser;
import org.lightningcraft.util.InventoryLE;
import org.lightningcraft.util.InventoryLE.LECharge;

public class ItemKineticSword extends ItemSwordLC implements ISimpleLEUser, IKineticGear {

	public static final double attackDamage = 10;

	public ItemKineticSword(Tier mat) {
		super(mat);
	}

	@Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity user) {
		InventoryLE.hitEntity(stack, entity, user, attackDamage, true);
    	return super.hurtEnemy(stack, entity, user);
    }

	@Override
	public Multimap getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return HashMultimap.create();
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		LECharge charge = new LECharge();
		boolean charged = InventoryLE.appendHoverText(stack, level, list, flag, charge);
		double damage = 0;

		if(charged && charge.getCharge() > 0) {
			list.add(Component.literal(LCText.getAutoRepair2Lore()));
			damage = Math.min(charge.getCharge(), attackDamage);
		}

		list.add(Component.literal(""));
		if(!Screen.hasShiftDown()) {
			list.add(Component.literal(LCText.secSign + "9+" + LCText.af.format(damage) + " Attack Damage"));
		} else {
			list.add(Component.literal(LCText.secSign + "3+" + LCText.af.format(charge.getCharge()) + " Attack Damage (Sneaking)"));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Rarity getRarity(ItemStack stack) {
		return ILERarity;
	}

}
