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

import org.lightningcraft.gui.server.ContainerLightningMiner;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.tiles.TileEntityLightningMiner;

@OnlyIn(Dist.CLIENT)
public class GuiLightningMiner extends GuiContainerLC<ContainerLightningMiner> {

	public static final ResourceLocation minerTx = new ResourceLocation(RefStrings.MODID, "textures/gui/container/lpminer.png");
	private TileEntityLightningMiner tile;
	private Button modeBtn; private int modeButtonX;
	private Button replaceBtn; private int replaceButtonX;

	public GuiLightningMiner(ContainerLightningMiner menu, Inventory invPlayer, net.minecraft.network.chat.Component title) {
		super(menu, invPlayer, title);
		this.tile = menu.getTile();
		this.imageHeight = 198;
	}

	@Override
	public void init() {
		super.init();
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        int buttonWidth;

        buttonWidth = 77;
        modeButtonX = 8;
        modeBtn = Button.builder(net.minecraft.network.chat.Component.literal(""), b -> this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0)).bounds(k + modeButtonX, l + 54, buttonWidth, 20).build();
		this.addRenderableWidget(modeBtn);

        buttonWidth = 77;
        replaceButtonX = this.imageWidth - modeButtonX - buttonWidth;
        replaceBtn = Button.builder(net.minecraft.network.chat.Component.literal(""), b -> this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1)).bounds(k + replaceButtonX, l + 54, buttonWidth, 20).build();
		this.addRenderableWidget(replaceBtn);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int par1, int par2) {

		String string = this.tile.getDisplayName().getString();
		guiGraphics.drawString(this.font, string, this.imageWidth / 2 - this.font.width(string) / 2, 6, 4210752);
		guiGraphics.drawString(this.font, I18n.get("container.inventory"), 8, this.imageHeight - 94, 4210752);

		guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.operating_mode"), modeButtonX, modeBtn.getY() - 9 - this.topPos, 4210752);
		modeBtn.setMessage(net.minecraft.network.chat.Component.literal(tile.mode.getName()));

		guiGraphics.drawString(this.font, net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.replace_blocks"), replaceButtonX, replaceBtn.getY() - 9 - this.topPos, 4210752);
		replaceBtn.setMessage(net.minecraft.network.chat.Component.literal(tile.replaceBlocks ? net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.yes") : net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.no")));

		int colorLP = 4210752;
		int colorXP = colorLP;

		String needLP;
		double power = tile.getActualNeededPower(tile.mode.getCost());

		if(power > 0) {
			needLP = LCText.df.format(power) + " " + net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.le_needed");
		} else {
			needLP = net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.no_le_source");
		}
		String have = tile.cellPower + " " + net.minecraft.client.resources.language.I18n.get("gui.lightningcraft.le_available");

		if(tile.cellPower < power || tile.cellPower == 0) colorLP = 0xC00000;

		guiGraphics.drawString(this.font, needLP, this.imageWidth - this.font.width(needLP) - 8, 85, colorLP);
		guiGraphics.drawString(this.font, have, this.imageWidth - this.font.width(have) - 8, 95, colorLP);
	}

	@Override
	protected void drawBackground(GuiGraphics guiGraphics) {
	        int k = (this.width - this.imageWidth) / 2;
	        int l = (this.height - this.imageHeight) / 2;
	        guiGraphics.blit(minerTx, k, l, 0, 0, this.imageWidth, this.imageHeight);
	}

}