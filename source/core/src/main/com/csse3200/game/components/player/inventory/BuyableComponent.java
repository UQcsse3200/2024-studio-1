package com.csse3200.game.components.player.inventory;
import com.csse3200.game.components.Component;

public class BuyableComponent extends Component {
    private final int cost;
    public BuyableComponent(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return this.cost;
    }

}
