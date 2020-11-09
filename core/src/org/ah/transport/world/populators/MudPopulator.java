package org.ah.transport.world.populators;

import java.awt.Point;
import java.util.Random;

import org.ah.transport.world.World;
import org.ah.transport.world.World.Decor;
import org.ah.transport.world.World.Tile;

public class MudPopulator extends WorldPopulator {

	public MudPopulator(World world, long seed) {
		super(world, seed);
	}

	@Override
	public long populate() {
		long time = System.currentTimeMillis();
		int size = world.getSize();
		Random random = new Random(seed);

		int patches = (random.nextInt(10) + 5) * size/128;
		for (int i = 0; i < patches; i++) {
			int cx = random.nextInt(size);
			int cy = random.nextInt(size);
			int radius = random.nextInt(6) + 3;
			Point centre = new Point(cx, cy);
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					if (world.getTile(x, y) == Tile.GRASS) {
						Point p = new Point(x, y);

						if (centre.distance(p) < radius) {
							world.setTile(x, y, Tile.DIRT);
						}
					}
						
				}
			}
		}
		return System.currentTimeMillis() - time;
	}

	@Override
	public String getName() {
		return "MudPopulator";
	}

}
