package org.lightningcraft.init;

import net.minecraftforge.common.MinecraftForge;
import org.lightningcraft.events.CombatEvents;
import org.lightningcraft.events.EntityItemEvents;
import org.lightningcraft.events.PlayerEvents;
import org.lightningcraft.events.ToolEvents;

public class LCEvents {

	public static void mainRegistry() {
		registerEvents();
	}

	private static void registerEvents() {
		MinecraftForge.EVENT_BUS.register(new PlayerEvents());

		MinecraftForge.EVENT_BUS.register(new CombatEvents());
		MinecraftForge.EVENT_BUS.register(new ToolEvents());
		MinecraftForge.EVENT_BUS.register(new EntityItemEvents());
	}

}
