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
import org.lightningcraft.recipes.LightningInfusionRecipes;

public class TileEntityLightningInfuser extends TileEntityLightningItemHandler.Upgradable {

	private static final int top = 0;
	private static final int inf1 = 1;
	private static final int inf2 = 2;
	private static final int inf3 = 3;
	private static final int inf4 = 4;
	private static final int bottom = 5;

	private static final int[] slotsTop = new int[]{top};
	private static final int[] slotsBottom = new int[]{bottom};
	private static final int[] slotsSides = new int[]{inf1, inf2, inf3, inf4};

	public static final int burnTime = 600;

	public int infuserBurnTime;
	public int infuserCookTime;
	public int currentBurnTime;
	public int infusionCost;

	private boolean redo;

	public TileEntityLightningInfuser(BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_INFUSER.get(), pos, state);
		setSizeInventory(6);
	}

	@OnlyIn(Dist.CLIENT)
	public int getInfusionProgressScaled(int par1) {
		return this.infuserCookTime * par1 / burnTime;
	}

	@OnlyIn(Dist.CLIENT)
	public int getInfusionTimeRemainingScaled(int par1) {
		if (this.currentBurnTime == 0) {
			this.currentBurnTime = burnTime;
		}

		return this.infuserBurnTime * par1 / this.currentBurnTime;
	}

	public boolean isBurning() {
		return this.infuserBurnTime > 0;
	}

	public void tick() {
		boolean burning = this.infuserCookTime > 0;
		boolean dosave = false;

		if (this.infuserBurnTime > 0) {
			--this.infuserBurnTime;
		}

		if (this.level.isClientSide) {

		} else {
			if (this.infuserBurnTime == 0 && this.canInfuse()) {
				this.currentBurnTime = this.infuserBurnTime = burnTime;

				if (this.infuserBurnTime > 0) {
					dosave = true;
				}
			}

			if (this.isBurning() && this.canInfuse()) {
				++this.infuserCookTime;

				if (this.infuserCookTime == burnTime) {
					this.infuserCookTime = 0;
					this.infuseItem();
					dosave = true;
				}
			} else {
				this.infuserCookTime = 0;
			}

			if (burning != this.infuserCookTime > 0) {
				dosave = true;
				BlockState state = level.getBlockState(pos);
				((IFurnace)state.getBlock()).setBurning(state, level, pos, this.infuserCookTime > 0);
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

	private boolean canInfuse() {

		this.infusionCost = 0;
		if(!this.hasLPCell()) return false;

		if (this.getStack(0).isEmpty()) {
			return false;
		} else {
			ItemStack itemstack = LightningInfusionRecipes.instance().getInfusionResult(this.getStack(0),
					this.getStack(1), this.getStack(2), this.getStack(3), this.getStack(4));
			this.infusionCost = LightningInfusionRecipes.instance().getLastResultCost();
			if (itemstack.isEmpty()) return false;
			if(this.infusionCost <= 0) return false;
			if(!this.canDrawCellPower(this.infusionCost)) return false;
			if (this.getStack(5).isEmpty()) return true;
			if (!ItemStack.isSameItemSameTags(this.getStack(5), itemstack)) return false;
			int result = getStack(5).getCount() + itemstack.getCount();
			return result <= getMaxStackSize() && result <= this.getStack(5).getMaxStackSize();
		}
	}

	private void infuseItem() {
		if (this.canInfuse()) {
			ItemStack itemstack = LightningInfusionRecipes.instance().getInfusionResult(this.getStack(0),
					this.getStack(1), this.getStack(2), this.getStack(3), this.getStack(4));
			int cost = LightningInfusionRecipes.instance().getLastResultCost();

			this.drawCellPower(cost);

			if (this.getStack(5).isEmpty()) {
				this.setStack(5, itemstack.copy());
			} else if (this.getStack(5).getItem() == itemstack.getItem()) {
				this.getStack(5).setCount(this.getStack(5).getCount() + itemstack.getCount());
			}

			for(int i = 0; i < 5; i++) {
				if(!getStack(i).isEmpty()) {
					this.getStack(i).setCount(this.getStack(i).getCount() - 1);

					if(this.getStack(i).getCount() <= 0){
						this.setStack(i, ItemStack.EMPTY);
					}
				}

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

		this.infuserBurnTime = tagCompound.getShort("BurnTime");
		this.infuserCookTime = tagCompound.getShort("CookTime");
		this.currentBurnTime = burnTime;
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);

		tagCompound.putShort("BurnTime", (short) this.infuserBurnTime);
		tagCompound.putShort("CookTime", (short) this.infuserBurnTime);

	}

}
