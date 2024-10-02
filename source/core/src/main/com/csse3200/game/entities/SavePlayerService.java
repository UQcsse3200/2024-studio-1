package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;

/**
 * A Class to handle saving character state into a json file so that a player
 * can be created/loaded from previous game
 */
public class SavePlayerService {

    /**
     * Calls file writer to save player's assets into a player_save.json file
     * under Assets/configs using a config generator
     *
     * @param player an entity whose assets need to be saved
     */
    public void savePlayerState(Entity player) {
        PlayerConfigGenerator generator = new PlayerConfigGenerator();
        PlayerConfig config = generator.savePlayerState(player);
        FileLoader.writeClass(config, "configs/player_save.json", FileLoader.Location.EXTERNAL);
    }

}
