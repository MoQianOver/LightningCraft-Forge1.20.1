package org.lightningcraft;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCCapabilities;
import org.lightningcraft.init.LCRegistry;
import org.lightningcraft.proxy.ClientProxy;
import org.lightningcraft.proxy.CommonProxy;
import org.lightningcraft.ref.RefStrings;

@Mod(LightningCraft.MODID)
public class LightningCraft {

	public static final String MODID = RefStrings.MODID;

	public static CommonProxy proxy;

	public LightningCraft() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LCConfig.SPEC,
				RefStrings.MODID + "-common.toml");
		modBus.register(LCConfig.class);

		LCRegistry.register(modBus);

		proxy = createProxy();
		proxy.preInit();

		modBus.register(LCCapabilities.class);
		modBus.register(org.lightningcraft.init.LCEntities.class);

		modBus.addListener(this::onCommonSetup);
		modBus.addListener(this::onLoadComplete);
	}

	private static CommonProxy createProxy() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			return new ClientProxy();
		}
		return new CommonProxy();
	}

	private void onCommonSetup(FMLCommonSetupEvent event) {
		proxy.onInit();
	}

	private void onLoadComplete(FMLLoadCompleteEvent event) {
		proxy.postInit();
	}

}
