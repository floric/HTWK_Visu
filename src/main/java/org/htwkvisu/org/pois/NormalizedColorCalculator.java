package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.htwkvisu.gui.MapCanvas;

public class NormalizedColorCalculator {

    private static final int COLOR_MAX_VAL = 255;
    private MapCanvas canvas;
    private boolean colorModeUsed;
    private double minVal = 0.0;
    private double maxVal = Double.MAX_VALUE;

    public NormalizedColorCalculator(MapCanvas canvas) {
        this.canvas = canvas;
        ScoringCalculator.updateMaxScoreValue(canvas);
        Category.EDUCATION.updateMaxScoreValue(canvas);
        Category.HEALTH.updateMaxScoreValue(canvas);
        Category.INFRASTRUCTURE.updateMaxScoreValue(canvas);
        minVal = canvas.getConfig().getMinScoringValue();
        maxVal = canvas.getConfig().getMaxScoringValue();
        this.colorModeUsed = canvas.isColorModeActive();
    }

    public double normEnabled(Category category, Point2D pt) {
        final int score = (int) category.calculateEnabledScoreValue(pt);
        final int maxScoreValue = category.getMaxScoreValue();
        if (score > maxScoreValue) {
            throw new IllegalArgumentException("Norm-calculation fails! Score: " + score + " of point: "
                    + pt + " is higher as the maxScore: " + maxScoreValue + ".");
        }
        return (score * COLOR_MAX_VAL) / maxScoreValue;
    }

    public Color calculateColor(Point2D pt) {
        return colorModeUsed ? normedColorForEnabled(pt) : colorForEnabled(pt);
    }

    public Color normedColorForEnabled(Point2D pt) {
        return Color.rgb((int) normEnabled(Category.EDUCATION, pt)
                , (int) normEnabled(Category.HEALTH, pt)
                , (int) normEnabled(Category.INFRASTRUCTURE, pt));
    }

    private Color colorForEnabled(Point2D pt) {
        double value = ScoringCalculator.calculateEnabledScoreValue(pt);
        double hue;

        if (value < minVal) {
            hue = Color.BLUE.getHue();
        } else if (value > maxVal) {
            hue = Color.RED.getHue();
        } else {
            hue = Color.BLUE.getHue() + (Color.RED.getHue() - Color.BLUE.getHue())
                    * (value - minVal)
                    / (maxVal - minVal);
        }
        return Color.hsb(hue, 1.0, 1.0);
    }
}
