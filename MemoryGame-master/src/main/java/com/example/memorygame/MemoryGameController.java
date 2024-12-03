package com.example.memorygame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class MemoryGameController implements Initializable {

    @FXML
    private Label correctGuessesLabel;

    @FXML
    private Label guessLabel;

    @FXML
    private FlowPane imagesFlowPane;

    private ArrayList<MemoryCard> cardsInGame;

    private MemoryCard firstCard, secondCard;
    private int numOfGuess;
    private int numOfMatches;
    private int currentLevel;

    @FXML
    private Label titleLabel; // เชื่อมโยงกับ fx:id="titleLabel"

    // ฟังก์ชันนี้จะรับระดับด่านจาก ThemeController
    public void setLevel(int level) {
        this.currentLevel = level;
        adjustGameSettingsForLevel(level);
    }

    // ปรับการตั้งค่าของเกมตามระดับด่าน
    private void adjustGameSettingsForLevel(int level) {
        switch (level) {
            case 1:
                // ปรับการตั้งค่าเกมสำหรับด่าน 1 (ง่าย)
                titleLabel.setText("Level 1: Easy");
                System.out.println("Game started at Level 1 (Easy)");
                break;
            case 2:
                // ปรับการตั้งค่าเกมสำหรับด่าน 2 (กลาง)
                titleLabel.setText("Level 2: Medium");
                System.out.println("Game started at Level 2 (Medium)");
                break;
            case 3:
                // ปรับการตั้งค่าเกมสำหรับด่าน 3 (ยาก)
                titleLabel.setText("Level 3: Hard");
                System.out.println("Game started at Level 3 (Hard)");
                break;
            case 4:
                // ปรับการตั้งค่าเกมสำหรับด่าน 4 (เชี่ยวชาญ)
                titleLabel.setText("Level 4: Expert");
                System.out.println("Game started at Level 4 (Expert)");
                break;
            default:
                System.out.println("Unknown Level");
        }
    }

    @FXML
    void playAgain() {
        firstCard = null;
        secondCard = null;

        DeckOfCards deck = new DeckOfCards();
        deck.shuffle();
        cardsInGame = new ArrayList<>();

        for (int i = 0; i < imagesFlowPane.getChildren().size() / 2; i++) {
            Card cardDealt = deck.dealTopCard();
            cardsInGame.add(new MemoryCard(cardDealt.getSuit(), cardDealt.getFaceName()));
            cardsInGame.add(new MemoryCard(cardDealt.getSuit(), cardDealt.getFaceName()));
        }
        Collections.shuffle(cardsInGame);
        flipAllCards();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeImageView();
        playAgain();
    }

    /**
     * This will add a number to each ImageView and set the image to be the back of
     * a Card
     */
    private void initializeImageView() {
        for (int i = 0; i < imagesFlowPane.getChildren().size(); i++) {
            // "cast" the Node to be of type ImageView
            ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);
            imageView.setImage(new Image(Card.class.getResourceAsStream("images/back_of_card.png")));
            imageView.setUserData(i);

            // register a click listener
            imageView.setOnMouseClicked(event -> {
                flipCard((int) imageView.getUserData());
            });
        }
    }

    /**
     * This will show the back of all cards that are not matched
     */
    private void flipAllCards() {
        for (int i = 0; i < cardsInGame.size(); i++) {
            ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);
            MemoryCard card = cardsInGame.get(i);

            if (card.isMatched())
                imageView.setImage(card.getImage());
            else
                imageView.setImage(card.getBackOfCardImage());
        }
    }

    private void flipCard(int indexOfCard) {
        if (firstCard == null && secondCard == null)
            flipAllCards();

        ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(indexOfCard);

        if (firstCard == null) {
            firstCard = cardsInGame.get(indexOfCard);
            imageView.setImage(firstCard.getImage());
        } else if (secondCard == null) {
            numOfGuess++;
            secondCard = cardsInGame.get(indexOfCard);
            imageView.setImage(secondCard.getImage());
            checkForMatch();
            updateLabels();
        }
    }

    private void updateLabels() {
        correctGuessesLabel.setText(Integer.toString(numOfMatches));
        guessLabel.setText(Integer.toString(numOfGuess));
    }

    private void checkForMatch() {
        if (firstCard.isSameCard(secondCard)) {
            numOfMatches++;
            firstCard.setMatched(true);
            secondCard.setMatched(true);

            checkIfGameCompleted();
        }
        firstCard = null;
        secondCard = null;
    }

    private void checkIfGameCompleted() {
        // ถ้าจับคู่สำเร็จครบทุกคู่
        if (numOfMatches == cardsInGame.size() / 2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText("You Win!");
            alert.setContentText("You've matched all the cards!");

            // เพิ่มปุ่ม OK และตั้งค่าการกระทำเมื่อปิด Popup
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // ให้ผู้เล่นเล่นเกมใหม่
                    playAgain();
                }
            });
        }
    }

    private static Scene mainMenuScene;

    @FXML
    void handleBackTheme(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Theme.fxml"));

        Parent root = fxmlLoader.load();
        mainMenuScene = new Scene(root, 800, 600);
        // get the Stage object from the ActionEvent
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("Memory Card Game");
        stage.setScene(mainMenuScene);
        stage.show();
    }

}
