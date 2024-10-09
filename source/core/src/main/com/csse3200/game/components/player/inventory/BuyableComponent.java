package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.Component;

public class BuyableComponent extends Component {
    int cost;

    public BuyableComponent(int defaultCost) {
        super();
        this.cost = defaultCost;
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


}
