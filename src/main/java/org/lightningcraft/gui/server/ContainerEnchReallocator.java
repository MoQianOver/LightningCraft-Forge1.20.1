package org.lightningcraft.gui.server;
import org.lightningcraft.init.LCMenus;

import java.util.LinkedList;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lightningcraft.tiles.TileEntityEnchReallocator;
import org.lightningcraft.util.LCMisc;

public class ContainerEnchReallocator extends ContainerLightningUser.Upgradable {

	private TileEntityEnchReallocator tileRealloc;

	public ContainerEnchReallocator(int windowId, Inventory player, net.minecraft.core.BlockPos pos) {
		super(LCMenus.ENCH_REALLOCATOR.get(), windowId, player, LCMenus.tileAt(player, pos));
		TileEntityEnchReallocator tile = LCMenus.tileAt(player, pos);
		this.tileRealloc = tile;

		int yoff_inv = 32;

		this.addSlot(new Slot(tile, 0, 35, 17));
		this.addSlot(new Slot(tile, 1, 35, 53));

		int i;

		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + yoff_inv));
			}
		}

		for(i = 0; i < 9; ++i) {
			this.addSlot(new Slot(player, i, 8 + i * 18, 142 + yoff_inv));
		}

	}

	@Override
	protected int readInfo(int index) {
		return switch (index) {
			case INFO_OFFSET + 0 -> this.tileRealloc.reallocCookTime;
			case INFO_OFFSET + 1 -> this.tileRealloc.lpCost;
			case INFO_OFFSET + 2 -> this.tileRealloc.xpCost;
			case INFO_OFFSET + 3 -> this.tileRealloc.nTopEnchs;
			default -> super.readInfo(index);
		};
	}

	@Override
	protected int dataCount() {
		return INFO_OFFSET + 4;
	}

	@Override
	protected void writeInfo(int index, int par2) {
		switch (index) {
			case INFO_OFFSET + 0 -> this.tileRealloc.reallocCookTime = par2;
			case INFO_OFFSET + 1 -> this.tileRealloc.lpCost = par2;
			case INFO_OFFSET + 2 -> this.tileRealloc.xpCost = par2;
			case INFO_OFFSET + 3 -> this.tileRealloc.nTopEnchs = par2;
			default -> super.writeInfo(index, par2);
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return this.tileRealloc.isUseableByPlayer(player);
	}

	@Override
	public boolean clickMenuButton(Player player, int action) {
		this.tileRealloc.xpPlayer = player.experienceLevel;
		if(action > 0) {
			this.tileRealloc.player = player;
			this.tileRealloc.hasPlayer = true;
		} else if(action < 0) {
			this.tileRealloc.hasPlayer = false;
		}
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(par2);

		LinkedList<Integer> INPUT = new LinkedList<Integer>();

		int OUTPUT = 1;
		INPUT.addLast(0);
		INPUT.addLast(1);

		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (!INPUT.contains(par2))
			{

				if(!LCMisc.getEnchantments(itemstack1).isEmpty()) {

					if (!this.moveItemStackTo(itemstack1, INPUT.get(0), INPUT.get(0)+1, false))
					{
						return ItemStack.EMPTY;
					}
				}

				else if(LCMisc.getEnchantments(itemstack1).isEmpty() && (itemstack1.isEnchantable() || itemstack1.getItem() == Items.BOOK)) {

					if (!this.moveItemStackTo(itemstack1, INPUT.get(1), INPUT.get(1)+1, false))
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

	public TileEntityEnchReallocator getTile() {
		return this.tileRealloc;
	}
}
