package org.lightningcraft.ref;

import net.minecraft.network.chat.Component;

public final class Lang {

	private Lang() {}

	public static String tr(String key) {
		return Component.translatable(key).getString();
	}

	public static String tr(String key, Object... args) {
		return Component.translatable(key, args).getString();
	}
}