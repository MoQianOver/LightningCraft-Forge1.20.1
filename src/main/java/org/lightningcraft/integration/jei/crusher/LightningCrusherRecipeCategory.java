package org.lightningcraft.integration.jei.crusher;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.recipes.LightningCrusherRecipe;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.tiles.TileEntityLightningCrusher;

public class LightningCrusherRecipeCategory implements IRecipeCategory<LightningCrusherRecipe> {

	public static final RecipeType<LightningCrusherRecipe> TYPE =
			RecipeType.create("lightningcraft", "crusher", LightningCrusherRecipe.class);

	private static final ResourceLocation TEX =
			new ResourceLocation("lightningcraft", "textures/gui/container/lpfurnace.png");

	private static final int INPUT_X = 0, INPUT_Y = 0;
	private static final int OUT_X = 60, OUT_Y = 18;

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawableAnimated flame;
	private final IDrawableAnimated arrow;

	public LightningCrusherRecipeCategory(IGuiHelper gui) {
		this.background = gui.createDrawable(TEX, 55, 16, 82, 54);
		this.icon = gui.createDrawableItemStack(new ItemStack(LCBlocks.lightningCrusher));
		IDrawableStatic flameTex = gui.createDrawable(TEX, 176, 0, 14, 14);
		this.flame = gui.createAnimatedDrawable(flameTex, TileEntityLightningCrusher.burnTime,
				IDrawableAnimated.StartDirection.TOP, true);
		IDrawableStatic arrowTex = gui.createDrawable(TEX, 176, 14, 24, 17);
		this.arrow = gui.createAnimatedDrawable(arrowTex, TileEntityLightningCrusher.burnTime / 2,
				IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public RecipeType<LightningCrusherRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("gui.lightningcraft.crusher");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, LightningCrusherRecipe recipe, IFocusGroup focuses) {
		if (!recipe.getInput().isEmpty()) {
			builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, INPUT_Y).addItemStacks(recipe.getInput());
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, OUT_X, OUT_Y).addItemStack(recipe.getOutput());
	}

	@Override
	public void draw(LightningCrusherRecipe recipe, IRecipeSlotsView slots, GuiGraphics g,
			double mouseX, double mouseY) {
		flame.draw(g, 2, 20);
		arrow.draw(g, 24, 18);
	}
}
