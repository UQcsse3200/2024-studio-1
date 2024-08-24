package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.screens.SettingsScreen;

public class ItemPickupComponent extends Component {

    public ItemPickupComponent() {
        super();
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    }

    /*
    This method was mainly based off of the TouchPlayerInputComponent.java class
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        Entity itemEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        if (itemEntity.getComponent(CollectibleComponent.class) == null) {
            return;
        }
        Collectible item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
        entity.getComponent(InventoryComponent.class).pickup(item);
        System.out.println("It works!");
    }
}
