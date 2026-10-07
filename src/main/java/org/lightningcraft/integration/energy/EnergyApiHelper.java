package org.lightningcraft.integration.energy;

import net.minecraftforge.fml.ModList;

public class EnergyApiHelper {

	public static boolean rfLoaded = true;

	public static boolean teslaLoaded = isModLoaded("tesla");

	public static boolean rfOrTeslaLoaded = rfLoaded || teslaLoaded;

	public static boolean rfAndTeslaLoaded = rfLoaded && teslaLoaded;

	private static boolean isModLoaded(String id) {
		try {
			return ModList.get() != null && ModList.get().isLoaded(id);
		} catch (Throwable t) {
			return false;
		}
	}

}
