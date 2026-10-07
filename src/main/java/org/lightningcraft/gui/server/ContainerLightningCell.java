package org.lightningcraft.gui.server;
import org.lightningcraft.init.LCMenus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lightningcraft.gui.ShortSender;
import org.lightningcraft.tiles.TileEntityLightningCell;

public class ContainerLightningCell extends AbstractContainerMenu {

	private TileEntityLightningCell tileLPCell;
	private Short lowStored = null;
	private Short highStored = null;
	private Short lowMax = null;
	private Short highMax = null;

	public ContainerLightningCell(int windowId, Inventory player, net.minecraft.core.BlockPos pos) {
		super(LCMenus.LIGHTNING_CELL.get(), windowId);
		TileEntityLightningCell tile = LCMenus.tileAt(player, pos);
		this.tileLPCell = tile;
		this.addDataSlots(makeData());

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

	private ContainerData makeData() {
		return new ContainerData() {
			@Override
			public int get(int index) {
				int cellPower = (int)(tileLPCell.storedPower * 10D);
				int maxPower = (int)tileLPCell.maxPower;
				return switch (index) {
					case 0 -> ShortSender.getLowShort(cellPower);
					case 1 -> ShortSender.getHighShort(cellPower);
					case 2 -> ShortSender.getLowShort(maxPower);
					case 3 -> ShortSender.getHighShort(maxPower);
					case 4 -> tileLPCell.isUpgraded() ? 1 : 0;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> lowStored = (short)value;
					case 1 -> highStored = (short)value;
					case 2 -> lowMax = (short)value;
					case 3 -> highMax = (short)value;
					case 4 -> tileLPCell.setUpgraded(value > 0);
				}
				if(lowStored != null && highStored != null) {
					tileLPCell.storedPower = ShortSender.getInt(lowStored, highStored) / 10D;
				}
				if(lowMax != null && highMax != null) {
					tileLPCell.maxPower = ShortSender.getInt(lowMax, highMax);
				}
			}

			@Override
			public int getCount() {
				return 5;
			}
		};
	}
	@Override
	public boolean stillValid(Player player) {
		return this.tileLPCell.isUseableByPlayer(player);
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

	public TileEntityLightningCell getTile() {
		return this.tileLPCell;
	}
}
