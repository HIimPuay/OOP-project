package com.example.memorygame;

public class MemoryCard extends Card {
    private boolean matched;
    private String themeName;

    public MemoryCard(String suit, String faceName) {
        super(suit, faceName);
        this.matched = false;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public String getThemeName() {
        return this.themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    /**
     * This method returns true if the 2 MemoryCard objects
     * have the same suit and faceName
     */
    public boolean isSameCard(MemoryCard otherCard) {
        return (this.getSuit().equals(otherCard.getSuit()) &&
                (this.getFaceName().equals(otherCard.getFaceName())));
    }

}
