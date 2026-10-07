package org.lightningcraft.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.items.blocks.ItemBlockMeta;
import org.lightningcraft.util.LCStack;

public abstract class BlockMeta extends BlockLC implements BlockVariant {

	protected final int nSubBlocks;
	public final boolean sameIcon;

	public BlockMeta(Block parent, int nSubBlocks, float hardness, float resistance, boolean sameIcon) {
		this(parent, nSubBlocks, hardness, resistance, sameIcon, false);
	}

	public BlockMeta(Block parent, int nSubBlocks, float hardness, float resistance, boolean sameIcon,
			boolean noOcclusion) {
		super(parent, hardness, resistance, noOcclusion);
		if (nSubBlocks > 16) throw new IllegalArgumentException("More than 16 metadata states is unsupported!");
		this.nSubBlocks = nSubBlocks;
		this.sameIcon = sameIcon;
		this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, 0));
	}

	@Override
	public int getSubBlockCount() {
		return nSubBlocks;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(VARIANT);
	}

	@Override
	public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null) return null;
		int meta = Math.max(0, Math.min(context.getItemInHand().getDamageValue(),
				Math.max(0, getSubBlockCount() - 1)));
		return state.setValue(VARIANT, meta);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return LCStack.of(this, 1, this.getMetaFromState(state));
	}

	@Override
	public java.util.List<ItemStack> getDrops(BlockState state,
			net.minecraft.world.level.storage.loot.LootParams.Builder params) {
		return java.util.List.of(LCStack.of(this, 1, this.getMetaFromState(state)));
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockMeta.class;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerRender() {

	}

}
