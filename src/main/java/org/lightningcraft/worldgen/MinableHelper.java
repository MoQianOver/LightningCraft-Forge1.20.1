package org.lightningcraft.worldgen;

import java.util.function.Predicate;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class MinableHelper implements Predicate<BlockState> {

	private final BlockState state;

	public MinableHelper(BlockState state) {
		this.state = state;
	}

	@Override
	public boolean test(BlockState input) {
		if (input.getBlock() == this.state.getBlock()) {
			for (Property<?> p : input.getProperties()) {
				try {
					if (input.getValue(p) != this.state.getValue(p)) {
						return false;
					}
				} catch (Exception e) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
