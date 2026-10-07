package org.lightningcraft.gui.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.recipes.LightningInfusionRecipe;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.api.util.StackHelper;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.integration.energy.EnergyApiHelper;
import org.lightningcraft.recipes.LightningInfusionRecipes;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.tiles.TileEntityEnergyProvider;
import org.lightningcraft.tiles.TileEntityEnergyReceiver;
import org.lightningcraft.util.LCMisc;

@OnlyIn(Dist.CLIENT)
public class GuiLightningGuide extends Screen {
	private final int bookImageHeight = 192;
	private final int bookImageWidth = 192;
	private int currPage = 0;
	private int indexPage, recipePage;
	private int bookTotalPages;
	private static ResourceLocation bookCoverTexture = new ResourceLocation(RefStrings.MODID, "textures/gui/lightning_book_cover.png");
	private static ResourceLocation bookMainTexture = new ResourceLocation(RefStrings.MODID, "textures/gui/lightning_book.png");
	private static ResourceLocation bookRecipeTexture = new ResourceLocation(RefStrings.MODID, "textures/gui/lightning_book_recipes.png");
	private JointList<String> pages = new JointList<String>();
	private JointList<RecipePage> recipePages = new JointList<RecipePage>();

	private Button buttonDone;
	private NextPageButton buttonNextPage, buttonLastPage;
	private NextPageButton buttonPreviousPage, buttonFirstPage;
	private Button level1Ref; private int pageLevel1 = -1;
	private Button level2Ref; private int pageLevel2 = -1;
	private Button level3Ref; private int pageLevel3 = -1;
	private Button indexRef;
	private int level;

	private net.minecraft.client.gui.GuiGraphics currentGraphics;
	private int mouseX;
	private int mouseY;

	public GuiLightningGuide() {
		this(0);
	}

	public GuiLightningGuide(int level)
	{
		super(net.minecraft.network.chat.Component.translatable("item.lightningcraft.guide_0"));

		String edition = "";
		String S = LCText.secSign;
		switch(level) {
		case 0:
			break;
		case 1:
			edition = S + "4" + S + "l" + I18n.get("guide.lightningcraft.edition_skyfather");
			break;
		case 2:
			edition = S + "3" + S + "l" + I18n.get("guide.lightningcraft.edition_underworld");
			break;
		}

		java.util.List<String> pageKeys = new java.util.ArrayList<>();
		pageKeys.add("cover");
		pageKeys.add("intro1");
		pageKeys.add("intro2");
		pageKeys.add("air_terminals");
		pageKeys.add("cells");
		pageKeys.add("furnace");
		pageKeys.add("crusher");
		pageKeys.add("infuser");
		pageKeys.add("breaker");
		pageKeys.add("miner");
		pageKeys.add("generator");
		if (EnergyApiHelper.rfOrTeslaLoaded) {
			pageKeys.add("rf_export");
			pageKeys.add("rf_generator");
		}
		pageKeys.add("wireless");
		pageKeys.add("wireless2");
		pageKeys.add("reallocator");
		pageKeys.add("items_intro");
		pageKeys.add("thunderstone");
		pageKeys.add("electricium");
		pageKeys.add("useful_items");
		pageKeys.add("tools_intro");
		pageKeys.add("elec_tools");
		pageKeys.add("elec_weapons");
		pageKeys.add("elec_armor");
		pageKeys.add("stonebound");
		pageKeys.add("mobile_le");
		pageKeys.add("mobile_le2");
		pageKeys.add("kinetics");
		pageKeys.add("kinetics2");
		pageKeys.add("kinetics3");

		if (level <= 0) {
			pageKeys.add("mind_turns");
			pageKeys.add("what_if");
			pageKeys.add("skyfather1");
			pageKeys.add("skyfather2");
			pageKeys.add("skyfather4");
		} else {
			pageKeys.add("@LEVEL1");
			pageKeys.add("skyfather1");
			pageKeys.add("skyfather2");
			if (level == 1) pageKeys.add("skyfather3");
			pageKeys.add("skyfather4");
		}

		if (level > 1) {
			pageKeys.add("@LEVEL2");
			pageKeys.add("underworld1");
			pageKeys.add("structures_intro");
			pageKeys.add("water_temples");
			pageKeys.add("towers");
			pageKeys.add("ramparts");
			pageKeys.add("mobs");
			pageKeys.add("warding1");
			pageKeys.add("warding2");
			pageKeys.add("warding3");
		}

		pages.add(I18n.get("guide.lightningcraft.cover") + edition);
		this.indexPage = pages.size();
		pages.add(LCText.secSign + "l" + I18n.get("guide.lightningcraft.index") + LCText.secSign + "r\n"
				+ I18n.get("guide.lightningcraft.index_page"));

		for (String key : pageKeys) {
			if ("cover".equals(key)) continue;
			if ("@LEVEL1".equals(key)) { pageLevel1 = pages.size(); continue; }
			if ("@LEVEL2".equals(key)) { pageLevel2 = pages.size(); continue; }
			pages.add(I18n.get("guide.lightningcraft." + key));
		}
		pageLevel3 = pages.size();

		addRecipePages();

		this.bookTotalPages = pages.size();
		this.level = level;
	}

	protected void addRecipePages() {
		this.recipePage = pages.size();
		for(LightningInfusionRecipe recipe : LightningInfusionRecipes.instance().getRecipeList()) {
			ItemStack result = recipe.getOutput();
			List<String> items = recipe.getItems();
			JointList<String> ingredients = new JointList().join(items);
			ingredients.addFirst(recipe.getInfuseItem());
			int cost = recipe.getCost();

			pages.add("");
			recipePages.add(new RecipePage(this, result, ingredients, cost));
		}
	}

	protected void addIndex() {
		String S = LCText.secSign;
		int i = 0;

		this.indexPage = pages.size();
		pages.add(S + "l" + I18n.get("guide.lightningcraft.index") + S + "r\n" + I18n.get("guide.lightningcraft.index_page"));

		this.recipePage = pages.size();
		JointList<ItemStack> results = new JointList();
		JointList<Integer> costs = new JointList();

		for(LightningInfusionRecipe recipe : LightningInfusionRecipes.instance().getRecipeList()) {
			ItemStack result = recipe.getOutput();
			List<String> items = recipe.getItems();
			JointList<String> ingredients = new JointList().join(items);
			ingredients.addFirst(recipe.getInfuseItem());
			int cost = recipe.getCost();

			pages.add("");
			recipePages.add(new RecipePage(this, result, ingredients, cost));
		}
	}

	@Override
	public void init()
	{
		this.clearWidgets();

		buttonDone = Button.builder(net.minecraft.network.chat.Component.literal(I18n.get("gui.done")), b -> this.buttonPressed(b)).bounds(width / 2 - 49, 4 + bookImageHeight + (level + 1) * 24, 98, 20).build();
		level1Ref = Button.builder(net.minecraft.network.chat.Component.translatable("guide.lightningcraft.ref_skyfather"), b -> this.buttonPressed(b)).bounds(width / 2 - 80, 4 + bookImageHeight, 160, 20).build();
		level2Ref = Button.builder(net.minecraft.network.chat.Component.translatable("guide.lightningcraft.ref_underworld"), b -> this.buttonPressed(b)).bounds(width / 2 - 80, 28 + bookImageHeight, 160, 20).build();
		level3Ref = Button.builder(net.minecraft.network.chat.Component.translatable("guide.lightningcraft.ref_transcendental"), b -> this.buttonPressed(b)).bounds(width / 2 - 80, 52 + bookImageHeight, 160, 20).build();
		indexRef = Button.builder(net.minecraft.network.chat.Component.translatable("guide.lightningcraft.index"), b -> this.buttonPressed(b)).bounds(width / 2 - 49, 4 + bookImageHeight + level * 24, 98, 20).build();

		this.addRenderableWidget(buttonDone);
		if(level > 0) this.addRenderableWidget(level1Ref);
		if(level > 1) this.addRenderableWidget(level2Ref);
		if(level > 2) this.addRenderableWidget(level3Ref);
		this.addRenderableWidget(indexRef);
		int offsetFromScreenLeft = (width - bookImageWidth) / 2;
		int inOff = 20;
		this.addRenderableWidget(buttonNextPage = new NextPageButton(this, 0, offsetFromScreenLeft + 120 - inOff, 156, true, false));
		this.addRenderableWidget(buttonPreviousPage = new NextPageButton(this, 1, offsetFromScreenLeft + 38 + inOff, 156, false, false));
		this.addRenderableWidget(buttonLastPage = new NextPageButton(this, 2, offsetFromScreenLeft + 145 - inOff, 156, true, true));
		this.addRenderableWidget(buttonFirstPage = new NextPageButton(this, 3, offsetFromScreenLeft + 13 + inOff, 156, false, true));
	}

	@Override
	public void tick()
	{
		buttonDone.visible = true;
		buttonNextPage.visible = (currPage < bookTotalPages - 1);
		buttonPreviousPage.visible = currPage > 0;
	}

	@Override
	public void render(net.minecraft.client.gui.GuiGraphics guiGraphics, int mouseX, int mouseY, float p_73863_3_)
	{
		this.currentGraphics = guiGraphics;
		this.mouseX = mouseX;
		this.mouseY = mouseY;

		Minecraft minecraft = Minecraft.getInstance();
		if (currPage == 0) {
		}
		else if(currPage < recipePage) {
		}
		else {
		}

		int offsetFromScreenLeft = (width - bookImageWidth ) / 2;
		ResourceLocation bookBg = (currPage == 0) ? bookCoverTexture
				: (currPage >= recipePage ? bookRecipeTexture : bookMainTexture);
		guiGraphics.blit(bookBg, offsetFromScreenLeft, 2, 0, 0, bookImageWidth, bookImageHeight);

		int widthOfString;
		String stringPageIndicator = I18n.get("book.pageIndicator", Integer.valueOf(currPage + 1), bookTotalPages);
		widthOfString = font.width(stringPageIndicator);
		guiGraphics.drawString(font, stringPageIndicator, offsetFromScreenLeft - widthOfString + bookImageWidth - 44, 18, 0);
		guiGraphics.drawWordWrap(font, net.minecraft.network.chat.Component.literal(
				(currPage >= 0 && currPage < pages.size()) ? pages.get(currPage) : ""), offsetFromScreenLeft + 36, 34, 116, 0);

		super.render(guiGraphics, mouseX, mouseY, p_73863_3_);

		int rp = currPage - recipePage;
		if(rp >= 0 && rp < recipePages.size()) {
			recipePages.get(rp).drawPage(guiGraphics, offsetFromScreenLeft, 2);
		}
	}

	public void buttonPressed(Button parButton)
	{
		if (parButton == buttonDone)
		{

			minecraft.setScreen(null);
		}
		else if (parButton == buttonNextPage)
		{
			if (currPage < bookTotalPages - 1)
			{
				++currPage;
			}
		}
		else if (parButton == buttonPreviousPage)
		{
			if (currPage > 0)
			{
				--currPage;
			}
		}
		else if (parButton == buttonFirstPage)
		{
			currPage = 0;
		}
		else if (parButton == buttonLastPage)
		{
			currPage = bookTotalPages - 1;
		}
		else if (parButton == level1Ref)
		{
			currPage = pageLevel1;
		}
		else if (parButton == level2Ref)
		{
			currPage = pageLevel2;
		}
		else if (parButton == level3Ref)
		{
			currPage = pageLevel3;
		}
		else if (parButton == indexRef) {
			currPage = indexPage;
		}

	}

	@Override
	public void onClose()
	{

	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	protected void drawItemStack(net.minecraft.client.gui.GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
		if(stack != null && !stack.isEmpty()) {
			guiGraphics.renderItem(stack, x, y);
			guiGraphics.renderItemDecorations(this.font, stack, x, y);
		}
	}

	protected void renderToolTip(net.minecraft.client.gui.GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
		if(stack != null && !stack.isEmpty()) {
			guiGraphics.renderTooltip(this.font, stack, x, y);
		}
	}

	@OnlyIn(Dist.CLIENT)
	static class RecipePage {

		private static final Random rand = new Random();
		private static final int maxMetaTicks = 1;
		private static final int maxRecTicks = 50;
		private double metaTicks = maxMetaTicks + 1;
		private double recTicks = maxRecTicks + 1;
		private long lasttime;
		private GuiLightningGuide book;
		private ItemStack result;
		private List<String> ingredients;
		private int cost;
		private boolean ignoreDamage;
		private int[] itemRotate;
		private ItemStack[] displayStack;
		private int dispDamage = 0;
		private AABB resultBox;
		private JointList<AABB> ingredientBoxes;

		public RecipePage(GuiLightningGuide book, ItemStack result, List<String> ingredients, int cost) {
			this.book = book;
			this.result = result.copy();
			this.ingredients = ingredients;
			this.cost = cost;
			this.ignoreDamage = false;
			this.lasttime = net.minecraft.Util.getMillis();
		}

		public void drawPage(net.minecraft.client.gui.GuiGraphics guiGraphics, int bookX, int bookY) {

			book.currentGraphics.drawWordWrap(book.font,
					net.minecraft.network.chat.Component.translatable("guide.lightningcraft.le_cost", cost),
					bookX + 36, 120, 116, 0);

			int xoff = 3;
			int yoff = -16;
			bookX += xoff;
			bookY += yoff;

			if(ignoreDamage && metaTicks >= maxMetaTicks) {
				dispDamage++;
				if(dispDamage > result.getMaxDamage()) {
					dispDamage = Math.min(1, result.getMaxDamage());
				}
				metaTicks = 0;
			}
			metaTicks += (net.minecraft.Util.getMillis() - lasttime) / 50D;

			resultBox = new AABB(bookX + 120, bookY + 89, 0, bookX + 120 + 16, bookY + 89 + 16, 0);
			if(result.getMaxDamage() > 0) result.setDamageValue(dispDamage);
			book.drawItemStack(book.currentGraphics, result, (int)resultBox.minX, (int)resultBox.minY);

			ingredientBoxes = new JointList<AABB>();
			ingredientBoxes.add(new AABB(bookX + 59, bookY + 89,  0, bookX + 59 + 16, bookY + 89 + 16,  0));
			ingredientBoxes.add(new AABB(bookX + 59, bookY + 69,  0, bookX + 59 + 16, bookY + 69 + 16,  0));
			ingredientBoxes.add(new AABB(bookX + 79, bookY + 89,  0, bookX + 79 + 16, bookY + 89 + 16,  0));
			ingredientBoxes.add(new AABB(bookX + 59, bookY + 109, 0, bookX + 59 + 16, bookY + 109 + 16, 0));
			ingredientBoxes.add(new AABB(bookX + 39, bookY + 89,  0, bookX + 39 + 16, bookY + 89 + 16,  0));

			boolean change = recTicks >= maxRecTicks;
			Integer[] shuffle = new Integer[]{1, 2, 3, 4};
			if(change) {
				Collections.shuffle(Arrays.asList(shuffle));
				itemRotate = new int[ingredientBoxes.size()];
			}

			displayStack = new ItemStack[ingredientBoxes.size()];
			for(int i = 0; i < ingredientBoxes.size(); i++) {
				if(change) {
					if(i > 0) {
						itemRotate[i] = shuffle[i - 1];
					} else {
						itemRotate[i] = 0;
					}
					recTicks = 0;
				}
				ItemStack itemStack = StackHelper.animateItemStackFromString(ingredients.get(itemRotate[i]), change);
				if(itemStack.isEmpty()) continue;
				displayStack[i] = itemStack;
				if(displayStack[i].getMaxDamage() > 0) displayStack[i].setDamageValue(Math.min(displayStack[i].getMaxDamage(), dispDamage));
				book.drawItemStack(book.currentGraphics, displayStack[i], (int)ingredientBoxes.get(i).minX, (int)ingredientBoxes.get(i).minY);
			}
			recTicks += (net.minecraft.Util.getMillis() - lasttime) / 50D;
			lasttime = net.minecraft.Util.getMillis();

			drawTooltips();
		}

		public void drawTooltips() {

			if(LCMisc.mouseColl(book.mouseX, book.mouseY, resultBox)) book.renderToolTip(book.currentGraphics, result, book.mouseX, book.mouseY);

			for(int i = 0; i < ingredientBoxes.size(); i++) {
				if(LCMisc.mouseColl(book.mouseX, book.mouseY, ingredientBoxes.get(i)))
					book.renderToolTip(book.currentGraphics, displayStack[i], book.mouseX, book.mouseY);
			}
		}

	}

	@OnlyIn(Dist.CLIENT)
	static class NextPageButton extends Button {
		private final boolean isNextButton;
		private final boolean isTurbo;

		public NextPageButton(GuiLightningGuide parent, int parButtonId, int parPosX, int parPosY, boolean parIsNextButton, boolean parIsTurbo)
		{

			super(parPosX, parPosY, 23, 20,
					net.minecraft.network.chat.Component.literal(
							parIsNextButton ? (parIsTurbo ? ">>" : ">") : (parIsTurbo ? "<<" : "<")),
					b -> parent.buttonPressed(b), Button.DEFAULT_NARRATION);
			isNextButton = parIsNextButton;
			isTurbo = parIsTurbo;
		}

	}

}
