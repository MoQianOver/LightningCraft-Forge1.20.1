package org.lightningcraft.items.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.creativetabs.LCCreativeTab;
import org.lightningcraft.util.LCStack;

public class ItemMeta extends ItemLC {

	public final int nSubItems;
	public boolean sameIcon;
	public boolean[] hasEffect;

	public ItemMeta(int nSubItems, boolean sameIcon, boolean... hasEffect) {
		super();
		this.nSubItems = nSubItems;
		this.sameIcon = sameIcon;
		this.hasEffect = hasEffect;
	}

	public ItemMeta(int nSubItems) {
		this(nSubItems, false, false);
	}

	public ItemMeta(int nSubItems, Rarity rarity) {
		super(rarity);
		this.nSubItems = nSubItems;
		this.sameIcon = false;
	}

	public ItemMeta(int nSubItems, Rarity rarity, Item.Properties properties) {
		super(rarity, properties);
		this.nSubItems = nSubItems;
		this.sameIcon = false;
	}

	public ItemMeta(int nSubItems, boolean sameIcon, boolean[] hasEffect, Item.Properties properties) {
		super(Rarity.COMMON, properties);
		this.nSubItems = nSubItems;
		this.sameIcon = sameIcon;
		this.hasEffect = hasEffect;
	}

	public void addCreativeTabEntries() {
		for (int meta = 0; meta < nSubItems; meta++) {
			LCCreativeTab.addItemEntry(LCStack.of(this, 1, meta));
		}
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return super.getDescriptionId(stack) + "_" + stack.getDamageValue();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isFoil(ItemStack stack) {
		int index = stack.getDamageValue();
		if (hasEffect != null) {
			return hasEffect[Math.min(index, hasEffect.length - 1)];
		}
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerRender() {

	}

}
