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
    private double minVal = 0.0;
    private double maxVal = Double.MAX_VALUE;

    public NormalizedColorCalculator(MapCanvas canvas, boolean colorModeUsed) {
        this.canvas = canvas;
        maxScore = canvas.calculateMaxScore();
        minVal = canvas.getConfig().getMinScoringValue();
        maxVal = canvas.getConfig().getMaxScoringValue();
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
        final int score = (int) category.calculateEnabledScoreValue(pt);
        if (score > maxScore) {
            throw new IllegalArgumentException("Norm-calculation fails! Score: " + score + " of point: "
                    + pt + " is higher as the maxScore: " + maxScore + ".");
        }
        return (score * COLOR_MAX_VAL) / maxScore;
    }

    public Color calculateColor(Point2D pt) {
        return colorModeUsed ? normedColorForEnabled(pt) : colorForEnabled(pt);
    }

    private Color normedColorForEnabled(Point2D pt) {
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
