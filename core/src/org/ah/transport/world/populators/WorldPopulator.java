package org.ah.transport.world.populators;

import org.ah.transport.world.World;

public abstract class WorldPopulator {
	public World world;
	public long seed;
	

	public WorldPopulator(World world, long seed) {
		this.world = world;
		this.seed = seed;
	}
	
	public abstract String getName();
	
	public abstract long populate();
	
}
