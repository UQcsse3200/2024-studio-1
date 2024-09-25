package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.files.FileLoader;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ui.UIComponent;

import java.awt.*;

public class BuyablePickupComponent extends UIComponent {
    private boolean contact = false;
    Collectible collectible;
    Entity collectibleEntity;
    public BuyablePickupComponent() {
        super();
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        entity.getEvents().addListener("collectiblePurchased",  () ->attemptPurchase(collectible, collectibleEntity));
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        contact = true;
        Entity otherEntity = ((BodyUserData) other.getBody().getUserData()).entity;

        //If the other entity in collision is not buyable
        if (otherEntity.getComponent(BuyableComponent.class) == null) {
            return;
        }

        collectibleEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        collectible = collectibleEntity.getComponent(CollectibleComponent.class).getCollectible();
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        contact = false;
    }
    @Override
    protected void draw(SpriteBatch batch) {
        // handled by superclass
    }

    public void attemptPurchase(Collectible collectible, Entity collectibleEntity) {
        int testFunds = 8;

        if ((contact) && collectible != null && collectibleEntity != null) {
            int cost = collectibleEntity.getComponent(BuyableComponent.class).getCost();
            //Check for sufficient funds
            if (testFunds >= cost) {
                entity.getComponent(InventoryComponent.class).pickup(collectible);
                markEntityForRemoval(collectibleEntity);
            }
            else {
                String text = String.format("Sorry! Insufficient funds :(");
                Label achievementLabel = new Label(text, skin, "small");
                Table table = new Table();
                table.center();
                table.setFillParent(true);
                table.add(achievementLabel).padTop(5f);
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
    }

    private void markEntityForRemoval(Entity collectibleEntity) {
        ServiceLocator.getEntityService().markEntityForRemoval(collectibleEntity);
    }

}
