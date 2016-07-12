package org.htwkvisu.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 */
public class App extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org.htwkvisu.view/ApplicationView.fxml"));
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Lifequality Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
