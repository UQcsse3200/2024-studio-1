package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.PlayerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlayerSelection {
    /**
     * List of player json files.
     */
    public static final String[] PLAYERS = {
            "configs/player.json",
            "configs/player_2.json",
            "configs/player_3.json",
            "configs/player_4.json"
    };

    /**
     * Get all player configs from the given filenames.
     * @param configFilenames the files that contain the config info (json).
     * @return map of config filename to config.
     */
    public static Map<String, PlayerConfig> getPlayerConfigs(List<String> configFilenames) {
        return new PlayerFactory(Arrays.stream(PLAYERS).toList()).getOptions();
    }
}
