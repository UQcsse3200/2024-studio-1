package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.files.FileLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerSelection {
    private PlayerFactory playerFactory1;
    private PlayerFactory playerFactory2;
    private PlayerFactory playerFactory3;

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
        return configFilenames.stream()
                .collect(Collectors.toMap(
                        filename -> filename,
                        filename -> FileLoader.readClass(PlayerConfig.class, filename)
                ));
    }

    public List<Entity> createTwoPlayers() {
        Entity player1 = playerFactory1.createPlayer();
        this.playerFactory2 = new PlayerFactory(List.of("configs/player_2.json"));
        Entity player2 = playerFactory2.createPlayer();
        List<Entity> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        return players;
    }
}