package org.lightningcraft.gui.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.opengl.GL11;

import org.lightningcraft.gui.server.ContainerLightningInfuser;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.tiles.TileEntityLightningInfuser;

@OnlyIn(Dist.CLIENT)
public class GuiLightningInfuser extends GuiContainerLC<ContainerLightningInfuser> {

	public static final ResourceLocation infuserGuiTextures = new ResourceLocation(RefStrings.MODID, "textures/gui/container/lpinfuser.png");
	private TileEntityLightningInfuser tileInfuser;

	public GuiLightningInfuser(ContainerLightningInfuser menu, Inventory invPlayer, net.minecraft.network.chat.Component title) {
		super(menu, invPlayer, title);
		this.tileInfuser = menu.getTile();
		this.imageHeight = 178;
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int par1, int par2) {

		String string = this.tileInfuser.getDisplayName().getString();
		guiGraphics.drawString(this.font, string, this.imageWidth / 2 - this.font.width(string) / 2, 6, 4210752);
		guiGraphics.drawString(this.font, I18n.get("container.inventory", new Object[0]), 8, this.imageHeight - 94, 4210752);

		int color = 4210752;
		String need;
		double power = tileInfuser.getActualNeededPower(tileInfuser.infusionCost);
		if(power > 0) {
			need = LCText.df.format(power) + " " + net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.le_needed");
		} else {
			need = net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.no_infusion");
		}
		String have = tileInfuser.cellPower + " " + net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.le_available");
		if(tileInfuser.cellPower < power || tileInfuser.cellPower == 0) color = 0xC00000;
		guiGraphics.drawString(this.font, need, 119 - this.font.width(need) / 2, 60, color);
		guiGraphics.drawString(this.font, have, 119 - this.font.width(have) / 2, 70, color);

	}

	@Override
	protected void drawBackground(GuiGraphics guiGraphics) {
	        int k = (this.width - this.imageWidth) / 2;
	        int l = (this.height - this.imageHeight) / 2;
	        guiGraphics.blit(infuserGuiTextures, k, l, 0, 0, this.imageWidth, this.imageHeight);
	        int i1;

	        i1 = this.tileInfuser.getInfusionProgressScaled(24);
	        guiGraphics.blit(infuserGuiTextures, k + 75, l + 40, 176, 14, i1 + 1, 16);
	}

}