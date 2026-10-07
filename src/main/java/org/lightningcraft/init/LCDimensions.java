package org.lightningcraft.init;

import com.mojang.serialization.Codec;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.lightningcraft.LightningCraft;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.dimensions.ChunkGeneratorUnderworld;

public class LCDimensions {

	public static final String underworldName = "Underworld";

	public static int underworldID() {
		return LCConfig.underworldDimensionID;
	}

	public static final ResourceKey<Level> UNDERWORLD_KEY = ResourceKey.create(Registries.DIMENSION,
			new ResourceLocation(LightningCraft.MODID, "underworld"));

	public static final RegistryObject<Codec<? extends ChunkGenerator>> UNDERWORLD_GENERATOR =
			LCRegistry.CHUNK_GENERATORS.register("underworld", () -> ChunkGeneratorUnderworld.CODEC);

	public static void mainRegistry() {

	}

}
