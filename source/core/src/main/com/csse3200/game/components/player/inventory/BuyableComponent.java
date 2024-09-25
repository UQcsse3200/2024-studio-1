package com.csse3200.game.components.player.inventory;
import com.csse3200.game.components.Component;

public class BuyableComponent extends Component {
    private final int cost;
    private final Collectible collectible;
    public BuyableComponent(int cost, Collectible collectible) {
        this.cost = cost;
        this.collectible = collectible;
    }

    public int getCost() {
        return this.cost;
    }

}
