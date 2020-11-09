package org.ah.transport.util.noise;


public class IncreaseFrequencyNoise extends AbstractNoise {

    private Noise noise;
    private float frequency;
    private float seed;

    public IncreaseFrequencyNoise(Noise noise, float frequency, float seed) {
        this.noise = noise;
        this.frequency = frequency;
        this.seed = seed;
    }

    @Override
    public float noise(float x, float y, float z) {
        return noise.noise(x * frequency, y * frequency, z * frequency);
    }

    public static void main(String[] args) throws Exception {
        IncreaseFrequencyNoise noise = new IncreaseFrequencyNoise(new ClassicPerlinNoise(), 16f, 0.1f);
    }
}
