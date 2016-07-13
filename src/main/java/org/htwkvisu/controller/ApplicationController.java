package org.htwkvisu.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by floric on 7/12/16.
 */
public class ApplicationController implements Initializable {

    @FXML
    public Button resetViewButton;

    @FXML
    public Label messageLabel;

    @FXML
    public AnchorPane canvasPane;

    public void initialize(URL location, ResourceBundle resources) {
        messageLabel.setText("TEst");
    }
}
