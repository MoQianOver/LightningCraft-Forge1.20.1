package org.lightningcraft.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.integration.jei.crusher.LightningCrusherRecipeCategory;
import org.lightningcraft.integration.jei.infusion.LightningInfusionRecipeCategory;
import org.lightningcraft.recipes.LightningCrusherRecipes;
import org.lightningcraft.recipes.LightningInfusionRecipes;
import org.lightningcraft.ref.Log;
import org.lightningcraft.ref.Metal.Ingot;
import org.lightningcraft.util.LCStack;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

	private static final ResourceLocation UID =
			new ResourceLocation("lightningcraft", "jei_plugin");

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration reg) {
		if (!LCConfig.JEIIntegration) {
			Log.logger.info("JEI integration disabled.");
			return;
		}
		var gui = reg.getJeiHelpers().getGuiHelper();
		reg.addRecipeCategories(
				new LightningInfusionRecipeCategory(gui),
				new LightningCrusherRecipeCategory(gui)
		);
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg) {
		if (!LCConfig.JEIIntegration) {
			return;
		}
		reg.addRecipes(LightningInfusionRecipeCategory.TYPE,
				LightningInfusionRecipes.instance().getRecipeList());
		reg.addRecipes(LightningCrusherRecipeCategory.TYPE,
				LightningCrusherRecipes.instance().getRecipeList());

		reg.addIngredientInfo(LCStack.of(LCItems.ingot, 1, Ingot.ELEC),
				mezz.jei.api.constants.VanillaTypes.ITEM_STACK, net.minecraft.network.chat.Component.translatable("jei.lightningcraft.electricium_info"));
		reg.addIngredientInfo(LCStack.of(LCItems.ingot, 1, Ingot.SKY),
				mezz.jei.api.constants.VanillaTypes.ITEM_STACK, net.minecraft.network.chat.Component.translatable("jei.lightningcraft.skyfather_info"));
		reg.addIngredientInfo(LCStack.of(LCItems.ingot, 1, Ingot.MYSTIC),
				mezz.jei.api.constants.VanillaTypes.ITEM_STACK, net.minecraft.network.chat.Component.translatable("jei.lightningcraft.mystic_info"));

		Log.logger.info("JEI integration complete.");
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
		if (!LCConfig.JEIIntegration) {
			return;
		}
		reg.addRecipeCatalyst(new ItemStack(LCBlocks.lightningInfuser),
				LightningInfusionRecipeCategory.TYPE);
		reg.addRecipeCatalyst(new ItemStack(LCBlocks.lightningCrusher),
				LightningCrusherRecipeCategory.TYPE);
		reg.addRecipeCatalyst(new ItemStack(LCBlocks.lightningFurnace),
				mezz.jei.api.constants.RecipeTypes.SMELTING);
	}
}
