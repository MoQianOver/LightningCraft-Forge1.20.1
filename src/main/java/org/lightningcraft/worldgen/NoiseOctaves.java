package org.lightningcraft.worldgen;

import java.util.Arrays;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public class NoiseOctaves {

	private final ImprovedNoise[] generators;
	private final int octaves;

	public NoiseOctaves(RandomSource seed, int octavesIn) {
		this.octaves = octavesIn;
		this.generators = new ImprovedNoise[octavesIn];
		for (int i = 0; i < octavesIn; ++i) {

			this.generators[i] = new ImprovedNoise(seed);
		}
	}

	public double[] generateNoiseOctaves(double[] noiseArray, int xo, int yo, int zo,
			int xs, int ys, int zs, double xScale, double yScale, double zScale) {
		if (noiseArray == null) {
			noiseArray = new double[xs * ys * zs];
		} else {
			Arrays.fill(noiseArray, 0.0D);
		}

		double freq = 1.0D;
		for (int oct = 0; oct < this.octaves; ++oct) {

			double ox = xo * freq * xScale;
			double oy = yo * freq * yScale;
			double oz = zo * freq * zScale;
			long ix = (long) Math.floor(ox);
			long iz = (long) Math.floor(oz);
			ox -= ix;
			oz -= iz;
			ix %= 16777216L;
			iz %= 16777216L;
			ox += ix;
			oz += iz;

			double ax = xScale * freq;
			double ay = yScale * freq;
			double az = zScale * freq;

			int idx = 0;
			for (int x = 0; x < xs; ++x) {
				for (int z = 0; z < zs; ++z) {
					for (int y = 0; y < ys; ++y) {
						noiseArray[idx++] += this.generators[oct].noise(
								(ox + x) * ax,
								(oy + y) * ay,
								(oz + z) * az) * freq;
					}
				}
			}

			freq /= 2.0D;
		}
		return noiseArray;
	}

	public int getOctaves() {
		return this.octaves;
	}

}
