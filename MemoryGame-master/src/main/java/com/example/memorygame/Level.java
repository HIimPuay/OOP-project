package com.example.memorygame;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int levelNumber;
    private List<Card> cards;

    public Level(int levelNumber, List<Card> cards) {
        this.levelNumber = levelNumber;
        this.cards = new ArrayList<>(cards);
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public List<Card> getCards() {
        return cards;
    }
}
