package org.ah.transport.util.noise;


public class NoiseSeaLevel extends AbstractNoise {

    private Noise noise;
    private float seaLevel;

    public NoiseSeaLevel(Noise noise, float seaLevel) {
        this.noise = noise;
        this.seaLevel = seaLevel;
    }

    @Override
    public float noise(float x, float y, float z) {
        float n = noise.noise(x, y, z);
        if (n < seaLevel) { n = seaLevel; };
        return n;
    }

    public static void main(String[] args) throws Exception {
        NoiseSeaLevel noise = new NoiseSeaLevel(new ClassicPerlinNoise(), 16f);
    }
}
