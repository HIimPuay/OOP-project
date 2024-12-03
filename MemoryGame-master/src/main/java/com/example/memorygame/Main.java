package com.example.memorygame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene mainMenuScene;

    @Override
    public void start(Stage stage) throws IOException {
        // โหลด FXML ไฟล์
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));
        Parent root = fxmlLoader.load();

        // สร้าง Scene หลัก
        mainMenuScene = new Scene(root, 800, 600);

        // กำหนดค่าให้ Stage
        stage.setTitle("Memory Card Game");
        stage.setScene(mainMenuScene);
        stage.show();
    }

    public static Scene getMainMenuScene() {
        return mainMenuScene;
    }

    public static void main(String[] args) {
        launch(args);
    }

}