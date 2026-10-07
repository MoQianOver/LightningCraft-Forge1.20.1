package org.lightningcraft.api.recipes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.util.LCOreDictionary;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.api.util.StackHelper;

public class LightningCrusherRecipe {
	private Object in;
	private List<ItemStack> excluding;
	private List<ItemStack> inCache;
	private ItemStack out;

	public LightningCrusherRecipe(ItemStack out, Object in, ItemStack... excluding) {
		if(!(in instanceof List || in instanceof ItemStack || in instanceof String)) {
			throw new IllegalArgumentException("Crusher recipe input invalid.");
		}
		this.in = in;
		this.excluding = new JointList();
		if(excluding != null) {
			for(ItemStack s : excluding) this.excluding.add(s.copy());
		}
		this.inCache = null;
		this.out = out;
	}

	public Object getInputRaw() {
		return in;
	}

	public List<ItemStack> getInputBase() {
		if(in instanceof List) {
			return (List<ItemStack>)in;
		} else if(in instanceof ItemStack) {
			return new JointList().join(((ItemStack)in).copy());
		} else {
			if(LCOreDictionary.doesOreNameExist((String)in)) {
				return LCOreDictionary.getOres((String)in);
			} else {
				return new JointList();
			}
		}
	}

	public List<ItemStack> getInput() {
		if(inCache == null) {
			List<ItemStack> base = new ArrayList(getInputBase());
			for(ItemStack ex : excluding) {
				Iterator<ItemStack> iterator = base.iterator();
				while(iterator.hasNext()) {
					ItemStack in = iterator.next();
					if(StackHelper.areItemStacksEqualForCrafting(in, ex)) {
						iterator.remove();
					}
				}
			}
			return (inCache = base);
		} else {
			return inCache;
		}
	}

	public ItemStack getOutput() {
		return out.copy();
	}

	public List<ItemStack> getExcluding() {
		return excluding;
	}

}
