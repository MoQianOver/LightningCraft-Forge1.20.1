package org.lightningcraft.particles;

import java.awt.Color;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public final class ParticleSpecialSword {

	public static final float SCALE = 2.75F;

	private ParticleSpecialSword() {}

	public static ParticleOptions create(Color rgb) {
		return new DustParticleOptions(toVector(rgb), SCALE);
	}

	static Vector3f toVector(Color rgb) {
		return new Vector3f(rgb.getRed() / 255F, rgb.getGreen() / 255F, rgb.getBlue() / 255F);
	}
}
