package org.lightningcraft.integration.jei.infusion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.recipes.LightningInfusionRecipe;
import org.lightningcraft.api.util.LCOreDictionary;
import org.lightningcraft.api.util.StackHelper;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.tiles.TileEntityLightningInfuser;

public class LightningInfusionRecipeCategory implements IRecipeCategory<LightningInfusionRecipe> {

	public static final RecipeType<LightningInfusionRecipe> TYPE =
			RecipeType.create("lightningcraft", "infusion", LightningInfusionRecipe.class);

	private static final ResourceLocation TEX =
			new ResourceLocation("lightningcraft", "textures/gui/container/lpinfuser.png");

	private static final int INFUSE_X = 25, INFUSE_Y = 20;
	private static final int[][] SURROUND = { { 25, 0 }, { 45, 20 }, { 25, 40 }, { 5, 20 } };
	private static final int OUT_X = 102, OUT_Y = 15;

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawableAnimated arrow;

	public LightningInfusionRecipeCategory(IGuiHelper gui) {
		this.background = gui.createDrawable(TEX, 8, 20, 128, 60);
		this.icon = gui.createDrawableItemStack(new ItemStack(LCBlocks.lightningInfuser));
		IDrawableStatic arrowTex = gui.createDrawable(TEX, 176, 14, 24, 17);
		this.arrow = gui.createAnimatedDrawable(arrowTex, TileEntityLightningInfuser.burnTime,
				IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public RecipeType<LightningInfusionRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("gui.lightningcraft.infusion");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	private static List<ItemStack> resolve(String key) {
		if (key == null || key.isEmpty() || LightningInfusionRecipe.nullIdentifier.equals(key)) {
			return Collections.emptyList();
		}
		if (LCOreDictionary.doesOreNameExist(key)) {
			List<ItemStack> list = new ArrayList<>();
			for (ItemStack s : LCOreDictionary.getOres(key)) {
				if (!s.isEmpty()) list.add(s);
			}
			if (!list.isEmpty()) return list;
		}
		List<ItemStack> list = new ArrayList<>();
		try {
			ItemStack s = StackHelper.makeItemStackFromString(key, 0);
			if (!s.isEmpty()) list.add(s);
		} catch (Exception ignored) {

		}
		return list;
	}

	private static List<ItemStack> resolveInfuse(LightningInfusionRecipe recipe) {
		List<String> ores = recipe.getInfuseItemAsOre();
		if (ores != null && !ores.isEmpty()) {
			List<ItemStack> list = new ArrayList<>();
			for (String s : ores) {
				ItemStack st = StackHelper.makeItemStackFromString(s, 0);
				if (!st.isEmpty()) list.add(st);
			}
			if (!list.isEmpty()) return list;
		}
		return resolve(recipe.getInfuseItem());
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, LightningInfusionRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> infuse = resolveInfuse(recipe);
		if (!infuse.isEmpty()) {
			builder.addSlot(RecipeIngredientRole.INPUT, INFUSE_X, INFUSE_Y).addItemStacks(infuse);
		}
		List<String> items = recipe.getItems();
		for (int i = 0; i < SURROUND.length && i < items.size(); i++) {
			List<ItemStack> list = resolve(items.get(i));
			if (!list.isEmpty()) {
				builder.addSlot(RecipeIngredientRole.INPUT, SURROUND[i][0], SURROUND[i][1]).addItemStacks(list);
			}
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, OUT_X, OUT_Y).addItemStack(recipe.getOutput());
	}

	@Override
	public void draw(LightningInfusionRecipe recipe, IRecipeSlotsView slots, GuiGraphics g,
			double mouseX, double mouseY) {
		arrow.draw(g, 68, 20);
		Component cost = Component.literal(recipe.getCost() + " LE");
		g.drawString(Minecraft.getInstance().font, cost, 6, 52, 0x808080, false);
	}
}
