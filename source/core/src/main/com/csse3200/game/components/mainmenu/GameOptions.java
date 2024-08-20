package com.csse3200.game.components.mainmenu;

import com.csse3200.game.components.Component;

/**
 * Stores options chosen by the user for a new game. Includes difficulty selection, may include
 * more options later.
 */
public class GameOptions extends Component {

    private Difficulty difficulty;

    /**
     * Create a new GameOptions component.
     */
    public GameOptions() {
        this.difficulty = Difficulty.MEDIUM;
    }

    /**
     * Get the chosen difficulty.
     * @return the chosen difficulty.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Set the chosen difficulty.
     * @param newDifficulty the new difficulty.
     */
    public void setDifficulty(Difficulty newDifficulty) {
        this.difficulty = newDifficulty;
    }

    /**
     * The difficulty of the game. Will likely affect map creation (number of rooms). May affect
     * other features in the future.
     */
    public enum Difficulty {
        EASY {
            @Override
            public String toString() {
                return "Easy";
            }
        }, MEDIUM {
            @Override
            public String toString() {
                return "Medium";
            }
        }, HARD {
            @Override
            public String toString() {
                return "Hard";
            }
        }
    }
}
