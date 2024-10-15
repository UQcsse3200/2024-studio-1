package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.entities.Entity;

/**
 * A component that handles the UI display if a buyable item is unaffordable
 */
public class FundsDisplayComponent extends UIComponent {

    @Override
    protected void draw(SpriteBatch batch) {

    }

    /**
     * Method called upon creation
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("insufficientFunds", this::insufficientFundsMessage);
    }

    /**
     * Called when the player attempts to purchase an unaffordable buyable item. Here, a message is displayed
     * to the user to let them know that they have insufficient funds
     */
    public void insufficientFundsMessage() {
        String text = String.format("Sorry! Insufficient funds.");
        Label insufficientFundsLabel = new Label(text, skin, "small");
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(insufficientFundsLabel).padTop(5f);
        stage.addActor(table);
        // unrender the label after 2 second of display
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                table.remove(); // Remove the label from the screen
            }
        }, 2);
    }

}
