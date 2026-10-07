package org.lightningcraft.items.base;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class ItemAxeNotStupid extends AxeItem {

	protected final float attackSpeed;

	public ItemAxeNotStupid(Tier mat) {
		this(mat, 6.0F, -3.2F);
	}

	public ItemAxeNotStupid(Tier mat, float damageModifier, float speedModifier) {
		super(mat, damageModifier, speedModifier, new Item.Properties());
		this.attackSpeed = speedModifier;
	}

}
