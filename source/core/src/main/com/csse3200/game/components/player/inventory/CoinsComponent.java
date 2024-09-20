package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.Component;
import com.csse3200.game.ui.UIComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinsComponent extends Component {
    private int coins;
    private Inventory inventory;
    private static final Logger logger = LoggerFactory.getLogger(CoinsComponent.class);


    /**
     * Constructor for CoinComponent
     *
     * @param inventory Player's inventory
     */
    public CoinsComponent(Inventory inventory) {
        this.coins = 0;
        this.inventory = inventory;
        inventory.getEntity().getEvents().addListener("collectCoin:3", () -> addCoins(3));
        inventory.getEntity().getEvents().addListener("collectCoin:6", () -> addCoins(6));
        inventory.getEntity().getEvents().addListener("collectCoin:9", () -> addCoins(9));
        inventory.getEntity().getEvents().addListener("collectCoin:10", () -> addCoins(10));
    }

    /**
     * Returns the number of coins a player owns.
     *
     * @return the number of coins
     */
    public int getCoins() {
        return this.coins;
    }

    public Texture getCoinIcon() {
        return new Texture("images/items/coin.png");
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
     * Sets the player's coin count. The count has a minimum bound of 0.
     *
     * @param coins coins to set the player's coins to
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
        inventory.getEntity().getEvents().trigger("updateCoins");


    }

    /**
     * Adds to the player's coins. The amount added can be negative.
     */
    public void addCoins(int coins) {
        try {
            if (coins < 0) {
                throw new IllegalArgumentException("Coin value cannot be negative");
            }
            setCoins(this.coins + coins);
            logger.info("Added coins: " + coins);
            logger.info("Total coins: " + getCoins());
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
            if (cost >  getCoins()) {
                throw new IllegalArgumentException();
            }
            this.coins = getCoins() - cost;
            inventory.getEntity().getEvents().trigger("updateCoins");
        } catch (IllegalArgumentException e) {
            logger.error("Cannot spend more than you own");
        }
    }

}
