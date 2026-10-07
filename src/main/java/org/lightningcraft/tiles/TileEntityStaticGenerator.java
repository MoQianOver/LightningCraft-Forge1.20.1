package org.lightningcraft.tiles;

import net.minecraft.core.BlockPos;
import org.lightningcraft.init.LCTileEntities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.IFurnace;
import org.lightningcraft.entities.EntityLCLightningBolt;
import org.lightningcraft.util.Effect;

public class TileEntityStaticGenerator extends TileEntityLightningItemHandler.Upgradable {

	private static final int[] slotsTop = new int[]{0};

	private static final int lpBurnTime = 10;
	private static final double chargePerBlock = 0.8;
	private static final double chanceOfDrain = 0.25;

	public int generatorBurnTime;
	public int generatorCookTime;
	public int currentBurnTime;
	public double storedCharge;

	private boolean redo;

	public TileEntityStaticGenerator(BlockPos pos, BlockState state) {
		super(LCTileEntities.STATIC_GENERATOR.get(), pos, state);
		setSizeInventory(1);
	}

	@OnlyIn(Dist.CLIENT)
	public int getCookProgressScaled(int par1) {
		return this.generatorCookTime * par1 / lpBurnTime;
	}

	@OnlyIn(Dist.CLIENT)
	public int getBurnTimeRemainingScaled(int par1) {
		if (this.currentBurnTime == 0) {
			this.currentBurnTime = lpBurnTime;
		}

		return this.generatorBurnTime * par1 / this.currentBurnTime;
	}

	public boolean isBurning() {
		return this.generatorBurnTime > 0;
	}

	public void tick() {
		boolean burning = this.generatorCookTime > 0;
		boolean dosave = false;

		if (this.generatorBurnTime > 0) {
			--this.generatorBurnTime;
		}

		if (this.level.isClientSide) {

		} else {
			if (this.generatorBurnTime == 0 && this.canGenerate()) {
				this.currentBurnTime = this.generatorBurnTime = lpBurnTime;

				if (this.generatorBurnTime > 0) {
					dosave = true;
				}
			}

			if (this.isBurning() && this.canGenerate()) {
				++this.generatorCookTime;

				if (this.generatorCookTime == lpBurnTime) {
					this.generatorCookTime = 0;
					this.generate();
					dosave = true;
				}
			} else {
				this.generatorCookTime = 0;
			}

			if (burning != this.generatorCookTime > 0) {
				dosave = true;
				BlockState state = level.getBlockState(pos);
				((IFurnace)state.getBlock()).setBurning(state, level, pos, this.generatorCookTime > 0);
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

	private boolean canGenerate() {

		if(!this.hasLPCell() || this.isClientSidelyPowered()) return false;

		if (!this.getStack(0).isEmpty()) {
			if(this.tileCell.storedPower < 0.5) return false;
			if(this.getStack(0).getItem() instanceof BlockItem) return true;
		}
		return false;
	}

	private void generate() {
		if (this.canGenerate()) {

			double currentCost;
			EntityLCLightningBolt lightning;

			currentCost = (random.nextDouble() <= chanceOfDrain) ? 0.5 : 0.01;
			this.drawCellPower(currentCost);

			this.storedCharge += chargePerBlock;

			if(this.storedCharge >= 100 && this.cellPower < this.maxPower - 100D * this.tileCell.efficiency) {
				Effect.lightningGen(this.level, this.tileCell.getBlockPos().above());
				this.storedCharge = 0;
			}

			if(!getStack(0).isEmpty()) {
				this.getStack(0).setCount(this.getStack(0).getCount() - 1);

				if(this.getStack(0).getCount() <= 0){
					this.setStack(0, ItemStack.EMPTY);
				}
			}
		}
	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);

		this.generatorBurnTime = tagCompound.getShort("BurnTime");
		this.generatorCookTime = tagCompound.getShort("CookTime");
		this.storedCharge = tagCompound.getDouble("StoredCharge");
		this.currentBurnTime = lpBurnTime;
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);

		tagCompound.putShort("BurnTime", (short) this.generatorBurnTime);
		tagCompound.putShort("CookTime", (short) this.generatorBurnTime);
		tagCompound.putDouble("StoredCharge", this.storedCharge);

	}

	@Override
	public boolean canPlaceItem(int par1, ItemStack stack) {
		return stack != null && stack.getItem() instanceof BlockItem;
	}

	@Override
	public int[] getSlotsForFace(Direction facing) {
		return slotsTop;
	}

	@Override
	public boolean canPlaceItemThroughFace(int par1, ItemStack itemstack, Direction facing) {
		return this.canPlaceItem(par1, itemstack);
	}

	@Override
	public boolean canTakeItemThroughFace(int par1, ItemStack itemstack, Direction facing) {
		return false;
	}

}
