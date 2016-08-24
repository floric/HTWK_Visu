package org.htwkvisu.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

    private static final int FALL_OF_COLUMN_INDEX = 3;
    private static final int CATEGORY_COLUMN_INDEX = 1;
    private static final int DOUBLE_CLICK = 2;

    @FXML
    private CheckBox colorModeCheckBox;
    @FXML
    private CheckBox autoScaledCheckBox;
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

    @FXML
    private NumericTextField minScoringTextField;
    private static final int DEFAULT_MIN_SCORING_VALUE = 0;

    @FXML
    private NumericTextField maxScoringTextField;
    private static final int DEFAULT_MAX_SCORING_VALUE = 100000;

    @FXML
    private Button changeInterpMode;

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
        pixelDensityTextField.init(DEFAULT_PIXEL_DENSITY);
        minScoringTextField.init(DEFAULT_MIN_SCORING_VALUE);
        maxScoringTextField.init(DEFAULT_MAX_SCORING_VALUE);

        initTable();
        changeInterpMode.setText(config.getInterpolationMode().toString());
        Logger.getGlobal().log(Level.INFO, "ApplicationController initialized!");

    }

    private void initTable() {

        enabled.setCellValueFactory(new PropertyValueFactory<>("enabled"));
        enabled.setCellFactory(CheckBoxTableCell.forTableColumn(enabled));

        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        fallOfColumn.setCellValueFactory(new PropertyValueFactory<>("fallOf"));

        radiusColumn.setCellValueFactory(new PropertyValueFactory<>("paramOne"));
        radiusColumn.setCellFactory(f -> new EditingDoubleCell(0));

        maximumColumn.setCellValueFactory(new PropertyValueFactory<>("paramTwo"));
        maximumColumn.setCellFactory(f -> new EditingDoubleCell(0));

        exponentColumn.setCellValueFactory(new PropertyValueFactory<>("paramThree"));
        exponentColumn.setCellFactory(f -> new EditingDoubleCell(0));

        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        weightColumn.setCellFactory(f -> new EditingDoubleCell(1));

        List<ScoreTableModel> tableModels = ScoringCalculator.calcAllTableModels();
        tableView.setItems(FXCollections.observableList(tableModels));

        // deprecated, but easiest way to disable reorderable columns...,
        // alternative were impl. of ListChangedlistener..
        for (TableColumn col : tableView.getColumns()) {
            col.impl_setReorderable(false);
        }

        tableView.setOnMouseClicked(click -> {
            if (click.getClickCount() == DOUBLE_CLICK) {
                //TODO: If clicked outside of table, last value will be changed...
                ScoreTableModel model = tableView.getSelectionModel().getSelectedItem();
                final int column = tableView.getFocusModel().getFocusedCell().getColumn();

                if (column == FALL_OF_COLUMN_INDEX) {
                    // model must be set the fallOf because of the event listening
                    model.setFallOf(model.switchAndGetFallOfFromType(model.getFallOf()));
                } else if (column == CATEGORY_COLUMN_INDEX) {
                    final boolean newStatus = !model.getEnabled();
                    tableView.getItems().stream().
                            filter(scoreTableModel -> model.getCategory().equals(scoreTableModel.getCategory())).
                            forEach(scoreTableModel -> scoreTableModel.setEnabled(newStatus));
                    Logger.getGlobal().info("Set enabled of category: " + model.getCategory() + ", value: " + newStatus);
                }
            }
        });
    }

    private void initCanvas() {
        config = new ScoringConfig(DEFAULT_PIXEL_DENSITY, DEFAULT_MIN_SCORING_VALUE, DEFAULT_MAX_SCORING_VALUE);
        canvas = new MapCanvas(config);
        canvas.setColorModeCheckBox(colorModeCheckBox);
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

    @FXML
    public void onRedrawAction(ActionEvent ev) {
        if (autoScaledCheckBox.isSelected()) {
            int maxScoringValue = canvas.calculateMaxScore();
            config.setMaxScoringValue(maxScoringValue);
            Logger.getGlobal().info("New auto-scaled maxScoreValue: " + maxScoringValue);
        } else {
            canvas.redraw();
        }
    }

    @FXML
    public void onInterpModeChanged(ActionEvent ev) {
        config.setInterpolationMode(config.getInterpolationMode().next());
        changeInterpMode.setText(config.getInterpolationMode().toString());
        Logger.getGlobal().info("Interpolation Mode: " + config.getInterpolationMode());
        canvas.redraw();
    }

    @FXML
    public void onResetViewAction(ActionEvent ev) {
        canvas.centerView(MapCanvas.CITY_LEIPZIG);
    }

    @FXML
    public void onColorModeAction(ActionEvent ev) {
        autoScaledCheckBox.setDisable(colorModeCheckBox.isSelected());
    }
}
