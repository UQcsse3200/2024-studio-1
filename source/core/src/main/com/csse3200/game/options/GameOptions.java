package com.csse3200.game.options;

/**
 * Stores options chosen by the user for a new game. Includes difficulty selection, may include
 * more options later.
 */
public class GameOptions {

    /** The difficulty of the game. */
    public Difficulty difficulty;
    /**
     * The path to the json file of the selected player.
     */
    public String chosenPlayer;

    /**
     * The difficulty of the game. Will likely affect map creation (number of rooms). May affect
     * other features in the future.
     */
    public enum Difficulty {
        EASY, MEDIUM, HARD, TEST;

        @Override
        public String toString() {
            // this/THIS/tHiS -> This
            return this.name().substring(0, 1).toUpperCase()
                    + this.name().substring(1).toLowerCase();
        }
    }
}
