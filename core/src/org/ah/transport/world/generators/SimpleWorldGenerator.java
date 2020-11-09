package org.ah.transport.world.generators;

import java.util.Random;

import org.ah.transport.world.World;
import org.ah.transport.world.WorldGenerator;
import org.ah.transport.world.World.Decor;
import org.ah.transport.world.World.Tile;

public class SimpleWorldGenerator extends WorldGenerator {

	public SimpleWorldGenerator(World world, long seed) {
		super(world, seed);
	}

	@Override
	public long generate() {
		long time = System.currentTimeMillis();
		int size = world.getSize();
		Random random = new Random(seed);

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
					world.setTile(x, y, Tile.GRASS);
			}
		}
		return System.currentTimeMillis() - time;
	}

	@Override
	public String getName() {
		return "Simple";
	}

}
