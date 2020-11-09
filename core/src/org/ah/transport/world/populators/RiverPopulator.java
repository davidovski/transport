package org.ah.transport.world.populators;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.util.Random;

import org.ah.transport.world.World;
import org.ah.transport.world.World.Decor;
import org.ah.transport.world.World.Tile;

import apple.laf.JRSUIConstants.Direction;

public class RiverPopulator extends WorldPopulator {

	public RiverPopulator(World world, long seed) {
		super(world, seed);
	}

	@Override
	public long populate() {
		long time = System.currentTimeMillis();
		int size = world.getSize();
		Random random = new Random(seed);

		int rivers = random.nextInt(5) + 1;
		for (int i = 0; i < rivers; i++) {
			int side = random.nextInt(4);
			int dist = random.nextInt(size);

			Point start = null;
			int weightx = 0;
			int weighty = 0;
			if (side == 0) {
				start = new Point(dist, 0);
				weighty = 1;
			} else if (side == 1) {
				start = new Point(dist, size - 1);
				weighty = -1;
			} else if (side == 2) {
				start = new Point(0, dist);
				weightx = 1;
			} else if (side == 3) {
				start = new Point(size - 1, dist);
				weightx = -1;
			}

			int x = start.x;
			int y = start.y;
			world.setTile(x, y, Tile.WATER);
			int cantgo = 3;
			while (x >= 0 && y >= 0 && x < size && y < size) {

				int option = random.nextInt(5); // 0forward // 1left // 2right // 3 backwards
				if (option > 2) {
					if (random.nextBoolean()) {
					if (cantgo == 1) {
						option = 2;
					} else if (cantgo == 2) {
						option = 1;
					} else if (cantgo == 3) {
						option = 0;
					} }else {
						option = 0;
					}
				}
				if (option == cantgo) {
					if (random.nextBoolean()) {
						option += 1;
					} else {
						option -= 1;
					}
					option %= 3;
				}

				if (option == 1) {
					cantgo = 2;
				} else if (option == 2) {
					cantgo = 1;
				} else {
					cantgo = 3;
				}

				if (option == 0) {
					x += weightx;
					y += weighty;
				} else if (option == 1) {
					x += weighty;
					y += weightx;
				} else if (option == 2) {
					x -= weighty;
					y -= weightx;
				}

//				if (random.nextInt(size) == 0) {
//					int w = weightx;
//					weightx = weighty;
//					weighty = weightx;
//							
//				}
				if (world.getTile(x, y) == Tile.WATER) {
					break;
				} else {
					world.setTile(x, y, Tile.WATER);
				}
			}
		}
		return System.currentTimeMillis() - time;
	}

	@Override
	public String getName() {
		return "RiverPopulator";
	}

}
