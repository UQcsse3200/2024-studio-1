package com.csse3200.game.options;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;

/**
 * Stores options chosen by the user for a new game. Includes difficulty selection, may include
 * more options later.
 */
public class GameOptions {

    /** The difficulty of the game. */
    private Difficulty difficulty;
    /**
     * The player factory that will produce the in-game player chosen by the user.
     */
    public PlayerFactory playerFactory;

    public String seed;
    /**
     * Get player-set difficulty.
     * @return difficulty chosen by player.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Set the difficulty. Should only happen once per game
     * @param difficulty difficulty chosen by player.
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Set the player factory after the user chose a player.
     * @param playerFactory player factory to use to make a player.
     */
    public void setPlayerFactory(PlayerFactory playerFactory) {
        this.playerFactory = playerFactory;
    }

    /**
     * Create a player as per the user's choice.
     * @param difficulty difficulty to scale the player for.
     * @return player entity.
     */
    public Entity createPlayer(Difficulty difficulty) {
        return playerFactory.create(difficulty);
    }

    /**
     * The difficulty of the game. Will likely affect map creation (number of rooms). May affect
     * other features in the future.
     */
    public enum Difficulty {
        EASY(1f), MEDIUM(0.75f), HARD(0.5f), TEST(1f);

        private final float multiplier;

        Difficulty(float multiplier) {
            this.multiplier = multiplier;
        }

        /**
         * Get multiplier associated with this difficulty value. Initial player health and speed
         * are adjusted based on this multiplier to make the game easier or harder.
         * @return multiplier associated with this difficulty value.
         */
        public float getMultiplier() {
            return multiplier;
        }

        @Override
        public String toString() {
            // this/THIS/tHiS -> This
            return this.name().substring(0, 1).toUpperCase()
                    + this.name().substring(1).toLowerCase();
        }
    }
}
