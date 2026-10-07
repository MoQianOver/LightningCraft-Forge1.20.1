package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.IKineticGear;
import org.lightningcraft.items.base.ItemArmorLC;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.util.ISimpleLEUser;
import org.lightningcraft.util.InventoryLE;
import org.lightningcraft.util.InventoryLE.LECharge;

public class ItemKineticArmor extends ItemArmorLC implements ISimpleLEUser, IKineticGear {

	public static final double protectionCost = 0.5;
	public static final double protectionDamper = 5.0;
	public static final double soulCost = 3.0;

	public ItemKineticArmor(ArmorMaterial mat, EquipmentSlot armorType) {
		super(mat, armorType);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		LECharge charge = new LECharge();
		boolean charged = InventoryLE.appendHoverText(stack, level, list, flag, charge);

		if(charged && charge.getCharge() > 0) {
			list.add(Component.literal(LCText.getAutoRepair2Lore()));
			list.add(Component.literal(LCText.getInvincibilityLore()));
		}
	}

    @Override
	public boolean isValidRepairItem(ItemStack a, ItemStack b) {
    	return false;
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public Rarity getRarity(ItemStack stack) {
		return ILERarity;
	}

}
