package org.lightningcraft.gui.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.opengl.GL11;

import org.lightningcraft.gui.server.ContainerEnchReallocator;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.tiles.TileEntityEnchReallocator;

@OnlyIn(Dist.CLIENT)
public class GuiEnchReallocator extends GuiContainerLC<ContainerEnchReallocator> {

	public static final ResourceLocation reallocGuiTextures = new ResourceLocation(RefStrings.MODID, "textures/gui/container/lpenchantmentreallocator.png");
	private TileEntityEnchReallocator tileRealloc;
	private Button buttonAction;

	public GuiEnchReallocator(ContainerEnchReallocator menu, Inventory invPlayer, net.minecraft.network.chat.Component title) {
		super(menu, invPlayer, title);
		this.tileRealloc = menu.getTile();
		this.imageHeight = 198;
	}

	@Override
	public void init() {
		super.init();
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
		buttonAction = Button.builder(net.minecraft.network.chat.Component.literal(""), b -> this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0)).bounds(k + this.imageWidth - 64, l + 82, 60, 20).build();
		buttonAction.visible = false;
		this.addRenderableWidget(buttonAction);
	}

	public void buttonPressed(Button parButton)
	{
		if(parButton == buttonAction) {
			if(!(tileRealloc.reallocCookTime > 0)) {

				this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
			} else {

				this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, -1);
			}
		}
	}

	@Override
	public void containerTick() {
		super.containerTick();

		if(tileRealloc.reallocCookTime > 0) {
			buttonAction.setMessage(net.minecraft.network.chat.Component.literal(net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.cancel")));
		} else {
			buttonAction.setMessage(net.minecraft.network.chat.Component.literal(net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.start")));
		}

		if(tileRealloc.cellPower < tileRealloc.lpCost || tileRealloc.cellPower == 0 ||
				minecraft.player.experienceLevel < tileRealloc.xpCost || tileRealloc.lpCost <= 0 || tileRealloc.nTopEnchs <= 0) {
			buttonAction.visible = false;
		} else {
			buttonAction.visible = true;
		}

	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int par1, int par2){
		String string = this.tileRealloc.getDisplayName().getString();
		guiGraphics.drawString(this.font, string, this.imageWidth / 2 - this.font.width(string) / 2, 6, 4210752);
		guiGraphics.drawString(this.font, I18n.get("container.inventory", new Object[0]), 8, this.imageHeight - 94, 4210752);

		int colorLP = 4210752;
		int colorXP = colorLP;

		String needLP;
		String needXP;
		double power = tileRealloc.getActualNeededPower(tileRealloc.lpCost);

		if(power > 0) {
			needLP = LCText.df.format(power) + " " + net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.le_needed");
			needXP = tileRealloc.xpCost + " XP needed";
		} else {
			needLP = net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.no_item");
			needXP = "";
		}
		String have = tileRealloc.cellPower + " " + net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.le_available");

		String enchs, enchs2;
		if(tileRealloc.nTopEnchs > 0) {
			enchs = net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.will_reallocate");
			enchs2 = tileRealloc.nTopEnchs + (tileRealloc.nTopEnchs > 1 ? " enchantments" : " enchantment");
		} else {
			enchs = net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.no_enchantments");
			enchs2 = net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.available");
		}

		if(tileRealloc.cellPower < power || tileRealloc.cellPower == 0) colorLP = 0xC00000;
		if(minecraft.player.experienceLevel < tileRealloc.xpCost) colorXP = 0xC00000;

		guiGraphics.drawString(this.font, enchs, 60, 20, 4210752);
		guiGraphics.drawString(this.font, enchs2, 60, 30, 4210752);

		guiGraphics.drawString(this.font, needXP, 60, 50, colorXP);
		guiGraphics.drawString(this.font, needLP, 60, 60, colorLP);
		guiGraphics.drawString(this.font, have, 60, 70, colorLP);

		if(tileRealloc.player != this.minecraft.player) {
			this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
			tileRealloc.player = this.minecraft.player;
		}
	}

	@Override
	protected void drawBackground(GuiGraphics guiGraphics) {
	        int k = (this.width - this.imageWidth) / 2;
	        int l = (this.height - this.imageHeight) / 2;
	        guiGraphics.blit(reallocGuiTextures, k, l, 0, 0, this.imageWidth, this.imageHeight);
	        int i1;
	        i1 = this.tileRealloc.getReallocProgressScaled(13);
	        guiGraphics.blit(reallocGuiTextures, k + 35, l + 37, 176, 0, 15, i1 + 1);
	}

}
