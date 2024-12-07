package com.example.memorygame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThemeController {

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
        startGameWithLevel(event, 4);
    }

    private void startGameWithLevel(ActionEvent event, int level) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("memory-game.fxml"));
            Parent root = fxmlLoader.load();
            MemoryGameController controller = fxmlLoader.getController();
            controller.setLevel(level);

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

    private void updateGameCards(List<MemoryCard> newCards) {
        cardsInGame = new ArrayList<>(newCards);
        Collections.shuffle(cardsInGame);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void addCardSet(ActionEvent event) {
        // Ask for a theme name
        TextInputDialog dialog = new TextInputDialog("CustomTheme");
        dialog.setTitle("Add Card Set");
        dialog.setHeaderText("Enter a theme name for your cards.");
        dialog.setContentText("Theme Name:");

        String themeName = dialog.showAndWait().orElse("DefaultTheme");

        // FileChooser to allow user to select multiple image files
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        List<File> selectedFiles = fileChooser
                .showOpenMultipleDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            ArrayList<MemoryCard> newCards = new ArrayList<>();
            try {
                // Process each selected file
                for (File file : selectedFiles) {
                    String savedPath = ImageStorage.saveCardImage(file); // Save image to custom-images folder
                    MemoryCard card = new MemoryCard(themeName, savedPath);
                    card.setCustomImage(new javafx.scene.image.Image("file:" + savedPath)); // Set image

                    // Duplicate the card for matching
                    MemoryCard duplicateCard = new MemoryCard(themeName, savedPath);
                    duplicateCard.setCustomImage(new javafx.scene.image.Image("file:" + savedPath)); // Set image

                    // Add the original and duplicate cards to the list
                    newCards.add(card);
                    newCards.add(duplicateCard);
                }

                // Add new cards to the game
                customCards.addAll(newCards);
                updateGameCards(newCards);

                showAlert(Alert.AlertType.INFORMATION, "Success", "Cards added successfully for theme: " + themeName);

                // Start the game with the new cards
                startCustomGame(event, themeName);

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save images!");
                e.printStackTrace();
            }
        } else {
            System.out.println("No files selected.");
        }
    }

    private void startCustomGame(ActionEvent event, String themeName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("memory-game.fxml"));
            Parent root = fxmlLoader.load();
            MemoryGameController controller = fxmlLoader.getController();

            controller.setTheme(themeName);
            controller.setCardsInGame(cardsInGame);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Memory Game - Theme: " + themeName);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load memory-game.fxml!");
            e.printStackTrace();
        }
    }
}
