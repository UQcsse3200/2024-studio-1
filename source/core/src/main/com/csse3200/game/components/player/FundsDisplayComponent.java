package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ui.UIComponent;

public class FundsDisplayComponent extends UIComponent {

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("insufficientFunds", this::insufficientFundsMessage);
    }

    public void insufficientFundsMessage() {
        String text = String.format("Sorry! Insufficient funds.");
        Label insufficientFundsLabel = new Label(text, skin, "small");
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(insufficientFundsLabel).padTop(5f);
        stage.addActor(table);
        // unrender the label after 1 second of display
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                table.remove(); // Remove the label from the screen
            }
        }, 2);
    }

}
