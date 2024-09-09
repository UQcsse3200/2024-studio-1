package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.services.ServiceLocator;

/**
 * A component that allows a player to interact with items
 */
public class ItemPickupComponent extends Component {

    /**
     * Construct a new empty item pickup component
     */
    public ItemPickupComponent() {
        super();
    }

    /**
     * The create method that is called when the entity is created
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    }

    /**
     * A method that is called whenever the player collides with another entity. To ensure that the 'pickup'
     * functionality only occurs when the other entity is a collectible, checks are made to ensure that the
     * other entity has a CollectibleComponent
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
        itemEntity.getEvents().trigger("pickedUp");
        markEntityForRemoval(itemEntity);
        System.out.println("It works!"); //Test to see if on collision, it works
    }

    /**
     * Marks the entity for removal from the game. This method unregisters the entity from the entity service,
     * scheduling it for later disposal at the end of the games update cycle. This ensures that the entity is
     * not prematurely removed from the game state.
     *
     * @param itemEntity the item to be disposed
     */
    private void markEntityForRemoval(Entity itemEntity) {
        // Perform any last checks or cleanup before actual disposal
        ServiceLocator.getEntityService().unregister(itemEntity);
        ServiceLocator.getEntityService().markEntityForRemoval(itemEntity);
    }
}
