package org.lightningcraft.recipes;

import org.lightningcraft.util.LCStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.ref.Metal.Ingot;

public class LightningTransformRecipes {

	private static LightningTransformRecipes instance = new LightningTransformRecipes();
	private Map<List<ItemStack>, ItemStack> recipeList;

	public static LightningTransformRecipes instance() {
		return instance;
	}

	private LightningTransformRecipes() {
		this.addDefaultRecipes();
	}

	private void addDefaultRecipes() {
		recipeList = new HashMap();

		this.addRecipe(LCStack.of(LCItems.ingot, 1, Ingot.ELEC),
				new JointList().join(new ItemStack(Items.IRON_INGOT)).join(new ItemStack(Items.GOLD_INGOT)).join(new ItemStack(Items.DIAMOND)));
	}

	public void addRecipe(@Nonnull ItemStack output, List<ItemStack> input) {
		recipeList.put(input, output);
	}

	public @Nonnull ItemStack getTransformResult(List<ItemStack> input) {
		int matches = 0;
		List comp = null;
		ItemStack result = ItemStack.EMPTY;
		for(Entry<List<ItemStack>, ItemStack> entry : recipeList.entrySet()) {
			for(ItemStack rIn : entry.getKey()) {
				for(ItemStack iIn : input) {
					if(ItemStack.isSameItemSameTags(rIn, iIn)) {
						matches++;
						result = entry.getValue();
						comp = entry.getKey();
					}
				}
			}
		}
		if(matches == input.size() && matches == comp.size()) {
			return result.copy();
		} else {
			return ItemStack.EMPTY;
		}
	}

}
