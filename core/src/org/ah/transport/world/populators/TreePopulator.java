package org.ah.transport.world.populators;

import java.util.Random;

import org.ah.transport.world.World;
import org.ah.transport.world.World.Decor;
import org.ah.transport.world.World.Tile;

public class TreePopulator extends WorldPopulator {

	public TreePopulator(World world, long seed) {
		super(world, seed);
	}

	@Override
	public long populate() {
		long time = System.currentTimeMillis();
		int size = world.getSize();
		Random random = new Random(seed);
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Decor t = Decor.NONE;
				if (world.getTile(x, y) == Tile.GRASS) {
					if (random.nextInt(5) == 0) {
						int tree = random.nextInt(3);
						if (tree == 0) {
							if (random.nextBoolean()) {
								t = Decor.CONIFER_SHORT;
							} else {
								t = Decor.CONIFER_TALL;
							}
						} else if (tree == 1) {
							if (random.nextBoolean()) {
								t = Decor.AUTUMN_SHORT;
							} else {
								t = Decor.AUTUMN_TALL;
							}
						} else if (tree == 2) {
							if (random.nextBoolean()) {
								t = Decor.TREE_SHORT;
							} else {
								t = Decor.TREE_TALL;
							}
						}
					}
				}
				world.setDecor(x, y, t);

			}
		}
		return System.currentTimeMillis() - time;
	}

	@Override
	public String getName() {
		return "TreePopulator";
	}

}
