package com.example.memorygame;

import javafx.scene.image.Image;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Card {
    private String suit;
    private String faceName;
    private Image customImage;

    public Card(String suit, String faceName) {
        setSuit(suit);
        setFaceName(faceName);
    }

    public String getSuit() {
        return suit;
    }

    public static List<String> getValidSuits() {
        return Arrays.asList("hearts", "diamonds", "clubs", "spades");
    }

    /**
     * valid suits are "hearts","diamonds","clubs","spades"
     * 
     * @param suit
     */
    public void setSuit(String suit) {
        suit = suit.toLowerCase();
        if (getValidSuits().contains(suit))
            this.suit = suit;
        else
            throw new IllegalArgumentException(suit + " invalid, must be one of " + getValidSuits());
    }

    public String getFaceName() {
        return faceName;
    }

    public static List<String> getValidFaceNames() {
        return Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace");
    }

    /**
     * valid face names are
     * "2","3","4","5","6","7","8","9","10","jack","queen","king","ace"
     * 
     * @param faceName
     */
    public void setFaceName(String faceName) {
        faceName = faceName.toLowerCase();
        if (getValidFaceNames().contains(faceName))
            this.faceName = faceName;
        else
            throw new IllegalArgumentException(faceName + " is invalid, must be one of " + getFaceName());
    }

    public String toString() {
        return faceName + " of " + suit;
    }

    public String getColour() {
        if (suit.equals("hearts") || suit.equals("diamonds"))
            return "red";
        else
            return "black";
    }

    /**
     * This method will return the value of the card
     * [ "2","3","4","5","6","7","8","9","10","jack","queen","king","ace" ]
     * 0 1 2 3 4 .... 11 12
     * +2
     *
     */
    public int getValue() {
        return getValidFaceNames().indexOf(faceName) + 2;
    }

    /**
     * This method will return an Image that represents the Card
     */
    public Image getCustomImage() {
        String customPath = Paths.get(
                System.getProperty("user.dir"),
                "src",
                "main",
                "resources",
                "com",
                "example",
                "memorygame",
                "custom-images",
                ".png").toString();
        File customFile = new File(customPath);
        if (customFile.exists()) {
            return new Image(customFile.toURI().toString());
        }
        return getBackOfCardImage(); // โหลดภาพสำรอง
    }

    public void setCustomCardImage(File imageFile) {
        if (imageFile != null && imageFile.exists()) {
            this.customImage = new Image(imageFile.toURI().toString());
        } else {
            throw new IllegalArgumentException("Invalid file for custom card image.");
        }
    }

    public Image getImage() {
        if (customImage != null) {
            return customImage;
        }

        // ตรวจสอบในโฟลเดอร์ custom-images
        String customPathName = System.getProperty("user.home") + "/MemoryGame/custom-images/" + faceName + "_of_"
                + suit + ".png";
        File customFile = new File(customPathName);
        if (customFile.exists()) {
            return new Image(customFile.toURI().toString());
        }

        String pathName = "images/" + faceName + "_of_" + suit + ".png";
        Image defaultImage = new Image(Card.class.getResourceAsStream(pathName));
        if (defaultImage.isError()) {
            // หากไม่สามารถโหลดภาพการ์ดได้ ให้โหลดภาพพื้นหลังแทน
            return getBackOfCardImage();
        }
        return defaultImage;
    }

    public Image getBackOfCardImage() {
        String pathName = System.getProperty("user.home") + "/MemoryGame/custom-images/back_of_card.png";
        File backImageFile = new File(pathName);

        if (backImageFile.exists()) {
            return new Image(backImageFile.toURI().toString());
        } else {
            // โหลดภาพ default จาก resource
            return new Image(Card.class.getResourceAsStream("images/back_of_card.png"));
        }
    }

}
