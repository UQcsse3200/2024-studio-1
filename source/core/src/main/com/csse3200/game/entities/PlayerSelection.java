package com.csse3200.game.entities;

import com.csse3200.game.entities.factories.PlayerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the creation of two characters for player selection in the game.
 * <p>
 * It creates two separate {@link Entity} objects, each representing a player, by using
 * different configuration files. The created players are stored in a list and returned
 * to the caller.
 * </p>
 */
public class PlayerSelection {
    private PlayerFactory playerFactory1;
    private PlayerFactory playerFactory2;

    /**
     * Creates two players by instantiating two separate {@link PlayerFactory} objects, each
     * with different configuration files. The players are created from the specified configuration
     * files and added to a list.
     *
     * @return A list of two {@link Entity} objects representing the players.
     */
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
