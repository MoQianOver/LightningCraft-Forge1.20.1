package org.lightningcraft.biomes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.lightningcraft.init.LCEntities;

public class BiomeUnderworld {

	public static final int NATURE_COLOR = 0x2C5A80;

	public static Biome create() {

		MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
		spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
				LCEntities.DEMON_SOLDIER.get(), 100, 2, 4));
		spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
				LCEntities.UNDERWORLD_CREEPER.get(), 30, 1, 2));
		spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
				LCEntities.UNDERWORLD_SLIME.get(), 20, 1, 1));
		spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
				EntityType.CAVE_SPIDER, 40, 1, 3));
		spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
				LCEntities.UNDERWORLD_GHAST.get(), 20, 1, 1));

		BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
				.waterColor(0)
				.waterFogColor(0x050533)
				.fogColor(0x0B0B0B)
				.skyColor(0)
				.grassColorOverride(NATURE_COLOR)
				.foliageColorOverride(NATURE_COLOR);

		return new Biome.BiomeBuilder()
				.hasPrecipitation(false)
				.temperature(2.0F)
				.downfall(0.0F)
				.specialEffects(effects.build())
				.mobSpawnSettings(spawns.build())

				.build();
	}

}
