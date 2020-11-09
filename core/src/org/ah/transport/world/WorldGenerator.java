package org.ah.transport.world;

public abstract class WorldGenerator {
	public World world;
	public long seed;

	public WorldGenerator(World world, long seed) {
		this.world = world;
		this.seed = seed;
	}
	
	public abstract long generate();
	
	public abstract String getName();
	
}
