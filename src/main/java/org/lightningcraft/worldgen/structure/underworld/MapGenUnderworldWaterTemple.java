package org.lightningcraft.worldgen.structure.underworld;

import com.mojang.serialization.Codec;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.lightningcraft.init.LCStructures;
import org.lightningcraft.ref.RefMisc;
import org.lightningcraft.worldgen.structure.Feature;
import org.lightningcraft.worldgen.structure.MapGenLCStructure;

public class MapGenUnderworldWaterTemple extends MapGenLCStructure {

	public static final Codec<MapGenUnderworldWaterTemple> CODEC = MapGenLCStructure.simpleCodec(MapGenUnderworldWaterTemple::new);

	public MapGenUnderworldWaterTemple(Structure.StructureSettings settings) {
		super(settings, 10, 7, 0);
	}

	@Override
	protected Feature getStructurePiece(long seed, int x, int z) {
		if (RefMisc.DEBUG) {
			System.out.println("Attempting structure start: " + LCStructures.UNDERWORLD_WATER_TEMPLE);
		}
		RandomSource rand = RandomSource.create(seed + (long) x * 341873128712L + (long) z * 132897987541L);
		return new UnderworldWaterTemple(rand, x * 16, z * 16);
	}

	@Override
	public StructureType<?> type() {
		return LCStructures.UNDERWORLD_WATER_TEMPLE.get();
	}

}
