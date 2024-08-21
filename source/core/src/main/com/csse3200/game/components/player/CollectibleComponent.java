package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.Collectible;

public class CollectibleComponent extends Component {
    private final Collectible collectible;

    public CollectibleComponent(Collectible collectible) {
        this.collectible = collectible;
    }

    public Collectible getCollectible() {
        return collectible;
    }
}
