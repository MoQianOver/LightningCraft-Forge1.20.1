package org.lightningcraft.gui.server;
import org.lightningcraft.init.LCMenus;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.recipes.LightningCrusherRecipes;
import org.lightningcraft.tiles.TileEntityLightningCrusher;

public class ContainerLightningCrusher extends ContainerLightningUser.Upgradable {

	private TileEntityLightningCrusher tileCrusher;

	public ContainerLightningCrusher(int windowId, Inventory player, net.minecraft.core.BlockPos pos) {
		super(LCMenus.LIGHTNING_CRUSHER.get(), windowId, player, LCMenus.tileAt(player, pos));
		TileEntityLightningCrusher tile = LCMenus.tileAt(player, pos);
		this.tileCrusher = tile;

		this.addSlot(new Slot(tile, 0, 56, 17));
		this.addSlot(new FurnaceResultSlot(player.player, tile, 1, 116, 35));

		int i;

		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(i = 0; i < 9; ++i) {
			this.addSlot(new Slot(player, i, 8 + i * 18, 142));
		}

	}

	@Override
	protected int readInfo(int index) {
		return switch (index) {
			case INFO_OFFSET + 0 -> this.tileCrusher.crusherCookTime;
			case INFO_OFFSET + 1 -> this.tileCrusher.crusherBurnTime;
			case INFO_OFFSET + 2 -> this.tileCrusher.currentBurnTime;
			default -> super.readInfo(index);
		};
	}

	@Override
	protected int dataCount() {
		return INFO_OFFSET + 3;
	}

	@Override
	protected void writeInfo(int index, int par2) {
		switch (index) {
			case INFO_OFFSET + 0 -> this.tileCrusher.crusherCookTime = par2;
			case INFO_OFFSET + 1 -> this.tileCrusher.crusherBurnTime = par2;
			case INFO_OFFSET + 2 -> this.tileCrusher.currentBurnTime = par2;
			default -> super.writeInfo(index, par2);
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return this.tileCrusher.isUseableByPlayer(player);
	}

	@Override
	public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(par2);

		int OUTPUT = 1;
		int INPUT = 0;

		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (par2 == OUTPUT)
			{

				if (!this.moveItemStackTo(itemstack1, OUTPUT+1, OUTPUT+36+1, true))
				{
					return ItemStack.EMPTY;
				}

				}

			else if (par2 != OUTPUT && par2 != INPUT)
			{

				if (LightningCrusherRecipes.instance().getCrushingResult(itemstack1) != null)
				{

					if (!this.moveItemStackTo(itemstack1, INPUT, INPUT+1, false))
					{
						return ItemStack.EMPTY;
					}
				}

				else if (par2 >= OUTPUT+1 && par2 < OUTPUT+28)
				{

					if (!this.moveItemStackTo(itemstack1, OUTPUT+28, OUTPUT+37, false))
					{
						return ItemStack.EMPTY;
					}
				}

				else if (par2 >= OUTPUT+28 && par2 < OUTPUT+37 && !this.moveItemStackTo(itemstack1, OUTPUT+1, OUTPUT+28, false))
				{
					return ItemStack.EMPTY;
				}
			}

			else if (!this.moveItemStackTo(itemstack1, OUTPUT+1, OUTPUT+37, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0)
			{
				slot.set(ItemStack.EMPTY);
			}
			else
			{
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(par1EntityPlayer, itemstack1);
		}
		return itemstack;
	}

	public TileEntityLightningCrusher getTile() {
		return this.tileCrusher;
	}
}
