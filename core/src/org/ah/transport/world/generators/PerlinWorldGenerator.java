package org.ah.transport.world.generators;

import java.util.Random;

import org.ah.transport.util.noise.ClassicPerlinNoise;
import org.ah.transport.world.World;
import org.ah.transport.world.WorldGenerator;
import org.ah.transport.world.World.Decor;
import org.ah.transport.world.World.Tile;

public class PerlinWorldGenerator extends WorldGenerator {

	public PerlinWorldGenerator(World world, long seed) {
		super(world, seed);
	}

	@Override
	public long generate() {
		long time = System.currentTimeMillis();
		int size = world.getSize();
		Random random = new Random(seed);
		ClassicPerlinNoise noise = new ClassicPerlinNoise();
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				float d = 5;
				double n = noise.noise(x / d, y / d, seed);
				if (n > -0.2) {
					world.setTile(x, y, Tile.GRASS);
				} else {
					world.setTile(x, y, Tile.WATER);

				}
			}
		}
		return System.currentTimeMillis() - time;
	}

	@Override
	public String getName() {
		return "Simple";
	}

}
