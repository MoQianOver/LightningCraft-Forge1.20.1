package org.lightningcraft.blocks;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.lightningcraft.blocks.base.BlockMeta;
import org.lightningcraft.util.LCStack;

public class BlockUnderOre extends BlockMeta {

	public static final int nVariants = 4;

	public static final int IRON = 0;
	public static final int GOLD = 1;
	public static final int DIAMOND = 2;
	public static final int EMERALD = 3;

	public BlockUnderOre() {
		super(Blocks.STONE, nVariants, 10, 100, false);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		int meta = this.getMetaFromState(state);
		switch (meta) {
			case DIAMOND:
				return List.of(new ItemStack(Items.DIAMOND, 2));
			case EMERALD:
				return List.of(new ItemStack(Items.EMERALD, 2));
			default:
				return List.of(LCStack.of(this, 1, meta));
		}
	}

}
