package org.lightningcraft.blocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.block.Block;

import net.minecraft.util.RandomSource;

import net.minecraft.world.item.BlockItem;

import java.util.Random;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.LightningCraft;
import org.lightningcraft.api.IFurnace;
import org.lightningcraft.blocks.base.BlockContainerLC;
import org.lightningcraft.gui.LCGuiHandler;
import org.lightningcraft.items.blocks.ItemBlockRarity;
import org.lightningcraft.tiles.TileEntityLightningCrusher;

public class BlockLightningCrusher extends BlockContainerLC implements IFurnace {

	private static final DirectionProperty dir = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	private static final BooleanProperty lit = BooleanProperty.create("active");
	private static boolean keepInventory = false;

	public BlockLightningCrusher() {
		super(Blocks.IRON_BLOCK, 3.0f, 30.0f);
		this.registerDefaultState(this.stateDefinition.any().setValue(dir, Direction.NORTH).setValue(lit, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(dir, lit);
	}

	@Override
	public BlockEntity createNewTileEntity(BlockPos pos, BlockState state) {
		return new TileEntityLightningCrusher(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult side) {
		LCGuiHandler.open(player, LCGuiHandler.lightningCrusherGui, pos);
		return InteractionResult.SUCCESS;
	}

		public BlockState getStateFromMeta(int meta) {
		int side = Math.max(2, meta % 6);
		boolean on = meta / 6 > 0;
	    return defaultBlockState().setValue(dir, Direction.from3DDataValue(side)).setValue(lit, on);
	}

		public int getMetaFromState(BlockState state) {
	    Direction type = state.getValue(dir);
	    return type.get3DDataValue() + 6 * (state.getValue(lit) ? 1 : 0);
	}

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlock(pos, state.setValue(dir, placer.getDirection().getOpposite()), 2);
    }

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		BlockEntity tileentity = worldIn.getBlockEntity(pos);

		if (!keepInventory) {
			Containers.dropContents(worldIn, pos, (Container)tileentity);
			worldIn.updateNeighbourForOutputSignal(pos, this);
		}

		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

	@Override
	public boolean isBurning(BlockState state, BlockGetter level, BlockPos pos) {
		try {
			return state.getValue(lit);
		} catch(IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public void setBurning(BlockState state, BlockGetter level, BlockPos pos, boolean burning) {
        BlockEntity tile = level.getBlockEntity(pos);
        keepInventory = true;
		((Level)level).setBlock(pos, state.setValue(lit, burning), 3);
		keepInventory = false;
		((Level)level).setBlockEntity(tile);
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		return isBurning(state, level, pos) ? 12 : 0;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if(isBurning(state, level, pos)) {
			Direction enumfacing = state.getValue(dir);
			double d0 = pos.getX() + 0.5D;
			double d1 = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			double d2 = pos.getZ() + 0.5D;
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;

			switch (enumfacing) {
			case WEST:
				level.addParticle(ParticleTypes.CLOUD, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				break;
			case EAST:
				level.addParticle(ParticleTypes.CLOUD, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				break;
			case NORTH:
				level.addParticle(ParticleTypes.CLOUD, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
				break;
			case SOUTH:
				level.addParticle(ParticleTypes.CLOUD, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
				break;
			default:
			}
		}
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockRarity.class;
	}

	@Override
	public Object[] getItemClassArgs() {
		return new Object[]{Rarity.UNCOMMON};
	}

}
