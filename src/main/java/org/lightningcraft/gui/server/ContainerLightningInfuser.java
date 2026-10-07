package org.lightningcraft.gui.server;
import org.lightningcraft.init.LCMenus;

import java.util.LinkedList;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lightningcraft.recipes.LightningInfusionRecipes;
import org.lightningcraft.tiles.TileEntityLightningInfuser;

public class ContainerLightningInfuser extends ContainerLightningUser.Upgradable {

	private TileEntityLightningInfuser tileInfuser;
	public static final int xoff = -14;
	public static final int yoff_inf = 4;
	public static final int yoff_inv = 12;

	public ContainerLightningInfuser(int windowId, Inventory player, net.minecraft.core.BlockPos pos) {
		super(LCMenus.LIGHTNING_INFUSER.get(), windowId, player, LCMenus.tileAt(player, pos));
		TileEntityLightningInfuser tile = LCMenus.tileAt(player, pos);
		this.tileInfuser = tile;

		this.addSlot(new Slot(tile, 0, 48 + xoff, 37 + yoff_inf));
		this.addSlot(new Slot(tile, 1, 48 + xoff, 17 + yoff_inf));
		this.addSlot(new Slot(tile, 2, 68 + xoff, 37 + yoff_inf));
		this.addSlot(new Slot(tile, 3, 48 + xoff, 57 + yoff_inf));
		this.addSlot(new Slot(tile, 4, 28 + xoff, 37 + yoff_inf));
		this.addSlot(new FurnaceResultSlot(player.player, tile, 5, 125 + xoff, 32 + yoff_inf));

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
			case INFO_OFFSET + 0 -> this.tileInfuser.infuserCookTime;
			case INFO_OFFSET + 1 -> this.tileInfuser.infusionCost;
			default -> super.readInfo(index);
		};
	}

	@Override
	protected int dataCount() {
		return INFO_OFFSET + 2;
	}

	@Override
	protected void writeInfo(int index, int par2) {
		switch (index) {
			case INFO_OFFSET + 0 -> this.tileInfuser.infuserCookTime = par2;
			case INFO_OFFSET + 1 -> this.tileInfuser.infusionCost = par2;
			default -> super.writeInfo(index, par2);
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return this.tileInfuser.isUseableByPlayer(player);
	}

	@Override
	public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(par2);

		LinkedList<Integer> INPUT = new LinkedList<Integer>();

		int OUTPUT = 5;
		INPUT.addLast(0);
		INPUT.addLast(1);
		INPUT.addLast(2);
		INPUT.addLast(3);
		INPUT.addLast(4);

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

			else if (par2 != OUTPUT && !INPUT.contains(par2))
			{

				if(LightningInfusionRecipes.instance().hasBaseResult(itemstack1))
				{

					if (!this.moveItemStackTo(itemstack1, INPUT.get(0), INPUT.get(4)+1, false))
					{
						return ItemStack.EMPTY;
					}
				} else {

					if (!this.moveItemStackTo(itemstack1, INPUT.get(1), INPUT.get(4)+1, false))
					{
						return ItemStack.EMPTY;
					}
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

	public TileEntityLightningInfuser getTile() {
		return this.tileInfuser;
	}
}
