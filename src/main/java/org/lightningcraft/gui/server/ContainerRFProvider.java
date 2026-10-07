package org.lightningcraft.gui.server;
import org.lightningcraft.init.LCMenus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lightningcraft.gui.ShortSender;
import org.lightningcraft.tiles.TileEntityEnergy;

public class ContainerRFProvider extends ContainerLightningUser {

	private TileEntityEnergy tileRF;
	private Short low = null;
	private Short high = null;

	public ContainerRFProvider(int windowId, Inventory player, net.minecraft.core.BlockPos pos) {
		super(LCMenus.RF_PROVIDER.get(), windowId, player, LCMenus.tileAt(player, pos));
		TileEntityEnergy tile = LCMenus.tileAt(player, pos);
		this.tileRF = tile;

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
			case INFO_OFFSET + 0 -> ShortSender.getLowShort(this.tileRF.getEnergyStored(null));
			case INFO_OFFSET + 1 -> ShortSender.getHighShort(this.tileRF.getEnergyStored(null));
			default -> 0;
		};
	}

	@Override
	protected int dataCount() {
		return INFO_OFFSET + 2;
	}

	@Override
	protected void writeInfo(int index, int par2) {
		switch (index) {
			case INFO_OFFSET + 0 -> low = (short) par2;
			case INFO_OFFSET + 1 -> high = (short) par2;
			default -> {}
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return this.tileRF.isUseableByPlayer(player);
	}

	@Override
	public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(par2);

		int INPUT = -1;

		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (par2 != INPUT)
			{

				if (par2 >= INPUT+1 && par2 < INPUT+28)
				{

					if (!this.moveItemStackTo(itemstack1, INPUT+28, INPUT+37, false))
					{
						return ItemStack.EMPTY;
					}
				}

				else if (par2 >= INPUT+28 && par2 < INPUT+37 && !this.moveItemStackTo(itemstack1, INPUT+1, INPUT+28, false))
				{
					return ItemStack.EMPTY;
				}
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

	public TileEntityEnergy getTile() {
		return this.tileRF;
	}
}
