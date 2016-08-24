package org.htwkvisu.gui.interpolate;

import javafx.scene.paint.Color;

public enum InterpolationMode {
    BILINEAR(new LinearInterpolationStrategy()),
    BICUBIC(new BiCubicInterpolationStrategy());

    private InterpolationStrategy strategy;

    InterpolationMode(InterpolationStrategy strategy) {

        this.strategy = strategy;
    }

    public Color interpolateColor(InterpolateConfig config) {
        return this.strategy.interpolate(config);
    }

    public InterpolationMode next() {
        InterpolationMode[] values = InterpolationMode.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(this)) {
                if (i == 1) {
                    System.out.println("error here");
                }
                return values[(i + 1) % (values.length)];
            }

        }
        throw new RuntimeException("No interpolationMode found");
    }
}
