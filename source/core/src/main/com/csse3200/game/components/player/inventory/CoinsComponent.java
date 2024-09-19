package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinsComponent extends Component {

    private int coins;
    private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);

    public CoinsComponent(Inventory inventory) {
        super();
        setCoins(0);
        inventory.getEntity().getEvents().addListener("collectCoin:3", ()->addCoins(3));
        inventory.getEntity().getEvents().addListener("collectCoin:6", ()->addCoins(6));
        inventory.getEntity().getEvents().addListener("collectCoin:9", ()->addCoins(9));
        inventory.getEntity().getEvents().addListener("collectCoin:10", ()->addCoins(10));
    }


    /**
     *  the amount of coins a player owns
     * @return the amount of coins
     */
    public int getCoins() {
        return this.coins;
    }

    /**
     * Returns if the player has a certain amount of coin.
     * @param coins required amount
     * @return player has greater than or equal to the required amount of coin
     */
    public Boolean hasCoins(int coins) {
        return this.coins >= coins;
    }

    /**
     * Sets the player's coin. coin has a minimum bound of 0.
     *
     * @param coins coins to get the coins of player to
     */
    public void setCoins(int coins) {
        try {
            if (coins < 0) {
                throw new IllegalArgumentException();
            }

        } catch (IllegalArgumentException e) {
            System.out.println("coins value cannot be negative...Setting it to 0");
        }
        this.coins = Math.max(coins, 0);
    }

    /**
     * Adds to the player's coins. The amount added can be negative.
     */
    public void addCoins(int coins) {
        setCoins(this.coins + coins);
        logger.info("added coins" + coins);
        logger.info("Total Gold is: " + getCoins());
    }
}
