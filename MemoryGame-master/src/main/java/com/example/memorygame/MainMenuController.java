package com.example.memorygame;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private void handleStartGame(ActionEvent event) throws IOException {

        // โหลด FXML ของ Memory Game
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Theme.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // นำ Stage (หน้าต่างปัจจุบัน) มาจาก ActionEvent
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // เปลี่ยน Scene ไปที่ Memory Game
        stage.setTitle("Memory Card Game");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void handleHowtoplay(ActionEvent event) {
        System.out.println("How to play...");
        // Add logic to display High Scores
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.out.println("Exiting Game...");
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
