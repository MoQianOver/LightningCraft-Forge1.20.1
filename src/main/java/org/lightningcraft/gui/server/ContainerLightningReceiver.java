package org.lightningcraft.gui.server;
import org.lightningcraft.init.LCMenus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lightningcraft.gui.ShortSender;
import org.lightningcraft.tiles.TileEntityLightningReceiver;

public class ContainerLightningReceiver extends AbstractContainerMenu {

	private TileEntityLightningReceiver rx;
	private Short lowStored = null;
	private Short highStored = null;
	private Short lowMax = null;
	private Short highMax = null;
	private Short lowX = null;
	private Short highX = null;
	private Short lowZ = null;
	private Short highZ = null;

	public ContainerLightningReceiver(int windowId, Inventory player, net.minecraft.core.BlockPos pos) {
		super(LCMenus.LIGHTNING_RECEIVER.get(), windowId);
		TileEntityLightningReceiver tile = LCMenus.tileAt(player, pos);
		this.rx = tile;
		this.addDataSlots(makeData());

		int i;
		int yoff_inv = 32;

		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + yoff_inv));
			}
		}

		for(i = 0; i < 9; ++i) {
			this.addSlot(new Slot(player, i, 8 + i * 18, 142 + yoff_inv));
		}
	}

	private ContainerData makeData() {
		return new ContainerData() {
			@Override
			public int get(int index) {
				int cellPower = (int)(rx.storedPower * 10D);
				int maxPower = (int)rx.maxPower;
				int x = rx.txPos.getX();
				int z = rx.txPos.getZ();
				return switch (index) {
					case 0 -> ShortSender.getLowShort(cellPower);
					case 1 -> ShortSender.getHighShort(cellPower);
					case 2 -> ShortSender.getLowShort(maxPower);
					case 3 -> ShortSender.getHighShort(maxPower);
					case 4 -> ShortSender.getLowShort(x);
					case 5 -> ShortSender.getHighShort(x);
					case 6 -> ShortSender.getLowShort(z);
					case 7 -> ShortSender.getHighShort(z);
					case 8 -> rx.txPos.getY();
					case 9 -> rx.outOfRange ? 1 : 0;
					case 10 -> (int)(rx.efficiency * 1000D);
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
					case 4 -> lowX = (short)value;
					case 5 -> highX = (short)value;
					case 6 -> lowZ = (short)value;
					case 7 -> highZ = (short)value;
					case 8 -> rx.txPos = new BlockPos(rx.txPos.getX(), value, rx.txPos.getZ());
					case 9 -> rx.outOfRange = value == 1;
					case 10 -> rx.efficiency = value / 1000D;
				}
				if(lowStored != null && highStored != null) rx.storedPower = ShortSender.getInt(lowStored, highStored) / 10D;
				if(lowMax != null && highMax != null) rx.maxPower = ShortSender.getInt(lowMax, highMax);
				if(lowX != null && highX != null) rx.txPos = new BlockPos(ShortSender.getInt(lowX, highX), rx.txPos.getY(), rx.txPos.getZ());
				if(lowZ != null && highZ != null) rx.txPos = new BlockPos(rx.txPos.getX(), rx.txPos.getY(), ShortSender.getInt(lowZ, highZ));
			}

			@Override
			public int getCount() {
				return 11;
			}
		};
	}
	@Override
	public boolean stillValid(Player player) {
		return this.rx.isUseableByPlayer(player);
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

	public TileEntityLightningReceiver getTile() {
		return this.rx;
	}
}
