package org.lightningcraft.fluids;

import net.minecraftforge.fluids.ForgeFlowingFluid;

public class ModFluid {

	private ModFluid() {}

	public static class Source extends ForgeFlowingFluid.Source {
		public Source(ForgeFlowingFluid.Properties properties) {
			super(properties);
		}
	}

	public static class Flowing extends ForgeFlowingFluid.Flowing {
		public Flowing(ForgeFlowingFluid.Properties properties) {
			super(properties);
		}
	}
}
