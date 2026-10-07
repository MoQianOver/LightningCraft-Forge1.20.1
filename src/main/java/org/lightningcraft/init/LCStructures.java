package org.lightningcraft.init;

import java.util.function.Supplier;

import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.RegistryObject;
import org.lightningcraft.worldgen.structure.underworld.MapGenUnderworldRampart;
import org.lightningcraft.worldgen.structure.underworld.MapGenUnderworldTower;
import org.lightningcraft.worldgen.structure.underworld.MapGenUnderworldWaterTemple;
import org.lightningcraft.worldgen.structure.underworld.UnderworldRampart;
import org.lightningcraft.worldgen.structure.underworld.UnderworldTower;
import org.lightningcraft.worldgen.structure.underworld.UnderworldWaterTemple;

public class LCStructures {

	public static final String underworldWaterTempleName = "UnderworldWaterTemple";
	public static final String underworldTowerName = "UnderworldTower";
	public static final String underworldRampartName = "UnderworldRampart";

	public static final String heavenRuinsName = "HeavenRuins";
	public static final String heavenKeepName = "HeavenKeep";

	public static final RegistryObject<StructureType<MapGenUnderworldTower>> UNDERWORLD_TOWER =
			LCRegistry.STRUCTURE_TYPES.register("underworld_tower",
					(Supplier<StructureType<MapGenUnderworldTower>>) () -> () -> MapGenUnderworldTower.CODEC);

	public static final RegistryObject<StructureType<MapGenUnderworldRampart>> UNDERWORLD_RAMPART =
			LCRegistry.STRUCTURE_TYPES.register("underworld_rampart",
					(Supplier<StructureType<MapGenUnderworldRampart>>) () -> () -> MapGenUnderworldRampart.CODEC);

	public static final RegistryObject<StructureType<MapGenUnderworldWaterTemple>> UNDERWORLD_WATER_TEMPLE =
			LCRegistry.STRUCTURE_TYPES.register("underworld_water_temple",
					(Supplier<StructureType<MapGenUnderworldWaterTemple>>) () -> () -> MapGenUnderworldWaterTemple.CODEC);

	public static final RegistryObject<StructurePieceType> UNDERWORLD_TOWER_PIECE =
			LCRegistry.STRUCTURE_PIECES.register("underworld_tower",
					() -> (ctx, tag) -> new UnderworldTower(ctx, tag));

	public static final RegistryObject<StructurePieceType> UNDERWORLD_RAMPART_PIECE =
			LCRegistry.STRUCTURE_PIECES.register("underworld_rampart",
					() -> (ctx, tag) -> new UnderworldRampart(ctx, tag));

	public static final RegistryObject<StructurePieceType> UNDERWORLD_WATER_TEMPLE_PIECE =
			LCRegistry.STRUCTURE_PIECES.register("underworld_water_temple",
					() -> (ctx, tag) -> new UnderworldWaterTemple(ctx, tag));

	public static void mainRegistry() {

	}

	@SuppressWarnings("unused")
	private static <T> Supplier<T> identity(Supplier<T> s) {
		return s;
	}

}
