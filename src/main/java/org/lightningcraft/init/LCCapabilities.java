package org.lightningcraft.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lightningcraft.api.capabilities.ILightningUpgradable;

public class LCCapabilities {

	public static final Capability<ILightningUpgradable> LIGHTNING_UPGRADABLE =
			CapabilityManager.get(new CapabilityToken<ILightningUpgradable>() {});

	public static void mainRegistry() {

	}

	@SubscribeEvent
	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.register(ILightningUpgradable.class);
	}

}