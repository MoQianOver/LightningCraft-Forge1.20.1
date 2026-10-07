package org.lightningcraft.tiles;

import org.lightningcraft.init.LCTileEntities;

import org.lightningcraft.util.LCStack;

import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.ForgeEventFactory;
import org.lightningcraft.api.util.StackHelper;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.util.LCMisc;

public class TileEntityLightningMiner extends TileEntityLightningItemHandler.Upgradable {

	public static final int nStacks = 9;
	public static final int[] stacksInt = StackHelper.makeIntArray(nStacks);
	public static final int rangeStock = 16;
	public static final int rangeUpgraded = 24;
	public static final int breakModTime = TileEntityLightningBreaker.breakModTime * 2;
	private static final int maxRetryFactor = 11;

	private static Block[] fillerBlocks = LCMisc.getBlocksFromRegs(LCConfig.minerFillerBlocks);

	public EnumMode mode = EnumMode.DEFAULT;
	public boolean replaceBlocks = true;
	private int retries = 0;

	public TileEntityLightningMiner(BlockPos pos, BlockState state) {
		super(LCTileEntities.LIGHTNING_MINER.get(), pos, state);
		setSizeInventory(nStacks);
	}

	public static enum EnumMode {
		NONE(0, "Disabled", 0),
		SHAFT(1, "1x1 Tunnel", 0.5),
		TUNNEL(2, "3x3 Tunnel", 0.8),
		ORES(3, "Seek Ores", 1.5),
		LOGS(4, "Seek Logs", 1.3),
		MATCHING(5, "Slot Matching", 1.8),
		ALL(6, "All in Range", 1.2);

		private static EnumMode DEFAULT = NONE;
		private int id;
		private String name;
		private double cost;

		private EnumMode(int id, String name, double cost) {
			this.id = id;
			this.name = name;
			this.cost = cost;
		}

		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.id;
		}

		public double getCost() {
			return this.cost;
		}

		public static EnumMode assignMode(int id) {
			for(EnumMode m : EnumMode.values()) {
				if(m.id == id) return m;
			}
			return DEFAULT;
		}

	}

	public void tick() {
		if(!level.isClientSide && level.getGameTime() % breakModTime == 0) doOperation();
	}

	public void doOperation() {
		if(this.hasLPCell() && this.canDrawCellPower(mode.cost) && !level.hasNeighborSignal(pos)) {
			int effectiveRange = getEffectiveRange();
			Direction face = Direction.from3DDataValue(this.getBlockMetadata());
			BlockPos initial = pos.relative(face, random.nextInt(effectiveRange) + 1);

			switch(mode) {
			case NONE:
				break;
			case SHAFT:
				this.mineBlock(initial);
				break;
			default:
				int width = mode == EnumMode.TUNNEL ? 2 : effectiveRange;
				int rad = width / 2;

				Axis mineAxis = face.getAxis();
				BlockPos find = initial;
				for(Axis a : Axis.values()) {
					if(a != mineAxis) find = find.relative(face.getClockWise(a), random.nextInt(width + 1) - rad);
				}

				BlockState state = level.getBlockState(find);
				Block block = state == null ? null : state.getBlock();

				boolean flag = false;
				if(mode == EnumMode.ORES) {
					if(block != null && Item.byBlock(block) != null) {
						ItemStack stack = LCStack.of(block, 1, org.lightningcraft.blocks.base.BlockVariant.getMeta(state));
						if(stack.getDescriptionId().toLowerCase().contains("ore") ||
								StackHelper.oreDictNameStartsWith(stack, "ore")) flag = true;
					}

				} else if(mode == EnumMode.LOGS) {
					if(block != null && Item.byBlock(block) != null) {
						ItemStack stack = LCStack.of(block, 1, org.lightningcraft.blocks.base.BlockVariant.getMeta(state));
						if(StackHelper.oreDictNameStartsWith(stack, "log")) flag = true;
					}

				} else if(mode == EnumMode.MATCHING) {
					if(block != null && Item.byBlock(block) != null) {
						ItemStack stack = LCStack.of(block, 1, org.lightningcraft.blocks.base.BlockVariant.getMeta(state));
						for(int slot : stacksInt) {
							if(StackHelper.areItemStacksEqualForCrafting(getItem(slot), stack)) {
								flag = true;
								break;
							}
						}
					}
				} else {
					flag = true;
				}

				this.mineBlock(find, flag);
			}
		}
	}

	private boolean mineBlock(BlockPos minePos) {
		return mineBlock(minePos, true);
	}

	private boolean mineBlock(BlockPos minePos, boolean cond) {
		Direction face = Direction.from3DDataValue(this.getBlockMetadata());

		BlockState mineState = level.getBlockState(minePos);
		Block mineBlock = mineState.getBlock();
		if(cond && mineBlock != null && !mineState.isAir() && mineState.getDestroySpeed(level, minePos) >= 0) {
			List<ItemStack> drops = Block.getDrops(mineState, (ServerLevel) this.level, minePos, null);

			float chance = 1.0F;

			if(random.nextFloat() <= chance) {

				boolean flag = false;
				if(replaceBlocks) {
					if(!LCMisc.inArray(mineBlock, fillerBlocks)) {
						for(int slot : stacksInt) {
							ItemStack s = this.getItem(slot);
							if(!s.isEmpty()) {
								Block block = Block.byItem(s.getItem());
								if(block != null && s.getDamageValue() == 0 && LCMisc.inArray(block, fillerBlocks)) {
									s.setCount(s.getCount() - 1);
									if(s.getCount() <= 0) this.setItem(slot, null);
									level.setBlock(minePos, block.defaultBlockState(), 3);
									flag = true;
								}
							}
						}
					}
				} else {
					level.removeBlock(minePos, false);
					flag = true;
				}

				if(flag) {
					this.level.levelEvent(2001, minePos, Block.getId(mineState));
					this.drawCellPower(mode.cost);
					for(ItemStack drop : drops) {
						ItemStack leftover = HopperBlockEntity.addItem(this, this, drop, face);
						if(!leftover.isEmpty()) Block.popResource(level, pos, leftover);
					}
					retries = 0;
					this.setChanged();
					return true;
				}
			}
		}

		rescheduleOperation();
		return false;
	}

	public int getEffectiveRange() {
		return isUpgraded() ? rangeUpgraded : rangeStock;
	}

	private int getNumRetries() {
		return Math.min(LCConfig.minerMaxRetries, getEffectiveRange() * maxRetryFactor);
	}

	public void rescheduleOperation() {
		retries++;
		if(retries > getNumRetries()) retries = 0;
		if(retries < getNumRetries()) {
			((ServerLevel)level).getServer().execute(() -> doOperation());
		}
	}

	public void rotateOperatingMode() {
		this.mode = EnumMode.assignMode((mode.id + 1) % EnumMode.values().length);
		this.setChanged();
	}

	public void toggleBlockReplacement() {
		this.replaceBlocks = !this.replaceBlocks;
		this.setChanged();
	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);

		this.mode = EnumMode.assignMode(tagCompound.getInt("operatingMode"));
		this.replaceBlocks = tagCompound.getBoolean("replaceBlocks");
	}

	@Override
	public void saveAdditional(CompoundTag tagCompound) {
		super.saveAdditional(tagCompound);

		tagCompound.putInt("operatingMode", this.mode.id);
		tagCompound.putBoolean("replaceBlocks", this.replaceBlocks);

	}

	@Override
	public boolean canPlaceItem(int par1, ItemStack itemstack) {
		return true;
	}

	@Override
	public int[] getSlotsForFace(Direction facing) {
		return stacksInt;
	}

	@Override
	public boolean canPlaceItemThroughFace(int par1, ItemStack itemstack, Direction facing) {
		return true;
	}

	@Override
	public boolean canTakeItemThroughFace(int par1, ItemStack itemstack, Direction facing) {
		return true;
	}

}
