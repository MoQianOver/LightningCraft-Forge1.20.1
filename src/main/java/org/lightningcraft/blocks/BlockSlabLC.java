package org.lightningcraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.registry.ILightningCraftBlock;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.items.blocks.ItemSlabLC;

public class BlockSlabLC extends SlabBlock implements ILightningCraftBlock {

	public static final int nVariants = BlockStone.nTypes + 1;
	public static final int THUNDER = 0;
	public static final int DEMON = 1;
	public static final int UNDER = 2;
	public static final int UNDER_PLANK = 3;

	public static final IntegerProperty VAR = IntegerProperty.create("variant", 0, nVariants - 1);

	public BlockSlabLC(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(VAR, 0)
				.setValue(TYPE, SlabType.BOTTOM));
	}

	public BlockSlabLC() {
		this(Properties.of().mapColor(MapColor.STONE).strength(10.0F, 100.0F));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(VAR);
	}

	@Override
	public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
		if (getMetaFromState(state) == UNDER_PLANK) {
			return LCBlocks.woodPlank.getExplosionResistance(
					LCBlocks.woodPlank.defaultBlockState(), level, pos, explosion);
		}
		return super.getExplosionResistance(state, level, pos, explosion);
	}

	public BlockState getStateFromMeta(int meta) {
		BlockState state = this.defaultBlockState().setValue(VAR, meta % 8);
		return state.setValue(TYPE, meta >= 8 ? SlabType.TOP : SlabType.BOTTOM);
	}

	@Override
	public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null) {
			return null;
		}
		int meta = context.getItemInHand().getDamageValue() % 8;
		return state.setValue(VAR, Math.max(0, Math.min(meta, nVariants - 1)));
	}

	public int getMetaFromState(BlockState state) {
		if (state.getValue(TYPE) == SlabType.TOP) {
			return 8 + state.getValue(VAR);
		}
		return state.getValue(VAR);
	}

	public int getSubBlockCount() {
		return nVariants;
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemSlabLC.class;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerRender() {

	}

}
