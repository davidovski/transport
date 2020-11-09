package org.ah.transport.util.noise;


public class NoiseWithOctaves extends AbstractNoise {

    private Noise noise;
    private int octaves = 8;
    private float gain = 0.5f;
    private float lacunarity = 2.0f;

    public NoiseWithOctaves(Noise noise) {
        this.noise = noise;
    }

    public NoiseWithOctaves(Noise noise, int octaves, float gain, float lacunarity) {
        this.noise = noise;
        this.octaves = octaves;
        this.gain = gain;
        this.lacunarity = lacunarity;
    }

    @Override
    public float noise(float x, float y, float z) {
        float amplitude = 1.0f;
        float frequency = 1.0f;
        float sum = 0.0f;
        for(int i = 0; i < octaves; ++i) {
            sum += amplitude * noise.noise(x * frequency, y * frequency, z * frequency);
            amplitude = amplitude * gain;
            frequency = frequency * lacunarity;
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        Noise perlin = new ClassicPerlinNoise();
        Noise octaves = new NoiseWithOctaves(perlin);

    }
}
