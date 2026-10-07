package org.lightningcraft.blocks;

import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.lightningcraft.api.registry.ILightningCraftBlock;

public class BlockUnderSand extends FallingBlock implements ILightningCraftBlock {

	public BlockUnderSand() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.SAND)
				.strength(1.0F)
				.sound(SoundType.SAND));
	}

}
