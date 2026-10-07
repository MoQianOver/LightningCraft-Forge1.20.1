package org.lightningcraft.proxy;

import org.lightningcraft.config.LCConfig;
import org.lightningcraft.crafting.LCCraftingManager;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.init.LCCapabilities;
import org.lightningcraft.init.LCCreativeTabs;
import org.lightningcraft.init.LCDimensions;
import org.lightningcraft.init.LCEnchantments;
import org.lightningcraft.init.LCEntities;
import org.lightningcraft.init.LCEvents;
import org.lightningcraft.init.LCFluids;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.init.LCMenus;
import org.lightningcraft.init.LCNetwork;
import org.lightningcraft.init.LCPotions;
import org.lightningcraft.init.LCSoundEvents;
import org.lightningcraft.init.LCStructures;
import org.lightningcraft.init.LCTileEntities;
import org.lightningcraft.integration.LCModIntegration;
import org.lightningcraft.ref.Log;
import org.lightningcraft.ref.RefStrings;

public class CommonProxy {

	public void preInit() {
		Log.logger.info("PreInitializing " + RefStrings.NAME + "...");

		LCCreativeTabs.mainRegistry();
		LCBlocks.createBlocks();
		LCItems.createItems();
		LCFluids.createFluids();
		LCEntities.mainRegistry();
		LCNetwork.mainRegistry();

		LCTileEntities.mainRegistry();
		LCMenus.mainRegistry();
		LCSoundEvents.mainRegistry();
		LCEnchantments.mainRegistry();
		LCPotions.mainRegistry();
		LCDimensions.mainRegistry();
		LCStructures.mainRegistry();
	}

	public void onInit() {
		Log.logger.info("Initializing " + RefStrings.NAME + "...");

		LCConfig.load();

		LCEvents.mainRegistry();

		LCItems.finalizeMaterials();
		LCCreativeTabs.updateCreativeTabs();
		LCCraftingManager.addSmeltingRecipes();
		LCModIntegration.preInit();
		LCModIntegration.onInit();
	}

	public void postInit() {
		Log.logger.info("PostInitializing " + RefStrings.NAME + "...");
		LCModIntegration.postInit();
	}

	public void registerItemModels() {}

}
