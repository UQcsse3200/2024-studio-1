package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerSelection {

    /**
     * List of player json files.
     */
    public static final String[] PLAYERS = {
            "configs/player.json",
            "configs/player_2.json",
            "configs/player_3.json",
            "configs/necromancer.json",
            "configs/player_4.json"
    };

    /**
     * Get all player configs from the given filenames.
     * @param configFilenames the files that contain the config info (json).
     * @return map of config filename to config.
     */
    public static Map<String, PlayerConfig> getPlayerConfigs(List<String> configFilenames) {
        return configFilenames.stream()
                .collect(Collectors.toMap(
                        filename -> filename,
                        filename -> FileLoader.readClass(PlayerConfig.class, filename)
                ));
    }
}
