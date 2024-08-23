package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;


public class SavePlayerService {
    public void savePlayerState(Entity player) {
        FileLoader.writeClass(new PlayerConfig(player), "configs/player_save.json");
    }
}
