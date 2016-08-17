package org.htwkvisu.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.htwkvisu.gui.EditingDoubleCell;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.gui.NumericTextField;
import org.htwkvisu.gui.ScoringConfig;
import org.htwkvisu.model.ScoreTableModel;
import org.htwkvisu.org.pois.Category;
import org.htwkvisu.org.pois.ScoreType;
import org.htwkvisu.org.pois.ScoringCalculator;
import org.htwkvisu.scoring.IFallOf;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Control for GUI changes for App
 */
public class ApplicationController implements Initializable {

    @FXML
    private TableColumn<Double, Double> weightColumn;
    @FXML
    private TableColumn<Double, Double> radiusColumn;
    @FXML
    private TableColumn<Double, Double> maximumColumn;
    @FXML
    private TableColumn<Double, Double> exponentColumn;

    @FXML
    private TableColumn<Boolean, Boolean> enabled;
    @FXML
    private TableColumn<Category, String> categoryColumn;
    @FXML
    private TableColumn<ScoreType, String> scoreColumn;
    @FXML
    public TableColumn<IFallOf, String> fallOfColumn;
    @FXML
    private NumericTextField pixelDensityTextField;

    @FXML
    private TableView<ScoreTableModel> tableView;

    private static final int DEFAULT_PIXEL_DENSITY = 30;
    private static final int MAX_NUMERIC_FIELD_LENGTH = 6;

    @FXML
    private NumericTextField minScoringTextField;
    private static final int DEFAULT_MIN_SCORING_VALUE = 0;

    @FXML
    private NumericTextField maxScoringTextField;
    private static final int DEFAULT_MAX_SCORING_VALUE = 100000;

    @FXML
    private Button resetViewButton;
    @FXML
    private Button redrawButton;
    @FXML
    private Label messageLabel;

    @FXML
    private Pane canvasPane;

    private MapCanvas canvas;
    private ScoringConfig config;

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
     * @param location  Location
     * @param resources Resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: please remove this - read property-file and set value from them
        Category.EDUCATION.setEnabledForCategory(true);

        initCanvas();
        initNumericTextFields(pixelDensityTextField, DEFAULT_PIXEL_DENSITY);
        initNumericTextFields(minScoringTextField, DEFAULT_MIN_SCORING_VALUE);
        initNumericTextFields(maxScoringTextField, DEFAULT_MAX_SCORING_VALUE);

        initTable();

        Logger.getGlobal().log(Level.INFO, "ApplicationController initialized!");

    }

    private void initTable() {

        enabled.setCellValueFactory(new PropertyValueFactory<>("enabled"));
        enabled.setCellFactory(CheckBoxTableCell.forTableColumn(enabled));

        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        fallOfColumn.setCellValueFactory(new PropertyValueFactory<>("fallOf"));
        //TODO setCellFactory

        radiusColumn.setCellValueFactory(new PropertyValueFactory<>("paramOne"));
        radiusColumn.setCellFactory(f-> new EditingDoubleCell());

        maximumColumn.setCellValueFactory(new PropertyValueFactory<>("paramTwo"));
        maximumColumn.setCellFactory(f-> new EditingDoubleCell());

        exponentColumn.setCellValueFactory(new PropertyValueFactory<>("paramThree"));
        exponentColumn.setCellFactory(f-> new EditingDoubleCell());

        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        weightColumn.setCellFactory(f-> new EditingDoubleCell());

        List<ScoreTableModel> tableModels = ScoringCalculator.calcAllTableModels();
        tableView.setItems(FXCollections.observableList(tableModels));

        //TODO: Current only TEST purpose
        tableView.setOnMouseClicked(click -> {

            if (click.getClickCount() == 2) {
                TablePosition pos = tableView.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                int col = pos.getColumn();

                TableColumn column = pos.getTableColumn();
                String val = column.getCellObservableValue(row).toString();
                Logger.getGlobal().info("Selected Value, " + val + ", Column: " + col + ", Row: " + row);
            }
        });

    }

    private void initNumericTextFields(NumericTextField numericTextField, final int value) {
        numericTextField.setMaxlength(MAX_NUMERIC_FIELD_LENGTH);
        numericTextField.setDefaultValue(value);
        numericTextField.setText(Integer.toString(value));
    }

    private void initCanvas() {
        config = new ScoringConfig(DEFAULT_PIXEL_DENSITY, DEFAULT_MIN_SCORING_VALUE, DEFAULT_MAX_SCORING_VALUE);
        canvas = new MapCanvas(config);
        canvasPane.getChildren().add(canvas);
        canvasPane.widthProperty().addListener((observable, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        canvasPane.heightProperty().addListener((observable, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
    }

    /**
     * Refreshes Values for textFields
     *
     * @param ev Key Event
     */
    @FXML
    public void onEnterPressed(KeyEvent ev) {
        if (ev.getCode().equals(KeyCode.ENTER)) {
            config.changeValueOfNumericScoringTextField(
                    (NumericTextField) ev.getSource(),
                    pixelDensityTextField, minScoringTextField, maxScoringTextField);
        }
    }

    /**
     * Redraw or Resets view of canvas
     * @param ev MouseEvent
     */
    @FXML
    public void onClicked(MouseEvent ev) {
        if (ev.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
           if(ev.getSource().equals(redrawButton)){
                canvas.redraw();
            }else if(ev.getSource().equals(resetViewButton)){
               canvas.centerView(new Point2D(51.340333, 12.37475)); // test value as an example!Leipzig
            }
        }
    }

}
