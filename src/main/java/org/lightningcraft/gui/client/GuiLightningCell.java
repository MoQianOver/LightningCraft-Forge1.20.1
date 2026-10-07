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

import org.lightningcraft.gui.server.ContainerLightningCell;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.tiles.TileEntityLightningCell;

@OnlyIn(Dist.CLIENT)
public class GuiLightningCell extends GuiContainerLC<ContainerLightningCell> {

	private static final ResourceLocation lpCellGuiTextures = new ResourceLocation(RefStrings.MODID, "textures/gui/container/lpcell.png");
	private TileEntityLightningCell tileLPCell;

	public GuiLightningCell(ContainerLightningCell menu, Inventory invPlayer, net.minecraft.network.chat.Component title) {
		super(menu, invPlayer, title);
		this.tileLPCell = menu.getTile();
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int par1, int par2) {

		int width = 125;
		int height = 16;
		int texty = 36;

		String string = this.tileLPCell.cellName;
		guiGraphics.drawString(this.font, string, this.imageWidth / 2 - this.font.width(string) / 2, 6, 4210752);
		guiGraphics.drawString(this.font, I18n.get("container.inventory", new Object[0]), 8, this.imageHeight - 94, 4210752);

		guiGraphics.fill(25, 17, 25 + (int) (((float)tileLPCell.storedPower/(float)tileLPCell.maxPower) * width), 17 + height, 0xffffffff);

		guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.stored_le"), 24, texty, 4210752);
		guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.capacity"), 24, texty + 10, 4210752);
		guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.terminal_efficiency"), 24, texty + 20, 4210752);
		String storedPower = tileLPCell.storedPower + " LE";
		String maxPower = (int)tileLPCell.maxPower + " LE";
		String efficiency = (int)(tileLPCell.efficiency * 100D) + "%";
		guiGraphics.drawString(this.font, storedPower, 151 - this.font.width(storedPower), texty, 4210752);
		guiGraphics.drawString(this.font, maxPower, 151 - this.font.width(maxPower), texty + 10, 4210752);
		guiGraphics.drawString(this.font, efficiency, 151 - this.font.width(efficiency), texty + 20, 4210752);
	}

	@Override
	protected void drawBackground(GuiGraphics guiGraphics) {
	        int k = (this.width - this.imageWidth) / 2;
	        int l = (this.height - this.imageHeight) / 2;
	        guiGraphics.blit(lpCellGuiTextures, k, l, 0, 0, this.imageWidth, this.imageHeight);
	}

}