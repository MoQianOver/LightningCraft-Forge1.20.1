package org.lightningcraft.tiles;

import net.minecraft.core.BlockPos;
import org.lightningcraft.init.LCTileEntities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.IFurnace;

public class TileEntityLightningFurnace extends TileEntityLightningItemHandler.Upgradable {

	private static final int top = 0;
	private static final int bottom = 1;

	private static final int[] slotsTop = new int[]{top};
	private static final int[] slotsBottom = new int[]{bottom};
	private static final int[] slotsSides = new int[]{top};

	private static final int burnTime = 80;

	public int furnaceBurnTime;
	public int furnaceCookTime;
	public int currentBurnTime;

	private boolean redo;

	public TileEntityLightningFurnace(BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_FURNACE.get(), pos, state);
		setSizeInventory(2);
	}

	@OnlyIn(Dist.CLIENT)
	public int getCookProgressScaled(int par1) {
		return this.furnaceCookTime * par1 / (burnTime / 2);
	}

	@OnlyIn(Dist.CLIENT)
	public int getBurnTimeRemainingScaled(int par1) {
		if (this.currentBurnTime == 0) {
			this.currentBurnTime = (burnTime / 2);
		}

		return this.furnaceBurnTime * par1 / this.currentBurnTime;
	}

	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	public void tick() {
		boolean burning = this.furnaceBurnTime > 0;
		boolean dosave = false;

		if(this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
		}

		if(this.level.isClientSide) {

		} else {

			if (this.furnaceBurnTime == 0 && this.canSmelt()) {

				this.currentBurnTime = this.furnaceBurnTime = burnTime;

				if (this.furnaceBurnTime > 0) {
					dosave = true;
					this.drawCellPower(1);
				}
			}

			if (this.isBurning() && this.canSmelt()) {
				++this.furnaceCookTime;
				if (this.furnaceCookTime == burnTime / 2) {
					this.furnaceCookTime = 0;
					this.smeltItem();
					dosave = true;
				}
			} else {
				this.furnaceCookTime = 0;
			}

			if (burning != this.furnaceBurnTime > 0) {
				dosave = true;
				BlockState state = level.getBlockState(pos);
				((IFurnace)state.getBlock()).setBurning(state, level, pos, this.furnaceBurnTime > 0);
			}

		}

		if (dosave) {
			this.setChanged();
		}

		if(isUpgraded() && !redo) {
			redo = true;
			tick();
		}
		redo = false;
	}

	private boolean canSmelt() {

		if(!this.hasLPCell() || !this.canDrawCellPower(1)) return false;

		if (this.getStack(0).isEmpty()) {
			return false;
		} else {
			ItemStack itemstack = getSmeltingResult(this.getStack(0));
			if (itemstack.isEmpty()) return false;
			if (this.getStack(1).isEmpty()) return true;
			if (!ItemStack.isSameItemSameTags(this.getStack(1), itemstack)) return false;
			int result = getStack(1).getCount() + itemstack.getCount();
			return result <= getMaxStackSize() && result <= this.getStack(1).getMaxStackSize();
		}
	}

	private void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = getSmeltingResult(this.getStack(0));

			if (this.getStack(1).isEmpty()) {
				this.setStack(1, itemstack.copy());
			} else if (this.getStack(1).getItem() == itemstack.getItem()) {
				this.getStack(1).setCount(this.getStack(1).getCount() + itemstack.getCount());
			}

			this.getStack(0).setCount(this.getStack(0).getCount() - 1);

			if(this.getStack(0).getCount() <= 0) {
				this.setStack(0, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack itemstack) {
		return slot != bottom;
	}

	@Override
	public int[] getSlotsForFace(Direction facing) {
		return facing == Direction.UP ? slotsTop : (facing == Direction.DOWN ? slotsBottom : slotsSides);
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack itemstack, Direction facing) {
		return this.canPlaceItem(slot, itemstack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack itemstack, Direction facing) {
		return slot == bottom;
	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);

		this.furnaceBurnTime = tagCompound.getShort("BurnTime");
		this.furnaceCookTime = tagCompound.getShort("CookTime");
		this.currentBurnTime = burnTime;
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);

		tagCompound.putShort("BurnTime", (short) this.furnaceBurnTime);
		tagCompound.putShort("CookTime", (short) this.furnaceBurnTime);

	}

}
