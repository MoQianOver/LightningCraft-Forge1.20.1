package org.lightningcraft.fluids;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

public class LCFluidType extends FluidType {

	private final ResourceLocation still;
	private final ResourceLocation flowing;
	private final int tintColor;

	public LCFluidType(ResourceLocation still, ResourceLocation flowing, int tintColor,
			int density, int viscosity, int temperature, int luminosity) {
		super(FluidType.Properties.create()
				.density(density)
				.viscosity(viscosity)
				.temperature(temperature)
				.lightLevel(luminosity));
		this.still = still;
		this.flowing = flowing;
		this.tintColor = tintColor;
	}

	@Override
	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {
			@Override
			public ResourceLocation getStillTexture() {
				return still;
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return flowing;
			}

			@Override
			public int getTintColor() {
				return tintColor;
			}
		});
	}
}
