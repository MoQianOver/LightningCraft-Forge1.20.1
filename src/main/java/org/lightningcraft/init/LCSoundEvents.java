package org.lightningcraft.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import org.lightningcraft.ref.RefStrings;

public class LCSoundEvents {

	public static void mainRegistry() {
		registerSounds();
	}

	public static SoundEvent demonSoldierIdle;
	public static SoundEvent demonSoldierHurt;
	public static SoundEvent demonSoldierDeath;
	public static SoundEvent underworldGhastMoan;
	public static SoundEvent underworldGhastWarn;
	public static SoundEvent underworldGhastHurt;
	public static SoundEvent underworldGhastDeath;
	public static SoundEvent portalUnderworld;

	private static void registerSounds() {
		demonSoldierIdle = registerSound("mob.demon_soldier.idle");
		demonSoldierHurt = registerSound("mob.demon_soldier.hurt");
		demonSoldierDeath = registerSound("mob.demon_soldier.death");
		underworldGhastMoan = registerSound("mob.underworld_ghast.moan");
		underworldGhastWarn = registerSound("mob.underworld_ghast.warn");
		underworldGhastHurt = registerSound("mob.underworld_ghast.hurt");
		underworldGhastDeath = registerSound("mob.underworld_ghast.death");
		portalUnderworld = registerSound("portal.underworld");
	}

	private static SoundEvent registerSound(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(RefStrings.MODID, soundName);
		SoundEvent e = SoundEvent.createVariableRangeEvent(soundID);
		LCRegistry.SOUNDS.register(soundName, () -> e);
		return e;
	}

}