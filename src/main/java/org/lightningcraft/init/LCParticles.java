package org.lightningcraft.init;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.particles.ParticleElectricAttack;
import org.lightningcraft.particles.ParticleSpecialSword;
import org.lightningcraft.particles.ParticleUnderPortal;

public class LCParticles {

	public static final int swordParticleCount = 6;
	public static final float swordParticleSpread = 2.5F;
	public static final float electricAttackScale = 0.7F;

	private static final Color soulRGB = new Color(96, 32, 32);
	private static final Color zombieRGB = new Color(64, 128, 64);
	private static final Color featherRGB = new Color(192, 192, 64);
	private static final Color enderRGB = new Color(128, 32, 128);
	private static final Color blazeRGB = new Color(255, 192, 16);
	private static final Color iceRGB = new Color(128, 140, 224);
	private static final Color electricRGB = new Color(192, 250, 255);

	@OnlyIn(Dist.CLIENT)
	public static void spawnParticle(String name, double x, double y, double z, double vx, double vy, double vz) {
		Level level = Minecraft.getInstance().level;
		if (level == null) return;

		ParticleOptions custom = customFor(name);
		if (custom != null) {
			level.addParticle(custom, x, y, z, vx, vy, vz);
			return;
		}

		ParticleOptions vanilla = vanillaByName(name);
		if (vanilla != null) {
			level.addParticle(vanilla, x, y, z, vx, vy, vz);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static ParticleOptions customFor(String name) {
		switch (name) {
			case "soulSteal":     return ParticleSpecialSword.create(soulRGB);
			case "zombieSummon":  return ParticleSpecialSword.create(zombieRGB);
			case "featherAttack": return ParticleSpecialSword.create(featherRGB);
			case "enderAttack":   return ParticleSpecialSword.create(enderRGB);
			case "blazeAttack":   return ParticleSpecialSword.create(blazeRGB);
			case "iceAttack":     return ParticleSpecialSword.create(iceRGB);
			case "electricAttack": return ParticleElectricAttack.create(electricAttackScale, electricRGB);
			case "underPortal":   return ParticleUnderPortal.create();
			default:              return null;
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static ParticleOptions vanillaByName(String name) {
		for (var entry : BuiltInRegistries.PARTICLE_TYPE.entrySet()) {
			ResourceLocation key = entry.getKey().location();
			if (key.getPath().equals(name) && entry.getValue() instanceof SimpleParticleType simple) {
				return simple;
			}
		}
		return null;
	}

}
