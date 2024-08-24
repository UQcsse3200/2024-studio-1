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
        //Get the entity attached to thing ('other') that the player collided with
        Entity itemEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        if (itemEntity.getComponent(CollectibleComponent.class) == null) {
            //If the other thing does not have a 'CollectibleComponent', then it is not a Collectible entity
            return;
        }
        //Get the Collectible that was passed into this Collectible entity
        Collectible item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
        //Use the 'pickup' method of the InventoryComponent, pass in the item
        //The 'pickup' method of the InventoryComponent class uses the unique 'pickup' method of the item that
        // is passed in
        entity.getComponent(InventoryComponent.class).pickup(item);
        System.out.println("It works!"); //Test to see if on collision, it works
    }
}
