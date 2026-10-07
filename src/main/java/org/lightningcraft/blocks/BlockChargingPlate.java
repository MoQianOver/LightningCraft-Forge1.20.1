package org.lightningcraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import org.lightningcraft.api.registry.ILightningCraftBlock;
import org.lightningcraft.items.blocks.ItemBlockRarity;
import org.lightningcraft.tiles.TileEntityChargingPlate;

public class BlockChargingPlate extends PressurePlateBlock implements ILightningCraftBlock, EntityBlock {

	public BlockChargingPlate() {
		super(Sensitivity.MOBS, BlockBehaviour.Properties.of()
				.mapColor(MapColor.METAL)
				.strength(0.5F)
				.sound(SoundType.METAL)
				.noCollission()
				.requiresCorrectToolForDrops(), BlockSetType.IRON);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityChargingPlate(pos, state);
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
