package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.player.inventory.CoinsComponent;
import com.csse3200.game.ui.UIComponent;

public class PlayerCoinDisplay extends UIComponent {
    private Label coinLabel;
    private CoinsComponent coinsComponent;
    private Table table;

    public PlayerCoinDisplay(CoinsComponent coinsComponent) {
        this.coinsComponent = coinsComponent;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("updateCoins", this::updateCoinLabel);
        // create a table for display and set it to the top of the screen
        table = new Table();
        table.top();
        table.setFillParent(true);

        Image coinImage = new Image(new Texture("images/items/coin.png"));
        table.add(coinImage).size(40f).padTop(15f).padRight(10f);
        coinLabel = new Label("Coins: x0", skin, "small");
        // FIXME set the colour of the text to white (not rendering white)
        coinLabel.setColor(1,1,1,1);

        table.add(coinLabel).padTop(15f);
        stage.addActor(table);
        // create and add the image of coin followed by the amount
        updateCoinLabel();
    }


    /**
     * Updates the coin label with the current coin count.
     */
    private void updateCoinLabel() {
        coinLabel.setText("Coins: " + "$" + coinsComponent.getCoins());
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
