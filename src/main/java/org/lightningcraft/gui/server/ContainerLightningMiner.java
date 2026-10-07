package org.lightningcraft.gui.server;
import org.lightningcraft.init.LCMenus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.tiles.TileEntityLightningMiner;
import org.lightningcraft.tiles.TileEntityLightningMiner.EnumMode;
import org.lightningcraft.util.IntList;

public class ContainerLightningMiner extends ContainerLightningUser.Upgradable {

	private TileEntityLightningMiner tile;

	public ContainerLightningMiner(int windowId, Inventory player, net.minecraft.core.BlockPos pos) {
		super(LCMenus.LIGHTNING_MINER.get(), windowId, player, LCMenus.tileAt(player, pos));
		TileEntityLightningMiner tile = LCMenus.tileAt(player, pos);
		this.tile = tile;

		int yoff_inv = 32;
		int i;

		for(i = 0; i < TileEntityLightningMiner.nStacks; ++i) {
			this.addSlot(new Slot(tile, i, 8 + i * 18, 20));
		}

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
	public boolean clickMenuButton(Player player, int action) {
		switch(action) {
		case 0:
			tile.rotateOperatingMode();
			break;
		case 1:
			tile.toggleBlockReplacement();
			break;
		}
		return true;
	}

	@Override
	protected int readInfo(int index) {
		return switch (index) {
			case INFO_OFFSET + 0 -> this.tile.mode.getID();
			case INFO_OFFSET + 1 -> this.tile.replaceBlocks ? 1 : 0;
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
			case INFO_OFFSET + 0 -> this.tile.mode = EnumMode.assignMode(par2);
			case INFO_OFFSET + 1 -> this.tile.replaceBlocks = par2 > 0;
			default -> super.writeInfo(index, par2);
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return this.tile.isUseableByPlayer(player);
	}

	@Override
	public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(par2);

		IntList INPUT = new IntList();
		for(int i : TileEntityLightningMiner.stacksInt) INPUT.add(i);
		int OUTPUT = INPUT.getLast();

		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (!INPUT.contains(par2))
			{

				if (!this.moveItemStackTo(itemstack1, INPUT.getFirst(), INPUT.getLast() + 1, false))
				{
					return ItemStack.EMPTY;
				}

				}

			else if(par2 >= OUTPUT+1 && par2 < OUTPUT+28)
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
				return null;
			}

			slot.onTake(par1EntityPlayer, itemstack1);
		}
		return itemstack;
	}

	public TileEntityLightningMiner getTile() {
		return this.tile;
	}
}
