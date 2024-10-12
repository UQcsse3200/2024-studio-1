package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.player.inventory.CoinsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays the amount of coins player has on the UI
 */
public class PlayerCoinDisplay extends UIComponent {
    /**
     * Label containing the amount of coins player has collected
     */
    private Label coinLabel;
    /**
     * Path to coin image
     */
    private static final String IMAGE_PATH = "images/items/coin.png";
    /**
     * An instance of CoinsComponent which contains information about the coins
     */
    private final CoinsComponent coinsComponent;

    /**
     * Constructor that initialises the rendering of coins on UI
     *
     * @param coinsComponent the component to display
     */
    public PlayerCoinDisplay(CoinsComponent coinsComponent) {
        this.coinsComponent = coinsComponent;
    }

    /**
     * Initialises and set up UI components required for the display
     */
    @Override
    public void create() {
        super.create();
        this.getEntity().getEvents().addListener("updateCoins", this::updateCoinLabel);
        this.getEntity().getEvents().addListener("collectCoin", this::showCoins);

        // create a table for display and set it to the top of the screen
        Table table = new Table();
        table.top().setFillParent(true);

        Image coinImage = new Image(new Texture(IMAGE_PATH));
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
        coinLabel.setText("Coins: x" + coinsComponent.getCoins());
    }

    /**
     * When a player successfully defeats an animal, the
     * amount of coins earned is displayed on the screen for few seconds
     *
     * @param coins the amount of coins an animal gave from defeating
     */
    private void showCoins(int coins) {
        String text = String.format("+%d coins added", coins);
        Label coinText = new Label(text, skin, "small");
        Table table = new Table();
        table.top().padTop(150f);
        table.setFillParent(true);
        table.row().padTop(5f);
        table.add(coinText).padTop(5f);
        stage.addActor(table);
        // unrender the label after 3 second of display
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                coinText.remove();
            }
        }, 1f);

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

