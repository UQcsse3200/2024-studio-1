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
    private Entity lastPickedUpEntity = null;

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
     * Handles player collision with another entity. The pickup functionality only occurs
     * if the collided entity is a collectible.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        Entity itemEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        if (!isCollectible(itemEntity) || itemEntity == lastPickedUpEntity) {
            return; // Not a collectible or already picked up
        }

        Collectible item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
        handleItemPickup(item, itemEntity);
    }

    /**
     * Checks if the given entity is collectible
     * @param entity to check
     * @return true if entity is collectible, otherwise false
     */
    private boolean isCollectible(Entity entity) {
        return entity.getComponent(CollectibleComponent.class) != null;
    }

    /**
     * Handles the logic for picking up the item based on its type
     * @param item the item to be picked up
     * @param itemEntity the item entity
     */
    private void handleItemPickup(Collectible item, Entity itemEntity) {
        InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
        if (isWeapon(item)) {
            inventory.pickup(item, itemEntity);
        } else {
            inventory.pickup(item);
            markEntityForRemoval(itemEntity);
        }

        lastPickedUpEntity = itemEntity; //Update the last picked up entity
    }

    /**
     * Checks if the item is a weapon
     * @param item the item to be picked up
     * @return true if the item is weapon, false otherwise
     */
    private boolean isWeapon(Collectible item) {
        return item.getType() == Collectible.Type.MELEE_WEAPON || item.getType() == Collectible.Type.RANGED_WEAPON;
    }

    /**
     * Marks the entity for removal from the game.
     * @param itemEntity the item to be disposed
     */
    private void markEntityForRemoval(Entity itemEntity) {
        ServiceLocator.getEntityService().markEntityForRemoval(itemEntity);
    }
}
