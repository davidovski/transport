package org.ah.transport.util.noise;

public abstract class AbstractNoise implements Noise {

    private StringBuilder stringBuilder;

    protected void clearStringBuilder() {
        stringBuilder = new StringBuilder();
    }

    protected void println(String s) {
        stringBuilder.append(s);
        stringBuilder.append("\n");
    }

    protected String finishBuffer() {
        return stringBuilder.toString();
    }
}
