package org.lightningcraft.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class LCInventoryCrafting implements Container {

	protected final ItemStack[] stackList;
	private final int width;
	private final int height;

	public LCInventoryCrafting(AbstractContainerMenu menu, int xSz, int ySz) {
		this.width = xSz;
		this.height = ySz;
		this.stackList = new ItemStack[xSz * ySz];
		for (int i = 0; i < this.stackList.length; i++) {
			this.stackList[i] = ItemStack.EMPTY;
		}
	}

	public LCInventoryCrafting(int xSz, int ySz) {
		this(null, xSz, ySz);
	}

	public LCInventoryCrafting(int xSz, int ySz, java.util.List<ItemStack> contents) {
		this(xSz, ySz);
		int a = 0;
		for (ItemStack i : contents) {
			if (a >= this.stackList.length) break;
			this.setItem(a++, i);
		}
	}

	public LCInventoryCrafting(int xSz, int ySz, ItemStack... contents) {
		this(xSz, ySz);
		int a = 0;
		for (ItemStack i : contents) {
			if (a >= this.stackList.length) break;
			this.setItem(a++, i);
		}
	}

	@Override
	public int getContainerSize() {
		return this.stackList.length;
	}

	@Override
	public ItemStack getItem(int index) {
		return index >= 0 && index < this.stackList.length ? this.stackList[index] : ItemStack.EMPTY;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if (index >= 0 && index < this.stackList.length) {
			this.stackList[index] = stack;
		}
	}

	@Override
	public ItemStack removeItem(int index, int amount) {
		ItemStack s = this.getItem(index);
		if (!s.isEmpty()) {
			ItemStack out = s.split(amount);
			this.stackList[index] = s.isEmpty() ? ItemStack.EMPTY : s;
			return out;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack s = this.getItem(index);
		this.setItem(index, ItemStack.EMPTY);
		return s;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack s : this.stackList) {
			if (!s.isEmpty()) return false;
		}
		return true;
	}

	@Override
	public void clearContent() {
		for (int i = 0; i < this.stackList.length; i++) {
			this.stackList[i] = ItemStack.EMPTY;
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void setChanged() {

	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

}
