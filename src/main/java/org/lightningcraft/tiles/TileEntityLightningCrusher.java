package org.lightningcraft.tiles;

import net.minecraft.core.BlockPos;
import org.lightningcraft.init.LCTileEntities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.IFurnace;
import org.lightningcraft.recipes.LightningCrusherRecipes;

public class TileEntityLightningCrusher extends TileEntityLightningItemHandler.Upgradable {

	private static final int top = 0;
	private static final int bottom = 1;

	private static final int[] slotsTop = new int[]{top};
	private static final int[] slotsBottom = new int[]{bottom};
	private static final int[] slotsSides = new int[]{top};

	public static final int burnTime = 160;
	public static final double energyUsage = 5;

	public int crusherBurnTime;
	public int crusherCookTime;
	public int currentBurnTime;

	private boolean redo;

	public TileEntityLightningCrusher(BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_CRUSHER.get(), pos, state);
		setSizeInventory(2);
	}

	@OnlyIn(Dist.CLIENT)
	public int getCookProgressScaled(int par1) {
		return this.crusherCookTime * par1 / (burnTime / 2);
	}

	@OnlyIn(Dist.CLIENT)
	public int getBurnTimeRemainingScaled(int par1) {
		if (this.currentBurnTime == 0) {
			this.currentBurnTime = (burnTime / 2);
		}

		return this.crusherBurnTime * par1 / this.currentBurnTime;
	}

	public boolean isBurning() {
		return this.crusherBurnTime > 0;
	}

	public void tick() {
		boolean burning = this.crusherCookTime > 0;
		boolean dosave = false;

		if(this.crusherBurnTime > 0) {
			--this.crusherBurnTime;
		}

		if(this.level.isClientSide) {

		} else {

			if (this.crusherBurnTime == 0 && this.canCrush()) {

				this.currentBurnTime = this.crusherBurnTime = burnTime;

				if (this.crusherBurnTime > 0) {
					dosave = true;
					this.drawCellPower(energyUsage);
				}
			}

			if (this.isBurning() && this.canCrush()) {
				++this.crusherCookTime;
				if (this.crusherCookTime == burnTime / 2) {
					this.crusherCookTime = 0;
					this.crushItem();
					dosave = true;
				}
			} else {
				this.crusherCookTime = 0;
			}

			if (burning != this.crusherCookTime > 0) {
				dosave = true;
				BlockState state = level.getBlockState(pos);
				((IFurnace)state.getBlock()).setBurning(state, level, pos, this.crusherCookTime > 0);
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

	private boolean canCrush() {

		if(!this.hasLPCell() || !this.canDrawCellPower(energyUsage)) return false;

		if (this.getStack(0).isEmpty()) {
			return false;
		} else {
			ItemStack itemstack = LightningCrusherRecipes.instance().getCrushingResult(this.getStack(0));
			if (itemstack.isEmpty()) return false;
			if (this.getStack(1).isEmpty()) return true;
			if (!ItemStack.isSameItemSameTags(this.getStack(1), itemstack)) return false;
			int result = getStack(1).getCount() + itemstack.getCount();
			return result <= getMaxStackSize() && result <= this.getStack(1).getMaxStackSize();
		}
	}

	private void crushItem() {
		if (this.canCrush()) {
			ItemStack itemstack = LightningCrusherRecipes.instance().getCrushingResult(this.getStack(0));

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
		return facing == Direction.DOWN ? slotsBottom : (facing == Direction.UP ? slotsTop : slotsSides);
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

		this.crusherBurnTime = tagCompound.getShort("BurnTime");
		this.crusherCookTime = tagCompound.getShort("CookTime");
		this.currentBurnTime = burnTime;
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);

		tagCompound.putShort("BurnTime", (short) this.crusherBurnTime);
		tagCompound.putShort("CookTime", (short) this.crusherBurnTime);

	}

}
