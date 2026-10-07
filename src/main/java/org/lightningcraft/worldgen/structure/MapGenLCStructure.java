package org.lightningcraft.worldgen.structure;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.lightningcraft.ref.RefMisc;

public abstract class MapGenLCStructure extends Structure {

	protected final int maxDistanceBetweenStructures;
	protected final int minDistanceBetweenStructures;
	protected final int seedDistance;

	protected MapGenLCStructure(StructureSettings settings,
			int maxDistanceBetweenStructures, int minDistanceBetweenStructures, int seedModifier) {
		super(settings);
		this.maxDistanceBetweenStructures = maxDistanceBetweenStructures;
		this.minDistanceBetweenStructures = minDistanceBetweenStructures;
		this.seedDistance = 14357617 + seedModifier;
	}

	protected boolean canSpawnStructureAtCoords(long seed, int x, int z) {
		int k = x;
		int l = z;

		if (x < 0) {
			x -= this.maxDistanceBetweenStructures - 1;
		}

		if (z < 0) {
			z -= this.maxDistanceBetweenStructures - 1;
		}

		int i1 = x / this.maxDistanceBetweenStructures;
		int j1 = z / this.maxDistanceBetweenStructures;
		RandomSource random = RandomSource.create(
				seed + (long) i1 * 341873128712L + (long) j1 * 132897987541L + this.seedDistance);
		i1 *= this.maxDistanceBetweenStructures;
		j1 *= this.maxDistanceBetweenStructures;
		i1 += random.nextInt(this.maxDistanceBetweenStructures - this.minDistanceBetweenStructures);
		j1 += random.nextInt(this.maxDistanceBetweenStructures - this.minDistanceBetweenStructures);

		return k == i1 && l == j1;
	}

	protected abstract Feature getStructurePiece(long seed, int x, int z);

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();

		if (RefMisc.DEBUG) {
			System.out.println("Structure gen possible for chunk (" + chunkPos.x + "," + chunkPos.z + ")");
		}

		return Optional.of(new Structure.GenerationStub(chunkPos.getWorldPosition(), builder -> {
			Feature piece = this.getStructurePiece(context.seed(), chunkPos.x, chunkPos.z);
			if (piece != null) {
				builder.addPiece(piece);
			}

			org.lightningcraft.ref.Log.logger.info("[LC-STRUCT] findGenerationPoint "
					+ this.getClass().getSimpleName() + " chunk=(" + chunkPos.x + "," + chunkPos.z + ")"
					+ " piece=" + (piece == null ? "null" : piece.getClass().getSimpleName()
							+ " box=" + piece.getBoundingBox()));
		}));
	}

	@Override
	public abstract StructureType<?> type();

	public static <S extends Structure> Codec<S> simpleCodec(
			java.util.function.Function<Structure.StructureSettings, S> factory) {
		return Structure.simpleCodec(factory);
	}

	@SuppressWarnings("unused")
	private static BlockPos unused() {
		return null;
	}

}
