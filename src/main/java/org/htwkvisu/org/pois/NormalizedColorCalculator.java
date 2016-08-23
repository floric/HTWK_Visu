package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.htwkvisu.gui.MapCanvas;

import java.util.List;
import java.util.stream.Collectors;

public class NormalizedColorCalculator {

    private static final int COLOR_MAX_VAL = 255;
    private MapCanvas canvas;
    private int maxScore;
    private boolean colorModeUsed;

    public NormalizedColorCalculator(MapCanvas canvas, boolean colorModeUsed) {
        this.canvas = canvas;
        maxScore = canvas.calculateMaxScore(); //TODO: fix calculateMaxScore to fix the red color
        this.colorModeUsed = colorModeUsed;
    }

    public List<Double> norm() {
        return canvas.calculateGrid().stream()
                .map(pt -> (ScoringCalculator.calculateScoreValue(pt) * COLOR_MAX_VAL) / maxScore).collect(Collectors.toList());
    }

    public List<Double> normEnabled() {
        return canvas.calculateGrid().stream()
                .map(pt -> (ScoringCalculator.calculateEnabledScoreValue(pt) * COLOR_MAX_VAL) / maxScore).collect(Collectors.toList());
    }

    public List<Double> norm(Category category) {
        return canvas.calculateGrid().stream()
                .map(pt -> (category.calculateScoreValue(pt) * COLOR_MAX_VAL) / maxScore).collect(Collectors.toList());
    }

    public List<Double> normEnabled(Category category) {
        return canvas.calculateGrid().stream()
                .map(pt -> (category.calculateEnabledScoreValue(pt) * COLOR_MAX_VAL) / maxScore).collect(Collectors.toList());
    }

    public double normEnabled(Category category, Point2D pt) {
        return (category.calculateEnabledScoreValue(pt) * COLOR_MAX_VAL) / maxScore;
    }

    public Color calculateColor(Point2D pt) {
        return colorModeUsed ? normedColorForEnabled(pt) : colorForEnabled(pt);
    }

    private Color normedColorForEnabled(Point2D pt) {
        return Color.rgb((int) normEnabled(Category.EDUCATION, pt)
                , (int) normEnabled(Category.HEALTH, pt)
                , (int) normEnabled(Category.INFRASTRUCTURE, pt));
    }

    //TODO: this could be normalized too
    private Color colorForEnabled(Point2D pt) {
        double value = ScoringCalculator.calculateEnabledScoreValue(pt);
        double hue;
        final double minVal = canvas.getConfig().getMinScoringValue();
        final double maxVal = canvas.getConfig().getMaxScoringValue();

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
