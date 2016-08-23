package org.htwkvisu.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoringConfig {

    private int samplingPixelDensity;
    private int minScoringValue;
    private int maxScoringValue;
    private InterpolationMode interpMode = InterpolationMode.BILINEAR;
    private ScoringCanvas canvas;

    public enum InterpolationMode {
        BILINEAR,
        BICUBIC
    }

    public ScoringConfig(int samplingPixelDensity, int minScoringValue, int maxScoringValue) {
        this.samplingPixelDensity = samplingPixelDensity;
        this.minScoringValue = minScoringValue;
        this.maxScoringValue = maxScoringValue;
    }

    public int getMinScoringValue() {
        return minScoringValue;
    }

    public int getMaxScoringValue() {
        return maxScoringValue;
    }

    public int getSamplingPixelDensity() {
        return samplingPixelDensity;
    }

    public ScoringCanvas getCanvas() {
        return canvas;
    }

    public void setMinScoringValue(int minScoringValue) {
        if (minScoringValue >= 0 && minScoringValue < maxScoringValue) {
            this.minScoringValue = minScoringValue;
            canvas.redraw();
        } else {
            Logger.getGlobal().log(Level.INFO, "Min Score should be greater zero and lesser Max Score!");
        }
    }

    public void setSamplingPixelDensity(int samplingPixelDensity) {
        if (samplingPixelDensity > 0) {
            this.samplingPixelDensity = samplingPixelDensity;
            canvas.redraw();
        } else {
            Logger.getGlobal().log(Level.INFO, "Pixel Density should be greater zero!");
        }
    }

    public void setMaxScoringValue(int maxScoringValue) {
        if (maxScoringValue > minScoringValue) {
            this.maxScoringValue = maxScoringValue;
            canvas.redraw();
        } else {
            Logger.getGlobal().log(Level.INFO, "Max Score should be greater Min Score!");
        }
    }

    public void setInterpolationMode(InterpolationMode mode) {
        this.interpMode = mode;
    }

    public InterpolationMode getInterpolationMode() {
        return interpMode;
    }

    public void setCanvas(ScoringCanvas canvas) {
        this.canvas = canvas;
    }

    /**
     * This method changes the value of the given field on the given config fields.
     * <p>
     * If the count of the config fields increase, a list or a container object will be a better parameter
     * - instead of all textFields.
     *
     * @param field      - the field, that must be changed
     * @param pixelField - pixelDensityTextField
     * @param minField   - minScoringTextField
     * @param maxField   - maxScoringTextField
     */
    public void changeValueOfNumericScoringTextField(NumericTextField field
            , NumericTextField pixelField, NumericTextField minField, NumericTextField maxField) {
        if (field.getText().isEmpty()) {
            field.setText(Integer.toString(field.getDefaultValue()));
        }

        int value = Integer.parseInt(field.getText());

        if (pixelField.equals(field)) {
            setSamplingPixelDensity(value);
            field.setText(Integer.toString(getSamplingPixelDensity()));
        } else if (minField.equals(field)) {
            setMinScoringValue(value);
            field.setText(Integer.toString(getMinScoringValue()));
        } else if (maxField.equals(field)) {
            setMaxScoringValue(value);
            field.setText(Integer.toString(getMaxScoringValue()));
        }
    }
}
