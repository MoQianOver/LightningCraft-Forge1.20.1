package org.lightningcraft.integration;

import net.minecraftforge.fml.ModList;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.integration.energy.EnergyApiHelper;
import org.lightningcraft.ref.Log;

public class LCModIntegration {

	public static void preInit() {

		if(ModList.get().isLoaded("tconstruct") && LCConfig.tinkersIntegration) {
			Log.logger.info("Tinker's Construct found, but TiC3 integration is not ported yet - skipped.");
		} else if(LCConfig.tinkersIntegration) {
			Log.logger.info("Tinker's Construct not found, fluid integration skipped.");
		} else {
			Log.logger.info("Tinker's Construct fluid integration disabled.");
		}
	}

	public static void onInit() {

		if(ModList.get().isLoaded("tconstruct") && LCConfig.tinkersIntegration) {
			Log.logger.info("Tinker's Construct found, but TiC3 tool integration is not ported yet - skipped.");
		} else if(LCConfig.tinkersIntegration) {
			Log.logger.info("Tinker's Construct not found, tool integration skipped.");
		} else {
			Log.logger.info("Tinker's Construct tool integration disabled.");
		}

		if(EnergyApiHelper.rfOrTeslaLoaded) {
			if(EnergyApiHelper.rfLoaded) Log.logger.info("Forge Energy compatibility enabled");
			if(EnergyApiHelper.teslaLoaded) Log.logger.info("TESLA API found, compatibility enabled");
		} else {
			if(LCConfig.RFIntegration) {
				Log.logger.info("TESLA not found.");
			} else {
				Log.logger.info("Forge Energy and TESLA disabled in config.");
			}
		}

		if(LCConfig.chiselIntegration) {
			Log.logger.info("Chisel integration is intentionally not ported - skipped.");
		}

	}

	public static void postInit() {}

}
