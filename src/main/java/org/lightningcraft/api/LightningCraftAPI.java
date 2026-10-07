package org.lightningcraft.api;

import java.lang.reflect.Method;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.lightningcraft.api.recipes.LightningCrusherRecipe;
import org.lightningcraft.api.recipes.LightningInfusionRecipe;

public class LightningCraftAPI {

	public static final String MODID = "lightningcraft";
	public static final String NAME = "LightningCraft";

	public static boolean addLightningCrusherRecipe(LightningCrusherRecipe recipe) {
		try {
			Class crusherContainer = Class.forName("org.lightningcraft.recipes.LightningCrusherRecipes");
			Method crusherInstance = crusherContainer.getMethod("instance");
			Method recipeAdder = crusherContainer.getMethod("addRecipe", LightningCrusherRecipe.class);
			recipeAdder.invoke(crusherInstance.invoke(null), recipe);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public static boolean addLightningInfusionRecipe(LightningInfusionRecipe recipe) {
		try {
			Class infusionContainer = Class.forName("org.lightningcraft.recipes.LightningInfusionRecipes");
			Method infusionInstance = infusionContainer.getMethod("instance");
			Method recipeAdder = infusionContainer.getMethod("addRecipe", LightningInfusionRecipe.class);
			recipeAdder.invoke(infusionInstance.invoke(null), recipe);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

}
