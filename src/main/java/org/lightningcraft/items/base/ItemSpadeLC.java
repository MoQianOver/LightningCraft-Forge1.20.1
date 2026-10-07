package org.lightningcraft.items.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.registry.ILightningCraftItem;
import org.lightningcraft.api.util.LCOreDictionary;
import org.lightningcraft.util.LCMisc;
import org.lightningcraft.util.ToolHelper;

public class ItemSpadeLC extends ShovelItem implements ILightningCraftItem {

	private final Rarity rarity;
	private final Tier mat;

	protected final float attackSpeed;

	public ItemSpadeLC(Tier mat, float damageModifier, float speedModifier, Rarity rarity) {
		super(mat, damageModifier, speedModifier, new Item.Properties());
		this.attackSpeed = speedModifier;
		this.mat = mat;
		this.rarity = rarity;
	}

	public ItemSpadeLC(Tier mat, Rarity rarity) {
		this(mat, 1.5F, -3.0F, rarity);
	}

	public ItemSpadeLC(Tier mat) {
		this(mat, DYNAMIC);
	}

	@Override
	public void setRarity() {}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Rarity getRarity(ItemStack stack) {
		if (rarity != DYNAMIC) return rarity;
		return LCMisc.getRarityFromStack(ToolHelper.getRepairStack(mat));
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		ItemStack matStack = ToolHelper.getRepairStack(mat);
		return !matStack.isEmpty() && LCOreDictionary.itemMatches(matStack, repair, false);
	}

}
