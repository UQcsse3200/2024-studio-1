package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.configs.PlayerConfig;

public class PlayerConfigComponent extends Component {
    private final PlayerConfig playerConfig;

    public PlayerConfigComponent(PlayerConfig playerConfig) {
        this.playerConfig = playerConfig;
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }
}
