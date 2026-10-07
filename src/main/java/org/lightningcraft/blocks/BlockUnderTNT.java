package org.lightningcraft.blocks;
import org.lightningcraft.init.LCEntities;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import org.lightningcraft.api.registry.ILightningCraftBlock;
import org.lightningcraft.entities.EntityLCTNTPrimed;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.items.blocks.ItemBlockUnderTNT;
import org.lightningcraft.ref.Material;
import org.lightningcraft.util.LCStack;

public class BlockUnderTNT extends TntBlock implements ILightningCraftBlock {

	public static final int nVariants = 3;

	public static final int RAMPART = 0;
	public static final int LIGHTNING = 1;
	public static final int MYSTIC = 2;

	public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, nVariants - 1);

	public static final BooleanProperty EXPLODE = BooleanProperty.create("explode");

	public BlockUnderTNT() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.FIRE)
				.strength(10.0F, 5.0F)
				.sound(SoundType.SAND));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(EXPLODE, VARIANT);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return LCStack.of(this, 1, this.getMetaFromState(state));
	}

	public BlockState getStateFromMeta(int meta) {
		return this.defaultBlockState().setValue(VARIANT, meta % 8).setValue(EXPLODE, meta / 8 > 0);
	}

	public int getMetaFromState(BlockState state) {
		return state.getValue(VARIANT) + (state.getValue(EXPLODE) ? 8 : 0);
	}

	@Override
	public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null) {
			return null;
		}

		return this.getStateFromMeta(context.getItemInHand().getDamageValue());
	}

	@Override
	public void onCaughtFire(BlockState state, Level worldIn, BlockPos pos, Direction face, LivingEntity igniter) {
		if (!worldIn.isClientSide) {
			if (state.getValue(EXPLODE)) {
				EntityLCTNTPrimed entitytntprimed = new EntityLCTNTPrimed(LCEntities.LC_TNT_PRIMED.get(), worldIn, state.getValue(VARIANT),
						pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, igniter);
				worldIn.addFreshEntity(entitytntprimed);
				worldIn.playSound(null, entitytntprimed.getX(), entitytntprimed.getY(), entitytntprimed.getZ(),
						SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
		level.removeBlock(pos, false);

		if (!state.hasProperty(VARIANT) || level.isClientSide) {
			return;
		}
		int variant = state.getValue(VARIANT);
		EntityLCTNTPrimed entitytntprimed = new EntityLCTNTPrimed(LCEntities.LC_TNT_PRIMED.get(), level, variant,
				pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, explosion.getIndirectSourceEntity());
		entitytntprimed.fuse = level.getRandom().nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
		level.addFreshEntity(entitytntprimed);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {

		if (!state.hasProperty(VARIANT)) {
			return List.of();
		}
		if (state.getValue(VARIANT) == RAMPART) {
			return List.of(LCStack.of(LCItems.material, 2, Material.UNDER_POWDER_2));
		}
		return List.of(LCStack.of(this, 1, this.getMetaFromState(state)));
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockUnderTNT.class;
	}

}
