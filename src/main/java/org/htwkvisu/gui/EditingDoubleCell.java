package org.htwkvisu.gui;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.logging.Logger;

public class EditingDoubleCell extends TableCell<Double, Double> {

    private TextField textField;

    private DecimalFormat format;
    private final double minAcceptedValue;

    public EditingDoubleCell(double minAcceptedValue) {
        this.minAcceptedValue = minAcceptedValue;
        String pattern = "###.###";
        format = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
        format.applyPattern(pattern);
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.requestFocus();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(format.format(getItem()));
        setGraphic(null);
    }

    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private String getString() {
        return getItem() == null ? "" : format.format(getItem());
    }

    private void createTextField() {

        textField = new TextField();

        StringConverter<Double> converter = new StringConverter<Double>() {

            @Override
            public String toString(Double number) {
                return format.format(number);
            }

            @Override
            public Double fromString(String string) {
                try {
                    return format.parse(string).doubleValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return minAcceptedValue;
                }
            }

        };

        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, change ->
        {
            if (change.getControlNewText().isEmpty()) {
                return change;
            }
            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(change.getControlNewText(), parsePosition);
            if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
                return null;
            } else {
                return change;
            }
        });

        // add filter to allow for typing only integer
        textField.setTextFormatter(textFormatter);
        textField.setText(getString());

        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        // commit on Enter
        textFormatter.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue >= minAcceptedValue) {
                commitEdit(newValue);
                Logger.getGlobal().info("Set new value: " + newValue);
            } else {
                commitEdit(minAcceptedValue);
                Logger.getGlobal().warning("Replace new value:" + newValue + ", with default value: " + minAcceptedValue);
            }
        });
    }
}
