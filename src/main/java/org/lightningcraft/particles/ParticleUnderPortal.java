package org.lightningcraft.particles;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ParticleUnderPortal {

	private ParticleUnderPortal() {}

	public static ParticleOptions create() {
		return ParticleTypes.PORTAL;
	}
}
