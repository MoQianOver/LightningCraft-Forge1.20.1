package org.lightningcraft.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.registry.ILightningCraftBlock;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.init.LCParticles;
import org.lightningcraft.init.LCSoundEvents;
import org.lightningcraft.util.WorldUtils;

public class PortalUnderworld extends Block implements ILightningCraftBlock {

	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

	public static PortalStatus portalStatus = new PortalStatus();

	public static boolean ignitePortal(Level level, BlockPos pos) {
		if (LCConfig.portalEnabled) {
			return LCBlocks.underPortal.trySpawnPortal(level, pos);
		} else {
			return false;
		}
	}

	public PortalUnderworld() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.COLOR_BLACK)
				.strength(-1.0F, 3600000.0F)
				.noCollission()

				.noOcclusion()
				.randomTicks()
				.lightLevel(state -> 11)
				.noLootTable());
		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AXIS);
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return null;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (e.getVehicle() == null && e instanceof ServerPlayer sp) {
			portalStatus.setPortal(sp, true);
		}
	}

	public static void doTeleport(ServerPlayer p) {
		if (p.isOnPortalCooldown()) {
			p.setPortalCooldown();
			return;
		}
		net.minecraft.server.MinecraftServer server = p.server;
		if (server == null) {
			return;
		}

		boolean inUnderworld = p.level().dimension()
				.equals(org.lightningcraft.init.LCDimensions.UNDERWORLD_KEY);
		net.minecraft.resources.ResourceKey<Level> targetKey = inUnderworld
				? Level.OVERWORLD
				: org.lightningcraft.init.LCDimensions.UNDERWORLD_KEY;
		net.minecraft.server.level.ServerLevel target = server.getLevel(targetKey);
		if (target == null) {
			org.lightningcraft.ref.Log.logger.warn(
					"[LC] 找不到目标维度 " + targetKey.location() + "，传送取消");
			return;
		}

		portalStatus.setPortal(p, false);
		p.changeDimension(target,
				new org.lightningcraft.dimensions.TeleporterLC(0, target));
		p.setPortalCooldown();
	}

	public boolean trySpawnPortal(Level worldIn, BlockPos pos) {
		Size size = new Size(worldIn, pos, Direction.Axis.X);

		if (size.isSizeValid() && size.count == 0) {
			size.setPortalState();
			return true;
		} else {
			Size size1 = new Size(worldIn, pos, Direction.Axis.Z);

			if (size1.isSizeValid() && size1.count == 0) {
				size1.setPortalState();
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean movedByPiston) {
		Direction.Axis axis = state.getValue(AXIS);

		if (axis == Direction.Axis.X) {
			Size size = new Size(worldIn, pos, Direction.Axis.X);

			if (!size.isSizeValid() || size.count < size.sizeX * size.sizeY) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
			}
		} else if (axis == Direction.Axis.Z) {
			Size size1 = new Size(worldIn, pos, Direction.Axis.Z);

			if (!size1.isSizeValid() || size1.count < size1.sizeX * size1.sizeY) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level worldIn, BlockPos pos, RandomSource rand) {
		if (rand.nextInt(100) == 0) {

			worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
					LCSoundEvents.portalUnderworld, SoundSource.AMBIENT, 0.5F, rand.nextFloat() * 0.4F + 0.8F);
		}

		for (int i = 0; i < 4; ++i) {
			double d0 = pos.getX() + rand.nextFloat();
			double d1 = pos.getY() + rand.nextFloat();
			double d2 = pos.getZ() + rand.nextFloat();
			double d3 = (rand.nextFloat() - 0.5D) * 0.5D;
			double d4 = (rand.nextFloat() - 0.5D) * 0.5D;
			double d5 = (rand.nextFloat() - 0.5D) * 0.5D;
			int j = rand.nextInt(2) * 2 - 1;

			if (worldIn.getBlockState(pos.west()).getBlock() != this
					&& worldIn.getBlockState(pos.east()).getBlock() != this) {
				d0 = pos.getX() + 0.5D + 0.25D * j;
				d3 = rand.nextFloat() * 2.0F * j;
			} else {
				d2 = pos.getZ() + 0.5D + 0.25D * j;
				d5 = rand.nextFloat() * 2.0F * j;
			}

			LCParticles.spawnParticle("underPortal", d0, d1, d2, d3, d4, d5);
		}
	}

	public static class PortalStatus {
		private final Map<String, Boolean> portal;
		private final Map<String, Integer> cooldown;

		private PortalStatus() {
			portal = new HashMap<>();
			cooldown = new HashMap<>();
		}

		private String key(ServerPlayer player) {
			return player.getGameProfile().getName();
		}

		public boolean getPortal(ServerPlayer player) {
			Boolean b = portal.get(key(player));
			return b != null && b;
		}

		public void setPortal(ServerPlayer player, boolean doportal) {
			if (portal.containsKey(key(player)) || doportal) {
				portal.put(key(player), doportal);
			}
		}

		public void resetCooldown(ServerPlayer player) {
			cooldown.put(key(player), LCConfig.portalCooldown);
		}

		public boolean checkCooldown(ServerPlayer player) {
			Integer v = cooldown.get(key(player));
			int i = v == null ? 0 : v;
			if (i <= 0) {
				return true;
			} else {
				cooldown.put(key(player), i - 1);
				return false;
			}
		}
	}

	public static class Size {
		private final Level level;
		private final Direction.Axis axis;
		private final Direction face1;
		private final Direction face2;
		private int count = 0;
		private BlockPos pos;
		private int sizeY;
		private int sizeX;

		public Size(Level worldIn, BlockPos pos, Direction.Axis axis) {
			this.level = worldIn;
			this.axis = axis;

			if (axis == Direction.Axis.X) {
				this.face2 = Direction.EAST;
				this.face1 = Direction.WEST;
			} else {
				this.face2 = Direction.NORTH;
				this.face1 = Direction.SOUTH;
			}

			for (BlockPos blockpos = pos; pos.getY() > blockpos.getY() - 21 && pos.getY() > 0
					&& this.isValidBlock(worldIn.getBlockState(pos.below()).getBlock()); pos = pos.below());

			int i = this.calcSize(pos, this.face2) - 1;

			if (i >= 0) {
				this.pos = pos.relative(this.face2, i);
				this.sizeX = this.calcSize(this.pos, this.face1);

				if (this.sizeX < 2 || this.sizeX > 21) {
					this.pos = null;
					this.sizeX = 0;
				}
			}

			if (this.pos != null) {
				this.sizeY = this.updateSize();
			}
		}

		protected int calcSize(BlockPos pos, Direction face) {
			int i;

			for (i = 0; i < 22; ++i) {
				BlockPos blockpos = pos.relative(face, i);

				if (!this.isValidBlock(this.level.getBlockState(blockpos).getBlock())
						|| !WorldUtils.blockMatches(level, blockpos.below(), LCBlocks.stoneBlock, BlockStone.DEMON)) {
					break;
				}
			}

			return WorldUtils.blockMatches(level, pos.relative(face, i), LCBlocks.stoneBlock, BlockStone.DEMON) ? i : 0;
		}

		public int getSizeY() {
			return this.sizeY;
		}

		public int getSizeX() {
			return this.sizeX;
		}

		protected int updateSize() {
			label24:
				for (this.sizeY = 0; this.sizeY < 21; ++this.sizeY) {
					for (int i = 0; i < this.sizeX; ++i) {
						BlockPos blockpos = this.pos.relative(this.face1, i).above(this.sizeY);
						Block block = this.level.getBlockState(blockpos).getBlock();

						if (!this.isValidBlock(block)) {
							break label24;
						}

						if (block == LCBlocks.underPortal) {
							++this.count;
						}

						if (i == 0) {
							if (!WorldUtils.blockMatches(level, blockpos.relative(this.face2), LCBlocks.stoneBlock, BlockStone.DEMON)) {
								break label24;
							}
						} else if (i == this.sizeX - 1) {
							if (!WorldUtils.blockMatches(level, blockpos.relative(this.face1), LCBlocks.stoneBlock, BlockStone.DEMON)) {
								break label24;
							}
						}
					}
				}

			for (int j = 0; j < this.sizeX; ++j) {
				if (!WorldUtils.blockMatches(level, this.pos.relative(this.face1, j).above(this.sizeY), LCBlocks.stoneBlock, BlockStone.DEMON)) {
					this.sizeY = 0;
					break;
				}
			}

			if (this.sizeY <= 21 && this.sizeY >= 2) {
				return this.sizeY;
			} else {
				this.pos = null;
				this.sizeX = 0;
				this.sizeY = 0;
				return 0;
			}
		}

		protected boolean isValidBlock(Block block) {
			return block.defaultBlockState().isAir() || block == LCBlocks.underPortal;
		}

		public boolean isSizeValid() {
			return this.pos != null && this.sizeX >= 2 && this.sizeX <= 21 && this.sizeY >= 2 && this.sizeY <= 21;
		}

		public void setPortalState() {
			for (int i = 0; i < this.sizeX; ++i) {
				BlockPos blockpos = this.pos.relative(this.face1, i);
				for (int j = 0; j < this.sizeY; ++j) {
					this.level.setBlock(blockpos.above(j),
							LCBlocks.underPortal.defaultBlockState().setValue(AXIS, this.axis), 2);
				}
			}
		}
	}

}
