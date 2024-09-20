package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinsComponent extends UIComponent {
    private int coins;
    private static final Logger logger = LoggerFactory.getLogger(CoinsComponent.class);
    private Label coinLabel;
    private Table table;

    /**
     * Constructor for CoinComponent
     *
     * @param inventory Player's inventory
     */
    public CoinsComponent(Inventory inventory) {
        this.coins = 0;
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
        } catch (IllegalArgumentException e) {
            System.out.println("coins value cannot be negative...Setting it to 0");
        }
        this.coins = Math.max(coins, 0);
        updateCoinLabel();
    }

    /**
     * Adds to the player's coins. The amount added can be negative.
     */
    public void addCoins(int coins) {
        setCoins(this.coins + coins);
        logger.info("Added coins: " + coins);
        logger.info("Total coins: " + getCoins());
    }

    /**
     * Reduces the coins player owns
     *
     * @param cost the amount to reduce the coins by
     */
    public void spend(int cost) {
        this.coins =- cost;
    }

    @Override
    public void create() {
        super.create();
        // create a table for display and set it to the top of the screen
        table = new Table();
        table.top();
        table.setFillParent(true);
        // create and add the image of coin followed by the amount
        Image coinImage = new Image(getCoinIcon());
        table.add(coinImage).size(40f).padTop(15f).padRight(10f);
        coinLabel = new Label("Coins: x0", skin, "small");
        // FIXME set the colour of the text to white (not rendering white)
        coinLabel.setColor(1,1,1,1);

        table.add(coinLabel).padTop(15f);
        stage.addActor(table);
        updateCoinLabel();
    }


    /**
     * Updates the coin label with the current coin count.
     */
    private void updateCoinLabel() {
        coinLabel.setText("Coins: " + "$" + getCoins());
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // No custom drawing needed for this UI component
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
