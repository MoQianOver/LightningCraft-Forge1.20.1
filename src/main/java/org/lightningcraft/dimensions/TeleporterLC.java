package org.lightningcraft.dimensions;

import java.util.Random;
import java.util.function.Function;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.lightningcraft.init.LCBlocks;

public class TeleporterLC implements ITeleporter {

	private final int dimFrom;
	private final ServerLevel worldServerInstance;

	private final Random random;
	private final Long2ObjectMap<PortalPosition> destinationCoordinateCache = new Long2ObjectOpenHashMap<>(4096);

	public TeleporterLC(int dimFrom, ServerLevel worldIn) {
		this.dimFrom = dimFrom;
		this.worldServerInstance = worldIn;
		this.random = new Random(worldIn.getSeed());
	}

	@Override
	public Entity placeEntity(Entity entityIn, ServerLevel currentWorld, ServerLevel destWorld,
			float rotationYaw, Function<Boolean, Entity> repositionEntity) {
		Entity moved = repositionEntity.apply(false);
		if (moved != null) {
			this.placeInPortal(moved, rotationYaw);
		}
		return moved;
	}

	private BlockPos builtPortalPos;

	private void placeInPortal(Entity entityIn, float rotationYaw) {
		if (this.placeInExistingPortal(entityIn, rotationYaw)) {
			return;
		}

		if (this.makePortal(entityIn) && this.builtPortalPos != null) {
			double px = this.builtPortalPos.getX() + 0.5D;
			double py = this.builtPortalPos.getY() + 0.5D;
			double pz = this.builtPortalPos.getZ() + 0.5D;
			entityIn.teleportTo(px, py, pz);
			entityIn.setYRot(rotationYaw);
			entityIn.setDeltaMovement(Vec3.ZERO);
		}
	}

	private boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
		int i = 128;
		double d0 = -1.0D;
		int j = Mth.floor(entityIn.getX());
		int k = Mth.floor(entityIn.getZ());
		boolean flag = true;
		BlockPos blockpos = BlockPos.ZERO;
		long l = ChunkPos.asLong(j, k);

		if (this.destinationCoordinateCache.containsKey(l)) {
			PortalPosition ppos = this.destinationCoordinateCache.get(l);
			d0 = 0.0D;
			blockpos = ppos.pos;
			ppos.lastUpdateTime = this.worldServerInstance.getGameTime();
			flag = false;
		} else {
			BlockPos bp3 = entityIn.blockPosition();

			for (int i1 = -128; i1 <= 128; ++i1) {
				BlockPos bp2;

				for (int j1 = -128; j1 <= 128; ++j1) {
					for (BlockPos bp1 = bp3.offset(i1, this.worldServerInstance.getHeight() - 1 - bp3.getY(), j1);
							bp1.getY() >= this.worldServerInstance.getMinBuildHeight(); bp1 = bp2) {
						bp2 = bp1.below();

						if (this.worldServerInstance.getBlockState(bp1).getBlock() == LCBlocks.underPortal) {
							while (this.worldServerInstance.getBlockState(bp2 = bp1.below()).getBlock() == LCBlocks.underPortal) {
								bp1 = bp2;
							}

							double d1 = bp1.distSqr(bp3);

							if (d0 < 0.0D || d1 < d0) {
								d0 = d1;
								blockpos = bp1;
							}
						}
					}
				}
			}
		}

		if (d0 >= 0.0D) {
			if (flag) {
				this.destinationCoordinateCache.put(l, new PortalPosition(blockpos, this.worldServerInstance.getGameTime()));
			}

			double d5 = blockpos.getX() + 0.5D;
			double d6 = blockpos.getY() + 0.5D;
			double d7 = blockpos.getZ() + 0.5D;

			org.lightningcraft.ref.Log.logger.info("[LC] 传送门落点(已存在): " + blockpos
					+ "  从 (" + Mth.floor(entityIn.getX()) + "," + Mth.floor(entityIn.getY()) + ","
					+ Mth.floor(entityIn.getZ()) + ") 传入");
			entityIn.teleportTo(d5, d6, d7);
			entityIn.setYRot(rotationYaw);
			entityIn.setDeltaMovement(Vec3.ZERO);
			return true;
		} else {
			return false;
		}
	}

	private boolean makePortal(Entity ent) {
		int i = 16;
		double d0 = -1.0D;
		int j = Mth.floor(ent.getX());
		int k = Mth.floor(ent.getY());
		int l = Mth.floor(ent.getZ());
		int i1 = j;
		int j1 = k;
		int k1 = l;
		int l1 = 0;
		int i2 = this.random.nextInt(4);
		MutableBlockPos mpos = new MutableBlockPos();
		int maxY = this.worldServerInstance.getHeight() - 1;
		int minY = this.worldServerInstance.getMinBuildHeight();

		for (int j2 = j - i; j2 <= j + i; ++j2) {
			double d1 = j2 + 0.5D - ent.getX();

			for (int l2 = l - i; l2 <= l + i; ++l2) {
				double d2 = l2 + 0.5D - ent.getZ();
				label142:

				for (int j3 = maxY; j3 >= minY; --j3) {
					if (this.worldServerInstance.isEmptyBlock(mpos.set(j2, j3, l2))) {
						while (j3 > minY && this.worldServerInstance.isEmptyBlock(mpos.set(j2, j3 - 1, l2))) {
							--j3;
						}

						for (int k3 = i2; k3 < i2 + 4; ++k3) {
							int l3 = k3 % 2;
							int i4 = 1 - l3;

							if (k3 % 4 >= 2) {
								l3 = -l3;
								i4 = -i4;
							}

							for (int j4 = 0; j4 < 3; ++j4) {
								for (int k4 = 0; k4 < 4; ++k4) {
									for (int l4 = -1; l4 < 4; ++l4) {
										int i5 = j2 + (k4 - 1) * l3 + j4 * i4;
										int j5 = j3 + l4;
										int k5 = l2 + (k4 - 1) * i4 - j4 * l3;
										mpos.set(i5, j5, k5);

										BlockState state = this.worldServerInstance.getBlockState(mpos);

										if (l4 < 0 && !state.isSolid()
												|| l4 >= 0 && !this.worldServerInstance.isEmptyBlock(mpos)) {
											continue label142;
										}
									}
								}
							}

							double d5 = j3 + 0.5D - ent.getY();
							double d7 = d1 * d1 + d5 * d5 + d2 * d2;

							if (d0 < 0.0D || d7 < d0) {
								d0 = d7;
								i1 = j2;
								j1 = j3;
								k1 = l2;
								l1 = k3 % 4;
							}
						}
					}
				}
			}
		}

		if (d0 < 0.0D) {
			for (int l5 = j - i; l5 <= j + i; ++l5) {
				double d3 = l5 + 0.5D - ent.getX();

				for (int j6 = l - i; j6 <= l + i; ++j6) {
					double d4 = j6 + 0.5D - ent.getZ();
					label562:

					for (int i7 = maxY; i7 >= minY; --i7) {
						if (this.worldServerInstance.isEmptyBlock(mpos.set(l5, i7, j6))) {
							while (i7 > minY && this.worldServerInstance.isEmptyBlock(mpos.set(l5, i7 - 1, j6))) {
								--i7;
							}

							for (int k7 = i2; k7 < i2 + 2; ++k7) {
								int j8 = k7 % 2;
								int j9 = 1 - j8;

								for (int j10 = 0; j10 < 4; ++j10) {
									for (int j11 = -1; j11 < 4; ++j11) {
										int j12 = l5 + (j10 - 1) * j8;
										int i13 = i7 + j11;
										int j13 = j6 + (j10 - 1) * j9;
										mpos.set(j12, i13, j13);

										BlockState state = this.worldServerInstance.getBlockState(mpos);
										if (j11 < 0 && !state.isSolid()
												|| j11 >= 0 && !this.worldServerInstance.isEmptyBlock(mpos)) {
											continue label562;
										}
									}
								}

								double d6 = i7 + 0.5D - ent.getY();
								double d8 = d3 * d3 + d6 * d6 + d4 * d4;

								if (d0 < 0.0D || d8 < d0) {
									d0 = d8;
									i1 = l5;
									j1 = i7;
									k1 = j6;
									l1 = k7 % 2;
								}
							}
						}
					}
				}
			}
		}

		boolean alongX = l1 % 2 == 0;
		for (int y = j1 - 1; y <= j1 + 4; ++y) {
			for (int w = -2; w <= 2; ++w) {
				int px = alongX ? i1 + w : i1;
				int pz = alongX ? k1 : k1 + w;
				mpos.set(px, y, pz);
				this.worldServerInstance.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
			}
		}

		BlockState frameState = ((org.lightningcraft.blocks.base.BlockVariant) LCBlocks.stoneBlock)
				.getStateFromMeta(org.lightningcraft.blocks.BlockStone.DEMON);

		// 先把框架立起来，再放传送门方块。
		// 顺序不能反：PortalUnderworld.neighborChanged 一收到方块更新就会检查框架，
		// 框架还没立起来时 Size.isSizeValid() 必然失败，于是刚放下的传送门方块
		// 会被立刻删掉 —— 表现就是"传送门只剩框架，门里是空的"。
		for (int y = j1 - 1; y <= j1 + 3; ++y) {
			for (int s = -1; s <= 1; s += 2) {
				int fx = alongX ? i1 + s * 2 : i1;
				int fz = alongX ? k1 : k1 + s * 2;
				this.worldServerInstance.setBlockAndUpdate(mpos.set(fx, y, fz), frameState);
			}
		}
		for (int w = -2; w <= 2; ++w) {
			int px = alongX ? i1 + w : i1;
			int pz = alongX ? k1 : k1 + w;
			this.worldServerInstance.setBlockAndUpdate(mpos.set(px, j1 - 1, pz), frameState);
			this.worldServerInstance.setBlockAndUpdate(mpos.set(px, j1 + 3, pz), frameState);
		}

		// 最后放传送门方块，并且用 flag=2（不触发邻居更新）。
		// 这样即使放置过程中框架还没完全就位，也不会被 neighborChanged 误删。
		for (int y = j1; y < j1 + 3; ++y) {
			for (int w = -1; w <= 1; ++w) {
				int px = alongX ? i1 + w : i1;
				int pz = alongX ? k1 : k1 + w;
				this.worldServerInstance.setBlock(mpos.set(px, y, pz),
						LCBlocks.underPortal.defaultBlockState(), 2);
			}
		}

		this.builtPortalPos = new BlockPos(i1, j1, k1);
		org.lightningcraft.ref.Log.logger.info("[LC] 新建传送门 @ " + i1 + "," + j1 + "," + k1
				+ "  轴=" + (alongX ? "X" : "Z")
				+ "  玩家传入 y=" + Mth.floor(ent.getY())
				+ "  minY=" + minY + " maxY=" + maxY);
		return true;
	}

	private static class PortalPosition {
		final BlockPos pos;
		long lastUpdateTime;

		PortalPosition(BlockPos pos, long lastUpdate) {
			this.pos = pos;
			this.lastUpdateTime = lastUpdate;
		}
	}

}
