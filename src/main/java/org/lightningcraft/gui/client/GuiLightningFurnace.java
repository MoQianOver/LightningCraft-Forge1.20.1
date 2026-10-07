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

import org.lightningcraft.gui.server.ContainerLightningFurnace;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.tiles.TileEntityLightningFurnace;

@OnlyIn(Dist.CLIENT)
public class GuiLightningFurnace extends GuiContainerLC<ContainerLightningFurnace> {

	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(RefStrings.MODID, "textures/gui/container/lpfurnace.png");
	private TileEntityLightningFurnace tileFurnace;

	public GuiLightningFurnace(ContainerLightningFurnace menu, Inventory invPlayer, net.minecraft.network.chat.Component title) {
		super(menu, invPlayer, title);
		this.tileFurnace = menu.getTile();

	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int par1, int par2){

		int width = 34;
		int height = 6;
		int texty = 61;

		String string = this.tileFurnace.getDisplayName().getString();
		guiGraphics.drawString(this.font, string, this.imageWidth / 2 - this.font.width(string) / 2, 6, 4210752);
		guiGraphics.drawString(this.font, I18n.get("container.inventory", new Object[0]), 8, this.imageHeight - 94, 4210752);

		if(tileFurnace.cellPower == 0) {
			width = 0;
		} else {
			width = (int)(tileFurnace.cellPower/tileFurnace.maxPower * width);
		}
		guiGraphics.fill(47, 53, 47 + width, 53 + height, 0xffffffff);
		guiGraphics.drawString(this.font, tileFurnace.cellPower + " LE", 46, texty, 4210752);

	}

	@Override
	protected void drawBackground(GuiGraphics guiGraphics) {
	        int k = (this.width - this.imageWidth) / 2;
	        int l = (this.height - this.imageHeight) / 2;
	        guiGraphics.blit(furnaceGuiTextures, k, l, 0, 0, this.imageWidth, this.imageHeight);
	        int i1;

	        if (this.tileFurnace.isBurning())
	        {
	            i1 = this.tileFurnace.getBurnTimeRemainingScaled(12);
	            guiGraphics.blit(furnaceGuiTextures, k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
	        }

	        i1 = this.tileFurnace.getCookProgressScaled(24);
	        guiGraphics.blit(furnaceGuiTextures, k + 79, l + 34, 176, 14, i1 + 1, 16);
	}

}