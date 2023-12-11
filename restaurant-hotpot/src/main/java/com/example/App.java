package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("primary.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 980, 560);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
}

    public static void main(String[] args) {
        launch(new String[0]);
    }
}