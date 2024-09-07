package com.csse3200.game.entities;

import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.Entity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerSelection {
    private PlayerFactory playerFactory1;
    private PlayerFactory playerFactory2;

    public List<Entity> createTwoPlayers() {
        this.playerFactory1 = new PlayerFactory(List.of("configs/player.json"));
        Entity player1 = playerFactory1.createPlayer();
        this.playerFactory2 = new PlayerFactory(List.of("configs/player_2.json"));
        Entity player2 = playerFactory2.createPlayer();
        List<Entity> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        return players;
    }
}
