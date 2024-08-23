package com.csse3200.game.entities;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;


public class SavePlayerService {


    public void savePlayerState(Entity player) {
        PlayerConfigGenerator generator = new PlayerConfigGenerator();
        FileLoader.writeClass(generator.savePlayerState(player), "configs/player_save.json");

    }


    private String[] itemsToString(Array<Collectible> items) {
        String[] itemNames = new String[items.size];
        for (int i = 0; i < items.size; i++) {
            itemNames[i] = items.get(i).getSpecification();
        }
        return itemNames;
    }
}
