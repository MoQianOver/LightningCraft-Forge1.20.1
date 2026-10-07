package org.lightningcraft.tiles;

import net.minecraft.core.BlockPos;
import org.lightningcraft.init.LCTileEntities;

import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.IFurnace;
import org.lightningcraft.util.LCMisc;

public class TileEntityEnchReallocator extends TileEntityLightningItemHandler.Upgradable {

	private static final int top = 0;
	private static final int bottom = 1;

	private static final int[] slotsTop = new int[]{top};
	private static final int[] slotsBottom = new int[]{bottom};
	private static final int[] slotsSides = new int[]{top, bottom};

	private static final int lpBurnTime = 200;

	public int reallocBurnTime;
	public int reallocCookTime;
	public int currentBurnTime;

	private boolean redo;

	public List<CompoundTag> topEnchs;
	public int nTopEnchs;
	public int lpCost;
	public int xpCost;
	public int xpPlayer;
	public Player player = null;
	public boolean hasPlayer = false;

	public TileEntityEnchReallocator(BlockPos pos, BlockState state) {
		super(LCTileEntities.ENCH_REALLOCATOR.get(), pos, state);
		setSizeInventory(2);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@OnlyIn(Dist.CLIENT)
	public int getReallocProgressScaled(int par1) {
		return this.reallocCookTime * par1 / lpBurnTime;
	}

	@OnlyIn(Dist.CLIENT)
	public int getReallocTimeRemainingScaled(int par1) {
		if (this.currentBurnTime == 0) {
			this.currentBurnTime = lpBurnTime;
		}

		return this.reallocBurnTime * par1 / this.currentBurnTime;
	}

	public boolean isBurning() {
		return this.reallocBurnTime > 0;
	}

	public void tick() {
		boolean flag = this.reallocBurnTime > 0;
		boolean dosave = false;

		if (this.reallocBurnTime > 0) {
			--this.reallocBurnTime;
		}

		if (this.level.isClientSide) {

		} else {
			if (this.reallocBurnTime == 0 && this.canReallocate()) {
				this.currentBurnTime = this.reallocBurnTime = lpBurnTime;

				if (this.reallocBurnTime > 0) {
					dosave = true;
				}
			}

			if (this.isBurning() && this.canReallocate()) {
				++this.reallocCookTime;

				if (this.reallocCookTime == lpBurnTime) {
					this.reallocCookTime = 0;
					this.reallocateEnchs();
					dosave = true;
				}
			} else {
				this.reallocCookTime = 0;
			}

			if (flag != this.reallocBurnTime > 0) {
				dosave = true;
				BlockState state = level.getBlockState(pos);
				((IFurnace)state.getBlock()).setBurning(state, level, pos, reallocBurnTime > 0);
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

	private boolean canReallocate() {

		this.topEnchs = null;
		this.xpCost = -1;
		this.lpCost = -1;
		this.nTopEnchs = 0;

		if(!this.hasLPCell()) return false;

		ItemStack top = this.getStack(0);
		ItemStack bottom = this.getStack(1);

		List<CompoundTag> topEnchs = LCMisc.getEnchantments(top);

		if(!topEnchs.isEmpty()) {
			this.topEnchs = topEnchs;
			this.nTopEnchs = topEnchs.size();
		}

		if(top != null && bottom != null && !topEnchs.isEmpty() && (bottom.isEnchantable() || bottom.getItem() == Items.BOOK)) {
			this.xpCost = top.getBaseRepairCost() + bottom.getBaseRepairCost() + 5;
			this.lpCost = 50 + xpCost;

			if(!this.canDrawCellPower(lpCost) || this.xpCost > this.xpPlayer || this.player == null || !this.hasPlayer) return false;
			return true;
		}
		return false;
	}

	private void reallocateEnchs() {
		if (this.canReallocate()) {

			this.drawCellPower(lpCost);
			if(!this.player.getAbilities().instabuild) ((ServerPlayer)this.player).giveExperienceLevels(-xpCost);
			this.hasPlayer = false;

			int topCost = this.getStack(0).getBaseRepairCost();
			int bottomCost = this.getStack(1).getBaseRepairCost();

			if(this.getStack(0).getItem() == Items.ENCHANTED_BOOK) {
				this.setStack(0, new ItemStack(Items.BOOK, 1));
			} else if(this.getStack(0).getTag().contains("ench")) {
				this.getStack(0).getTag().remove("ench");
			}
			this.getStack(0).setRepairCost(topCost + 5);

			if(this.getStack(1).getItem() == Items.BOOK) {
				this.setStack(1, new ItemStack(Items.ENCHANTED_BOOK, 1));
			}

			LCMisc.addEnchantments(this.getStack(1), this.topEnchs);
			this.getStack(1).setRepairCost(bottomCost + 5);
		}
	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);

		this.reallocBurnTime = tagCompound.getShort("BurnTime");
		this.reallocCookTime = tagCompound.getShort("CookTime");
		this.currentBurnTime = lpBurnTime;
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);

		tagCompound.putShort("BurnTime", (short) this.reallocBurnTime);
		tagCompound.putShort("CookTime", (short) this.reallocBurnTime);

	}

	@Override
	public boolean canPlaceItem(int par1, ItemStack itemstack) {
		return true;
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
		return true;
	}

}
