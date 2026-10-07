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

import org.lightningcraft.gui.server.ContainerStaticGenerator;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.tiles.TileEntityStaticGenerator;

@OnlyIn(Dist.CLIENT)
public class GuiStaticGenerator extends GuiContainerLC<ContainerStaticGenerator> {

	private static final ResourceLocation generatorGuiTextures = new ResourceLocation(RefStrings.MODID, "textures/gui/container/lpstaticgenerator.png");
	private TileEntityStaticGenerator tileGenerator;

	public GuiStaticGenerator(ContainerStaticGenerator menu, Inventory invPlayer, net.minecraft.network.chat.Component title) {
		super(menu, invPlayer, title);
		this.tileGenerator = menu.getTile();
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int par1, int par2) {

		int width = 34;
		int height = 6;
		int texty = 61;
		int texty2 = (61 - 53) + 40;

		String string = this.tileGenerator.getDisplayName().getString();
		guiGraphics.drawString(this.font, string, this.imageWidth / 2 - this.font.width(string) / 2, 6, 4210752);
		guiGraphics.drawString(this.font, I18n.get("container.inventory", new Object[0]), 8, this.imageHeight - 94, 4210752);

		if(tileGenerator.cellPower == 0) {
			width = 0;
		} else {
			width = (int)((tileGenerator.cellPower/tileGenerator.maxPower) * width);
		}
		guiGraphics.fill(47, 53, 47 + width, 53 + height, 0xffffffff);
		guiGraphics.drawString(this.font, tileGenerator.cellPower + " LE", 46, texty, 4210752);

		width = 34;
		if(tileGenerator.storedCharge == 0) {
			width = 0;
		} else {
			width = (int)((tileGenerator.storedCharge/100D) * width);
		}
		guiGraphics.fill(112, 40, 112 + width, 40 + height, 0xffffffff);
		guiGraphics.drawString(this.font, tileGenerator.storedCharge + " C", 111, texty2, 4210752);

	}

	@Override
	protected void drawBackground(GuiGraphics guiGraphics) {
	        int k = (this.width - this.imageWidth) / 2;
	        int l = (this.height - this.imageHeight) / 2;
	        guiGraphics.blit(generatorGuiTextures, k, l, 0, 0, this.imageWidth, this.imageHeight);
	        int i1;

	        if (this.tileGenerator.isBurning())
	        {
	            i1 = this.tileGenerator.getBurnTimeRemainingScaled(12);
	            guiGraphics.blit(generatorGuiTextures, k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
	        }

	        i1 = this.tileGenerator.getCookProgressScaled(24);
	        guiGraphics.blit(generatorGuiTextures, k + 79, l + 34, 176, 14, i1 + 1, 16);
	}

}