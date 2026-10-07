package org.lightningcraft.items.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.registry.ILightningCraftItem;

public class ItemLC extends Item implements ILightningCraftItem {

	private final Rarity rarity;

	public ItemLC(Rarity rarity) {
		this(rarity, new Item.Properties());
	}

	public ItemLC(Rarity rarity, Item.Properties properties) {
		super(properties);
		this.rarity = rarity;
	}

	public ItemLC() {
		this(Rarity.COMMON);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Rarity getRarity(ItemStack stack) {
		return rarity;
	}

}
