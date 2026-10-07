package org.lightningcraft.tiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.lightningcraft.api.capabilities.ILightningUpgradable;
import org.lightningcraft.api.capabilities.implementation.BaseLightningUpgradable;
import org.lightningcraft.init.LCCapabilities;
import org.lightningcraft.tiles.ifaces.ISidedInventoryLC;
import org.lightningcraft.util.LCMisc;

public abstract class TileEntityLightningItemHandler extends TileEntityLightningUser implements ISidedInventoryLC {

	private SidedInvWrapper[] itemWrappers;
	private LazyOptional<IItemHandler>[] itemHandlers;

	private ItemStack[] stacks;

	protected TileEntityLightningItemHandler(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		stacks = new ItemStack[0];
	}

	public void setSizeInventory(int size) {
		stacks = new ItemStack[size];
		for(int i = 0; i < size; i++) {
			stacks[i] = ItemStack.EMPTY;
		}
	}

	@Override
	public int getContainerSize() {
		return stacks.length;
	}

	public @Nonnull ItemStack getStack(int index) {
		if(index >= stacks.length) return ItemStack.EMPTY;
		return stacks[index];
	}

	public boolean setStack(int index, @Nonnull ItemStack stack) {
		if(index >= stacks.length) return false;
		stacks[index] = stack;
		return true;
	}

	@Override
	public @Nonnull ItemStack getItem(int slot) {
		if(slot < 0 || slot >= stacks.length) return ItemStack.EMPTY;
		return stacks[slot];
	}

	@Override
	public @Nonnull ItemStack removeItem(int slot, int amount) {
		if(slot < 0 || slot >= stacks.length || stacks[slot].isEmpty()) {
			return ItemStack.EMPTY;
		}
		if(stacks[slot].getCount() <= amount) {
			ItemStack itemstack = stacks[slot];
			stacks[slot] = ItemStack.EMPTY;
			return itemstack;
		} else {
			ItemStack itemstack = stacks[slot].split(amount);
			if(stacks[slot].getCount() == 0) {
				stacks[slot] = ItemStack.EMPTY;
			}
			return itemstack;
		}
	}

	@Override
	public @Nonnull ItemStack removeItemNoUpdate(int slot) {
		if(slot < 0 || slot >= stacks.length || stacks[slot].isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack itemstack = stacks[slot];
		stacks[slot] = ItemStack.EMPTY;
		return itemstack;
	}

	@Override
	public void setItem(int slot, @Nonnull ItemStack itemstack) {
		if(slot < 0 || slot >= stacks.length) return;
		stacks[slot] = itemstack;
		if(!itemstack.isEmpty() && itemstack.getCount() > this.getMaxStackSize()) {
			itemstack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
		return true;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		int[] all = new int[getContainerSize()];
		for (int i = 0; i < all.length; i++) {
			all[i] = i;
		}
		return all;
	}

	@SuppressWarnings("unchecked")
	private LazyOptional<IItemHandler> handlerFor(Direction side) {
		if (itemHandlers == null) {
			itemWrappers = LCMisc.makeInvWrapper(this);
			itemHandlers = new LazyOptional[6];
			for (int i = 0; i < 6; i++) {
				final SidedInvWrapper wrapper = itemWrappers[i];
				itemHandlers[i] = LazyOptional.of(() -> wrapper);
			}
		}
		Direction dir = side == null ? Direction.UP : side;
		return itemHandlers[dir.get3DDataValue()];
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if(capability == ForgeCapabilities.ITEM_HANDLER) {
			return handlerFor(facing).cast();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);
		ListTag tagList = tagCompound.getList("Items", 10);
		setSizeInventory(this.getContainerSize());

		for (int i = 0; i < tagList.size(); ++i) {
			CompoundTag slotTag = tagList.getCompound(i);
			byte slot = slotTag.getByte("Slot");

			if (slot >= 0 && slot < this.stacks.length) {
				this.stacks[slot] = ItemStack.of(slotTag);
			}
		}
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);
		ListTag tagList = new ListTag();

		for (int i = 0; i < this.stacks.length; ++i) {
			if(!this.stacks[i].isEmpty()) {
				CompoundTag slotTag = new CompoundTag();
				slotTag.putByte("Slot", (byte)i);
				this.stacks[i].save(slotTag);
				tagList.add(slotTag);
			}
		}

		tagCompound.put("Items", tagList);
	}

	public static abstract class Upgradable extends TileEntityLightningItemHandler {

		private final BaseLightningUpgradable upgrade = new BaseLightningUpgradable();
		private LazyOptional<ILightningUpgradable> upgradeCapability;

		protected Upgradable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
			if(capability == LCCapabilities.LIGHTNING_UPGRADABLE) {
				if(upgradeCapability == null) {
					upgradeCapability = LazyOptional.of(() -> upgrade);
				}
				return upgradeCapability.cast();
			}
			return super.getCapability(capability, facing);
		}

		public boolean isUpgraded() {
			return upgrade.isUpgraded();
		}

		public void setUpgraded(boolean upgraded) {
			upgrade.setUpgraded(upgraded);
		}

		@Override
		public void load(CompoundTag tagCompound) {
			super.load(tagCompound);
			upgrade.deserializeNBT(tagCompound);
		}

		@Override
		public void saveAdditional(CompoundTag tagCompound) {
			super.saveAdditional(tagCompound);
			upgrade.serializeNBT(tagCompound);
		}

		public Component getDisplayName() {
			return Component.literal(this.getBlockState().getBlock().getName().getString()
					+ (isUpgraded() ? " (Upgr.)" : ""));
		}

		public boolean hasCustomName() {
			return true;
		}

	}

}
