package com.example.memorygame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThemeController {

    private ArrayList<MemoryCard> defaultCards = new ArrayList<>();
    private ArrayList<MemoryCard> customCards = new ArrayList<>();
    private ArrayList<MemoryCard> cardsInGame = new ArrayList<>();

    @FXML
    void handleStartLevel1(ActionEvent event) {
        startGameWithLevel(event, 1);
    }

    @FXML
    void handleStartLevel2(ActionEvent event) {
        startGameWithLevel(event, 2);
    }

    @FXML
    void handleStartLevel3(ActionEvent event) {
        startGameWithLevel(event, 3);
    }

    @FXML
    void handleStartLevel4(ActionEvent event) {
        startGameWithLevel(event, 4); // ส่งข้อมูลด่าน 4
    }

    private void startGameWithLevel(ActionEvent event, int level) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("memory-game.fxml"));
            Parent root = fxmlLoader.load();
            MemoryGameController controller = fxmlLoader.getController();
            controller.setLevel(level); // ส่งระดับด่านไปยัง Controller

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Memory Game");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading memory-game.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleBackToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));
        Parent root = fxmlLoader.load();
        Scene mainMenuScene = new Scene(root, 800, 600);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(mainMenuScene);
        stage.show();
    }

    // add card function จะทำให้เลือกภาพจากเครื่องเราได้
    // อยากทำให้เชื่อมโยงกับโฟลเดอร์ custom-images และ user.home
    // ที่สร้างไว้เพื่อเก็บภาพการ์ดลงไป
    @FXML
    private void addCardSet(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Card Images");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(
                ((Node) event.getSource()).getScene().getWindow());

        if (selectedFiles == null || selectedFiles.size() % 2 != 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an even number of images!");
            return;
        }

        for (File file : selectedFiles) {
            String imagePath = file.toURI().toString();
            MemoryCard card = new MemoryCard("Custom", imagePath);
            card.setCustomImage(new Image(imagePath));

            MemoryCard duplicateCard = new MemoryCard("Custom", imagePath);
            duplicateCard.setCustomImage(new Image(imagePath));

            customCards.add(card);
            customCards.add(duplicateCard);
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Custom card set added successfully!");
    }

    @FXML
    void startGameWithCustomOnly(ActionEvent event) {
        updateGameCards(customCards);
        playAgain();
    }

    @FXML
    void startGameWithDefaultAndCustom(ActionEvent event) {
        ArrayList<MemoryCard> combinedCards = new ArrayList<>(defaultCards);
        combinedCards.addAll(customCards);
        updateGameCards(combinedCards);
        playAgain();
    }

    private void updateGameCards(List<MemoryCard> selectedCards) {
        cardsInGame = new ArrayList<>(selectedCards);
        Collections.shuffle(cardsInGame);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void playAgain() {
        // Logic สำหรับเริ่มเกมใหม่ (เรียกจาก Controller หลัก)
        System.out.println("New game started with " + cardsInGame.size() + " cards.");
    }
}
