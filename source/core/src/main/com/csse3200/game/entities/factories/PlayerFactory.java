package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.options.GameOptions.Difficulty;


/**
 * Factory to create a player entity.
 * <p>
 * Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public interface PlayerFactory {
    /**
     * Create a new player, and scale them for a particular difficulty
     *
     * @param difficulty the difficulty level this player plays at.
     * @return the Player entity.
     */
    Entity create(Difficulty difficulty);
}
