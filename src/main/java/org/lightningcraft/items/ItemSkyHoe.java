package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.items.base.ItemHoeLC;
import org.lightningcraft.ref.LCText;

public class ItemSkyHoe extends ItemHoeLC {

	public ItemSkyHoe(Tier mat) {
		super(mat);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		list.add(Component.literal(LCText.getQuickFarmLore()));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction facing = context.getClickedFace();

		if (player.isShiftKeyDown()) {
			return super.useOn(context);
		}

		int xMode = 1;
		int zMode = 0;
		int direction = Mth.floor(player.getYRot() * 4.0F / 360.0F + 0.5D) & 3;
		if (direction == 0 || direction == 2) {
			xMode = 0;
			zMode = 1;
		}

		for (int i = -1; i <= 1; i++) {
			BlockPos base = pos.offset(i * xMode, 0, i * zMode);
			BlockPos above = pos.offset(i * xMode, 1, i * zMode);
			Block block = level.getBlockState(base).getBlock();
			if (!(facing != Direction.DOWN
					&& level.getBlockState(above).canBeReplaced()
					&& (block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.FARMLAND))) {
				if (i == 0) {
					return InteractionResult.FAIL;
				} else {
					return super.useOn(context);
				}
			}
		}

		boolean flag = false;

		if (level.getBlockState(pos).getBlock() == Blocks.FARMLAND) {
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					flag |= super.useOn(withPos(context, pos.offset(i, 0, j))) == InteractionResult.SUCCESS;
				}
			}

		} else {
			for (int i = -1; i <= 1; i++) {
				flag |= super.useOn(withPos(context, pos.offset(i * xMode, 0, i * zMode))) == InteractionResult.SUCCESS;
			}
		}
		return flag ? InteractionResult.SUCCESS : InteractionResult.FAIL;
	}

	private static UseOnContext withPos(UseOnContext original, BlockPos pos) {
		return new UseOnContext(original.getLevel(), original.getPlayer(), original.getHand(),
				original.getItemInHand(),
				new BlockHitResult(original.getClickLocation(), original.getClickedFace(), pos,
						original.isInside()));
	}

}
