package com.csse3200.game.entities.configs;

import com.csse3200.game.options.GameOptions.Difficulty;

import java.util.ArrayList;
import java.util.List;

public class MapLoadConfig {
    public List<String> roomsCompleted = new ArrayList<>();
    public List<String> shopRoomItems = new ArrayList<>();
    public String currentLevel = "0";
    public String currentRoom;
    public String seed = "seed";
    public int mapSize = SIZE_ON_MEDIUM;

    private static final int SIZE_ON_MEDIUM = 20;

    /**
     * Set map size based on difficulty. Harder difficulty corresponds to larger map size.
     * @param difficulty difficulty chosen by player.
     * @return this instance
     */
    public MapLoadConfig setSizeFromDifficulty(Difficulty difficulty) {
        // 10 on easy, 20 on medium, 30 on hard
        mapSize = (int) (40 * (1.25 - difficulty.getMultiplier()));
        if (mapSize <= 0) {
            throw new IllegalStateException(
                    "Difficulty is too easy and caused an invalid map size");
        }
        return this;
    }
}
