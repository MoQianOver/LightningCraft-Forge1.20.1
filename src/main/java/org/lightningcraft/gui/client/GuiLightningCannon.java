package org.lightningcraft.gui.client;

import org.lightningcraft.util.LCStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.opengl.GL11;

import org.lightningcraft.gui.server.ContainerLightningCannon;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.tiles.TileEntityLightningCannon;

@OnlyIn(Dist.CLIENT)
public class GuiLightningCannon extends GuiContainerLC<ContainerLightningCannon> {

	private static final ResourceLocation cannonGuiTextures = new ResourceLocation(RefStrings.MODID, "textures/gui/container/lpcannon.png");
	private TileEntityLightningCannon tileCannon;
	private Button buttonAction;
	private int buttonActionX;

	public GuiLightningCannon(ContainerLightningCannon menu, Inventory invPlayer, net.minecraft.network.chat.Component title) {
		super(menu, invPlayer, title);
		this.tileCannon = menu.getTile();
		this.imageHeight = 198;
	}

	@Override
	public void init() {
		super.init();
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        int buttonWidth;

        buttonWidth = 150;
        buttonActionX = 10;
		buttonAction = Button.builder(net.minecraft.network.chat.Component.literal(""), b -> this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0)).bounds(k + buttonActionX, l + 34, buttonWidth, 20).build();
		this.addRenderableWidget(buttonAction);
	}

	public void buttonPressed(Button parButton) {
		if(parButton == buttonAction) {
			this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int par1, int par2) {

		int width = 125;
		int height = 16;
		int texty = 36;

		String string = LCStack.of(LCBlocks.lightningCannon, 1, tileCannon.getBlockMetadata()).getDisplayName().getString();
		guiGraphics.drawString(this.font, string, this.imageWidth / 2 - this.font.width(string) / 2, 6, 4210752);
		guiGraphics.drawString(this.font, I18n.get("container.inventory", new Object[0]), 8, this.imageHeight - 94, 4210752);

		guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.operating_mode"), buttonActionX, 24, 4210752);
		buttonAction.setMessage(net.minecraft.network.chat.Component.literal(tileCannon.mode.getName()));

		int color = 4210752;
		Double power = tileCannon.getActualNeededPower(tileCannon.getAttackEnergy());
		String need = LCText.df.format(power) + " " + net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.le_needed");
		String have = tileCannon.cellPower + " " + net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.le_available");
		if(tileCannon.cellPower < power) color = 0xC00000;
		if(power != Double.NaN) guiGraphics.drawString(this.font, need, 10, 70, color);
		guiGraphics.drawString(this.font, have, 10, 80, color);
	}

	@Override
	protected void drawBackground(GuiGraphics guiGraphics) {
	        int k = (this.width - this.imageWidth) / 2;
	        int l = (this.height - this.imageHeight) / 2;
	        guiGraphics.blit(cannonGuiTextures, k, l, 0, 0, this.imageWidth, this.imageHeight);
	}

}
