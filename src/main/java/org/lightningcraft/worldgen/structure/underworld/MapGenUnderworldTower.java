package org.lightningcraft.worldgen.structure.underworld;

import com.mojang.serialization.Codec;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.lightningcraft.init.LCStructures;
import org.lightningcraft.ref.RefMisc;
import org.lightningcraft.worldgen.structure.Feature;
import org.lightningcraft.worldgen.structure.MapGenLCStructure;

public class MapGenUnderworldTower extends MapGenLCStructure {

	public static final Codec<MapGenUnderworldTower> CODEC = MapGenLCStructure.simpleCodec(MapGenUnderworldTower::new);

	public MapGenUnderworldTower(Structure.StructureSettings settings) {
		super(settings, 12, 6, 0);
	}

	@Override
	protected Feature getStructurePiece(long seed, int x, int z) {
		if (RefMisc.DEBUG) {
			System.out.println("Attempting structure start: " + LCStructures.UNDERWORLD_TOWER);
		}
		RandomSource rand = RandomSource.create(seed + (long) x * 341873128712L + (long) z * 132897987541L);
		return new UnderworldTower(rand, x * 16, z * 16);
	}

	@Override
	public StructureType<?> type() {
		return LCStructures.UNDERWORLD_TOWER.get();
	}

}
