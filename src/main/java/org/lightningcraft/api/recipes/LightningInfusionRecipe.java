package org.lightningcraft.api.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.util.LCOreDictionary;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.api.util.StackHelper;

public class LightningInfusionRecipe {

	public static final String nullIdentifier = "NULL";
	public static final int itemsNeeded = 4;

	private final ItemStack output;
	private final String infuse;
	private final List<String> items;
	private final int cost;
	private boolean nbtSensitive;

	public LightningInfusionRecipe(ItemStack output, int cost, Object infuse, Object... surrounding) {
		List items = new ArrayList(Arrays.asList(surrounding));

		if(items.size() > itemsNeeded) {
			throw new IllegalArgumentException("There must be at most " + itemsNeeded + " surrounding items in an infusion recipe.");
		}
		while(items.size() < itemsNeeded) {
			items.add(null);
		}

		this.output = output;
		this.cost = cost;

		if(infuse == null) {
			throw new IllegalArgumentException("The infused item cannot be null!");
		} else if(infuse instanceof Block) {
			this.infuse = StackHelper.makeStringFromItemStack(new ItemStack((Block)infuse));
		} else if(infuse instanceof Item) {
			this.infuse = StackHelper.makeStringFromItemStack(new ItemStack((Item)infuse));
		} else {
			this.infuse = StackHelper.makeStringFromItemStack(infuse);
		}

		this.items = new JointList();
		for(Object o : items) {
			if(o == null) {
				this.items.add(nullIdentifier);
			} else if(o instanceof Block) {
				this.items.add(StackHelper.makeStringFromItemStack(new ItemStack((Block)o)));
			} else if(o instanceof Item) {
				this.items.add(StackHelper.makeStringFromItemStack(new ItemStack((Item)o)));
			} else {
				this.items.add(StackHelper.makeStringFromItemStack(o));
			}
		}
	}

	public ItemStack getOutput() {
		return output;
	}

	public int getCost() {
		return cost;
	}

	public String getInfuseItem() {
		return infuse;
	}

	public List<String> getInfuseItemAsOre() {
		JointList<String> list = new JointList();
		if(LCOreDictionary.doesOreNameExist(infuse)) {
			for(ItemStack s : LCOreDictionary.getOres(infuse)) {
				list.add(StackHelper.makeStringFromItemStack(s));
			}
		}
		return list;
	}

	public List<String> getItems() {
		return items;
	}

	public List<List<String>> getItemsAsOres() {
		JointList<List<String>> list = new JointList();
		for(String name : items) {
			JointList<String> list2 = new JointList();
			if(LCOreDictionary.doesOreNameExist(name)) {
				for(ItemStack s : LCOreDictionary.getOres(name)) {
					list2.add(StackHelper.makeStringFromItemStack(s));
				}
			}
			list.add(list2);
		}
		return list;
	}

	public LightningInfusionRecipe setNBTSensitive() {
		this.nbtSensitive = true;
		return this;
	}

	public boolean getNBTSensitive() {
		return this.nbtSensitive;
	}

}
