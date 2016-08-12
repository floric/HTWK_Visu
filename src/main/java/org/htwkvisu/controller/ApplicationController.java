package org.htwkvisu.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.gui.NumericTextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Control for GUI changes for App
 */
public class ApplicationController implements Initializable {

    @FXML
    private NumericTextField pixelDensityTextField;

    private static final int DEFAULT_PIXEL_DENSITY = 50;
    private static final int MAX_NUMERIC_FIELD_LENGTH = 5;

    @FXML
    private Button resetViewButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Pane canvasPane;

    private MapCanvas canvas;

    /**
     * Write message to status bar.
     *
     * @param message            Message to show
     * @param isImportantMessage Show the message in red if it's important
     */
    public void writeStatusMessage(String message, boolean isImportantMessage) {
        if (isImportantMessage) {
            messageLabel.setTextFill(Color.RED);
        } else {
            messageLabel.setTextFill(Color.GRAY);
        }

        messageLabel.setText(message);
    }

    /**
     * Default JavaFX initilization method for controllers.
     * Implemented from Initializable-interface.
     *
     * @param location Location
     * @param resources Resources
     */
    public void initialize(URL location, ResourceBundle resources) {

        initCanvas();

        pixelDensityTextField.setText(Integer.toString(DEFAULT_PIXEL_DENSITY));
        pixelDensityTextField.setMaxlength(MAX_NUMERIC_FIELD_LENGTH);
        pixelDensityTextField.setDefaultValue(DEFAULT_PIXEL_DENSITY);

        Logger.getGlobal().log(Level.INFO, "ApplicationController initialized!");
    }

    private void initCanvas() {
        canvas = new MapCanvas();
        canvas.setSamplingPixelDensity(DEFAULT_PIXEL_DENSITY);
        canvasPane.getChildren().add(canvas);
        canvasPane.widthProperty().addListener((observable, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        canvasPane.heightProperty().addListener((observable, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
    }


    /**
     * Center to view to default point.
     *
     * @param ev Mouse click event
     */
    @FXML
    public void onResetViewClicked(MouseEvent ev) {
        canvas.centerView(new Point2D(51, 13)); // test value as an example!
    }

    /**
     * Refreshes Values
     *
     * @param ev Key Event
     */
    @FXML
    public void onEnterPressed(KeyEvent ev) {
        if(ev.getCode().equals(KeyCode.ENTER)){

            if(pixelDensityTextField.getText().isEmpty()){
                pixelDensityTextField.setText(Integer.toString(pixelDensityTextField.getDefaultValue()));
            }

            canvas.setSamplingPixelDensity(Integer.parseInt(pixelDensityTextField.getText()));
        }
    }

}
