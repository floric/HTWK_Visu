package org.htwkvisu.gui;

import javafx.scene.control.TextField;

import java.util.Optional;

public class NumericTextField extends TextField {

    private static final int MAX_NUMERIC_FIELD_LENGTH = 6;
    private static final int ZERO = 0;
    private static final String NUMBER_REGEX = "[0-9]";
    private int maxlength;

    private Optional<Integer> defaultValue;

    public NumericTextField() {
        this.maxlength = 10;
        this.defaultValue = Optional.empty();

        this.focusedProperty().addListener(x -> {
            if (getText().length() == ZERO && this.defaultValue.isPresent()) {
                replaceText(ZERO, ZERO, this.defaultValue.get().toString());
            }
        });
    }

    public void init(final int value) {
        setMaxlength(MAX_NUMERIC_FIELD_LENGTH);
        setDefaultValue(value);
        setText(Integer.toString(value));
    }

    private void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    private void setDefaultValue(int value) {
        if (value < ZERO) {
            throw new IllegalArgumentException("Default Value must be greater or equal zero");
        }
        this.defaultValue = Optional.of(value);
    }

    public int getDefaultValue() {
        return this.defaultValue.orElse(0);
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (this.validate(start, end, text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if ("".equals(text)) {
            super.replaceSelection(text);
        }

        if (text.matches(NUMBER_REGEX) && this.getText().length() < maxlength) {
            super.replaceSelection(text);
        }
    }

    private boolean validate(int start, int end, String text) {
        if ("".equals(text)) {
            return true;
        }

        if (!text.matches(NUMBER_REGEX)) {
            return false;
        }

        if (this.getText().length() < maxlength) {
            return true;
        }

        return getText().length() - (end - start) + text.length() <= this.maxlength;
    }
}