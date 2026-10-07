package org.lightningcraft.gui.client;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.opengl.GL11;

import org.lightningcraft.gui.server.ContainerLightningReceiver;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.tiles.TileEntityLightningReceiver;

@OnlyIn(Dist.CLIENT)
public class GuiLightningReceiver extends GuiContainerLC<ContainerLightningReceiver> {

	private static final ResourceLocation lpRXGuiTextures = new ResourceLocation(RefStrings.MODID, "textures/gui/container/lpreceiver.png");
	private TileEntityLightningReceiver rx;

	public GuiLightningReceiver(ContainerLightningReceiver menu, Inventory invPlayer, net.minecraft.network.chat.Component title) {
		super(menu, invPlayer, title);
		this.rx = menu.getTile();
		this.imageHeight = 198;
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int par1, int par2) {

		int width = 125;
		int height = 16;
		int texty = 36;

		String string = rx.getName();
		guiGraphics.drawString(this.font, string, this.imageWidth / 2 - this.font.width(string) / 2, 6, 0x404040);
		guiGraphics.drawString(this.font, I18n.get("container.inventory", new Object[0]), 8, this.imageHeight - 94, 0x404040);

		if(rx.efficiency > -1) {
			guiGraphics.fill(25, 17, 25 + (int) (rx.storedPower/rx.maxPower * width), 17 + height, 0xffffffff);

			guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.linked_le"), 24, texty, 0x404040);
			guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.capacity"), 24, texty + 10, 0x404040);
			guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.efficiency"), 24, texty + 20, 0x404040);
			guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.tx_distance"), 24, texty + 30, 0x404040);

			String storedPower = rx.storedPower + " LE";
			String maxPower = (int)rx.maxPower + " LE";
			String efficiency = LCText.df.format(rx.efficiency * 100D) + "%";
			double deltaX = rx.txPos.getX() - rx.getX();
			double deltaY = rx.txPos.getY() - rx.getY();
			double deltaZ = rx.txPos.getZ() - rx.getZ();
			String distance = LCText.df.format(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)) + "m";
			String position = "x: " + rx.txPos.getX() + ", y: " + rx.txPos.getY() + ", z: " + rx.txPos.getZ();

			guiGraphics.drawString(this.font, storedPower, 151 - this.font.width(storedPower), texty, 0x404040);
			guiGraphics.drawString(this.font, maxPower, 151 - this.font.width(maxPower), texty + 10, 0x404040);
			guiGraphics.drawString(this.font, efficiency, 151 - this.font.width(efficiency), texty + 20, 0x404040);
			guiGraphics.drawString(this.font, distance, 151 - this.font.width(distance), texty + 30, 0x404040);
			guiGraphics.drawString(this.font, position, 151 - this.font.width(position), texty + 40, 0x606060);
		} else if(rx.outOfRange) {
			guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.out_of_range"), 24, texty, 0xC00000);
		} else {
			guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.no_transmitter"), 24, texty, 0xC00000);
		}
	}

	@Override
	protected void drawBackground(GuiGraphics guiGraphics) {
	        int k = (this.width - this.imageWidth) / 2;
	        int l = (this.height - this.imageHeight) / 2;
	        guiGraphics.blit(lpRXGuiTextures, k, l, 0, 0, this.imageWidth, this.imageHeight);
	}

}