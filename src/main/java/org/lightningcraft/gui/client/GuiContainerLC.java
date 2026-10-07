package org.lightningcraft.gui.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class GuiContainerLC<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

	public GuiContainerLC(T menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected final void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		drawBackground(guiGraphics);
	}

	protected abstract void drawBackground(GuiGraphics guiGraphics);

}
