package org.example.oop2024_30423_ghile_patricia_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        //load the login window first
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/org/example/oop2024_30423_ghile_patricia_project/login.fxml"));
        Scene loginScene = new Scene(loginLoader.load());

        //set the title for the login window
        primaryStage.setTitle("Login");

        //set the scene to the stage
        primaryStage.setScene(loginScene);

        //show the login window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}