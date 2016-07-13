package org.htwkvisu.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.*;

/**
 *  Main App
 *  @author floric
 */
public class App extends Application {

    private static final String WINDOW_TITLE = "Lifequality Visualization";
    private static final Point2D WINDOW_SIZE = new Point2D(800, 600);
    private static final String LOG_FILENAME = "lifequality.log";

    /**
     * Main method
     *
     * @param args Program arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /** Start method, called by main-method, overwritten from Application class
     *
     * @param primaryStage Stage of window
     * @throws Exception Ex
     */
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ApplicationView.fxml"));
        Scene scene = new Scene(root, WINDOW_SIZE.getX(), WINDOW_SIZE.getY());

        // application specific initializations
        initApp();

        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initApp() {
        initLogging();
    }

    private void initLogging() {
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILENAME);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());


            Logger.getGlobal().addHandler(fileHandler);

            Logger.getGlobal().log(Level.INFO, "Logger initialized!");
        } catch (IOException e) {
            Logger.getGlobal().addHandler(new ConsoleHandler());
            Logger.getGlobal().log(Level.WARNING, "Log file output IO error!");
        }


    }
}
