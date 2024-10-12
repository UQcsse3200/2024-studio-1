package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component for ensuring
 */
public class CoinsComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CoinsComponent.class);
    private int coins;

    /**
     * Constructor for CoinComponent
     */
    public CoinsComponent() {
        this.coins = 0;
    }

    @Override
    public void create() {
        this.getEntity().getEvents().addListener("collectCoin", this::addCoins);
    }

    /**
     * Returns the number of coins a player owns.
     *
     * @return the number of coins
     */
    public int getCoins() {
        return this.coins;
    }

    /**
     * Sets the player's coin count. The count has a minimum bound of 0.
     *
     * @param coins coins to set the player's coins to
     * @requires coins >= 0
     * @ensures coins >= 0
     */
    public void setCoins(int coins) {
        if (coins < 0) {
            logger.error("Coin value cannot be negative");
            return;
        }
        this.coins = coins;
        this.getEntity().getEvents().trigger("updateCoins");
    }

    /**
     * Returns if the player has a certain amount of coins.
     *
     * @param coins required amount
     * @return player has greater than or equal to the required amount of coin
     */
    public Boolean hasCoins(int coins) {
        return this.coins >= coins;
    }

    /**
     * Adds to the player's coins. The amount added can be negative.
     *
     * @requires coins > 0
     * @ensures coins > 0
     */
    public void addCoins(int coins) {
        if (coins < 0) {
            logger.error("cannot add coins of a negative value");
            return;
        }
        setCoins(this.coins + coins);
        logger.info("Added coins: {}", coins);
        logger.info("Total coins: {}", getCoins());
    }

    /**
     * Reduces the coins player owns
     *
     * @param cost the amount to reduce the coins by
     */
    public void spend(int cost) {
        if (cost > getCoins()) {
            logger.error("Coin value cannot be greater than current coins");
            return;
        }
        this.coins = getCoins() - cost;
        this.getEntity().getEvents().trigger("updateCoins");
    }

}
