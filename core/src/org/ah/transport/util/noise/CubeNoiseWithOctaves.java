package org.ah.transport.util.noise;


public class CubeNoiseWithOctaves extends AbstractNoise {

    private Noise noise;
    private int octaves = 8;
    private float gain = 0.5f;
    private float lacunarity = 2.0f;
    private int power;
    private int[] skipOctaves = null;

    public CubeNoiseWithOctaves(Noise noise) {
        this.noise = noise;
    }

    public CubeNoiseWithOctaves(Noise noise, int octaves, int power, float gain, float lacunarity) {
        this(noise, octaves, power, gain, lacunarity, null);
    }

    public CubeNoiseWithOctaves(Noise noise, int octaves, int power, float gain, float lacunarity, int[] skipOctaves) {
        this.noise = noise;
        this.octaves = octaves;
        this.power = power;
        this.gain = gain;
        this.lacunarity = lacunarity;
        this.skipOctaves = skipOctaves;
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
    

    private void calcSkipOctaves() {
        StringBuilder s = new StringBuilder();
        s.append("    if (");
        boolean first = true;
        for (int i : skipOctaves) {
            if (first) { first = false; } else { s.append(" && "); }
            s.append("i != " + i);
        }
        s.append(") {");
        println(s.toString());
    }

    private String power(String v, int p) {
        StringBuilder s = new StringBuilder();
        s.append(v);
        for (int i = 1; i < p; i++) {
            s.append(" * ").append(v);
        }
        return s.toString();
    }

    public static void main(String[] args) throws Exception {
        Noise perlin = new ClassicPerlinNoise();
        Noise octaves = new CubeNoiseWithOctaves(perlin);

    }

}
