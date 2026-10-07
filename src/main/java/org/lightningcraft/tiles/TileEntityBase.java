package org.lightningcraft.tiles;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.lightningcraft.blocks.base.BlockVariant;

public abstract class TileEntityBase extends BlockEntity {

	protected Random random = new Random();

	protected BlockPos pos;

	protected TileEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.pos = pos;
	}

	public void tick() {

	}

	public int getBlockMetadata() {
		BlockState state = this.getBlockState();
		if (state.hasProperty(BlockVariant.VARIANT)) {
			return state.getValue(BlockVariant.VARIANT);
		}
		return 0;
	}

	public ItemStack getSmeltingResult(ItemStack input) {
		if (this.level == null || input.isEmpty()) return ItemStack.EMPTY;
		return this.level.getRecipeManager()
				.getRecipeFor(net.minecraft.world.item.crafting.RecipeType.SMELTING,
						new net.minecraft.world.SimpleContainer(input), this.level)
				.map(r -> r.getResultItem(this.level.registryAccess()))
				.orElse(ItemStack.EMPTY);
	}

	@Override
	public CompoundTag getUpdateTag() {

		CompoundTag tagCompound = super.getUpdateTag();
		this.saveAdditional(tagCompound);
		return tagCompound;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		if (tag != null) {
			this.load(tag);
		}
	}

	public int getX() {
		return this.getBlockPos().getX();
	}

	public int getY() {
		return this.getBlockPos().getY();
	}

	public int getZ() {
		return this.getBlockPos().getZ();
	}

	public boolean isUseableByPlayer(Player player) {
		TileEntityBase tile = this;
		if (tile.getLevel() == null) return false;
		return tile.getLevel().getBlockEntity(tile.getBlockPos()) != tile ? false :
			player.distanceToSqr(tile.getX() + 0.5D, tile.getY() + 0.5D, tile.getZ() + 0.5D) <= 64.0D;
	}

}
