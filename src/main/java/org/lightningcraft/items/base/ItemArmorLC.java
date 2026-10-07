package org.lightningcraft.items.base;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.registry.ILightningCraftItem;
import org.lightningcraft.api.util.StackHelper;
import org.lightningcraft.util.ArmorHelper;
import org.lightningcraft.util.LCMisc;

public abstract class ItemArmorLC extends ArmorItem implements ILightningCraftItem {

	private final Rarity rarity;
	private final ArmorMaterial mat;

	public ItemArmorLC(ArmorMaterial mat, EquipmentSlot armorType, Rarity rarity) {
		super(mat, ArmorHelper.toArmorType(armorType), new Item.Properties());
		this.mat = mat;
		this.rarity = rarity;
	}

	public ItemArmorLC(ArmorMaterial mat, EquipmentSlot armorType) {
		this(mat, armorType, DYNAMIC);
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		ItemStack matStack = ArmorHelper.getRepairStack(mat);
		return !matStack.isEmpty() && StackHelper.areItemStacksEqualForCrafting(repair, matStack);
	}

	@Override
	public void setRarity() {}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Rarity getRarity(ItemStack stack) {
		if (rarity != DYNAMIC) return rarity;
		return LCMisc.getRarityFromStack(ArmorHelper.getRepairStack(mat));
	}

}
