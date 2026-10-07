package org.lightningcraft.crafting;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.lightningcraft.api.util.JointList;

public class RecipeHelper {

	private static final List<Recipe<?>> RECIPES = new JointList<>();
	private static int recipeCounter = 0;

	public static void addRecipe(Recipe<?> recipe) {

		RECIPES.add(recipe);
		recipeCounter++;
	}

	public static void addShapedRecipe(ItemStack result, Object... recipe) {

		recipeCounter++;
	}

	public static void addShapelessRecipe(ItemStack result, Object... recipe) {

		recipeCounter++;
	}

	public static int getRecipeCount() {
		return RECIPES.size();
	}

}
