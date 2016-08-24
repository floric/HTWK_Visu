package org.htwkvisu.gui.interpolate;

import javafx.scene.paint.Color;

public interface InterpolationStrategy {
    Color interpolate(InterpolateConfig config);
}
