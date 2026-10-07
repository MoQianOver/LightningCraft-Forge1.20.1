package org.lightningcraft.api.registry;

import java.util.Random;

import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ILightningCraftObject {

	public static final Random random = new Random();
	public static final Rarity DYNAMIC = null;

	@OnlyIn(Dist.CLIENT)
	public default void registerRender() {}

	public default boolean showInCreativeTab() { return true; }

}
