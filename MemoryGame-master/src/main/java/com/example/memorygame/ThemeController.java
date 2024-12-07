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
import com.example.memorygame.ImageStorage;

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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Theme Name");
        dialog.setHeaderText("Enter a name for your theme");
        dialog.setContentText("Theme Name:");

        String themeName = dialog.showAndWait().orElse(null);
        if (themeName == null || themeName.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Theme name cannot be empty!");
            return;
        }

        for (MemoryCard card : customCards) {
            if (card.getThemeName().equalsIgnoreCase(themeName)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Theme '" + themeName + "' already exists!");
                return;
            }
        }

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

        ArrayList<MemoryCard> newCards = new ArrayList<>();
        ArrayList<String> savedImagePaths = new ArrayList<>();
        try {
            for (File file : selectedFiles) {
                String imagePath = ImageStorage.saveImage(file); // ใช้ ImageStorage เพื่อบันทึกไฟล์
                savedImagePaths.add(imagePath);

                MemoryCard card = new MemoryCard(themeName, imagePath);
                card.setCustomImage(new Image(new File(imagePath).toURI().toString()));

                MemoryCard duplicateCard = new MemoryCard(themeName, imagePath);
                duplicateCard.setCustomImage(new Image(new File(imagePath).toURI().toString()));

                newCards.add(card);
                newCards.add(duplicateCard);
            }
            customCards.addAll(newCards);
            updateGameCards(newCards);
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Theme '" + themeName + "' added successfully! Images saved to: " +
                            (ImageStorage.IS_DEVELOPMENT_MODE
                                    ? ImageStorage.DEV_IMAGE_DIRECTORY
                                    : ImageStorage.PROD_IMAGE_DIRECTORY));

            startCustomGame(event, themeName);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save images!");
            for (String path : savedImagePaths) {
                new File(path).delete();
            }
            e.printStackTrace();
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
