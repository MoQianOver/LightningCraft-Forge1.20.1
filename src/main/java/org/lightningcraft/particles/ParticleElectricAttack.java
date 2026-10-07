package org.lightningcraft.particles;

import java.awt.Color;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ParticleElectricAttack {

	private ParticleElectricAttack() {}

	public static ParticleOptions create(float scale, Color rgb) {
		return new DustParticleOptions(ParticleSpecialSword.toVector(rgb), scale);
	}
}
