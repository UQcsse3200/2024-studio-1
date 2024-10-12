package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinsComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CoinsComponent.class);
    private int coins;

    /**
     * Constructor for CoinComponent
     *
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
        try {
            if (coins < 0) {
                throw new IllegalArgumentException("Coin value cannot be negative");
            }
            this.coins = coins;
        } catch (IllegalArgumentException e) {
            System.out.println("coins value cannot be negative...Setting it to 0");
        }
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
        try {
            if (coins < 0) {
                throw new IllegalArgumentException("Coin value cannot be negative");
            }
            setCoins(this.coins + coins);
            logger.info("Added coins: {}", coins);
            logger.info("Total coins: {}", getCoins());
        } catch (IllegalArgumentException e) {
            logger.error("coins value cannot be negative...Setting it to 0");
        }

    }

    /**
     * Reduces the coins player owns
     *
     * @param cost the amount to reduce the coins by
     */
    public void spend(int cost) {
        try {
            if (cost > getCoins()) {
                throw new IllegalArgumentException();
            }
            this.coins = getCoins() - cost;
            this.getEntity().getEvents().trigger("updateCoins");
        } catch (IllegalArgumentException e) {
            logger.error("Cannot spend more than you own");
        }
    }

}
