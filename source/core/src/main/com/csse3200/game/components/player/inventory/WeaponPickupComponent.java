//package com.csse3200.game.components.player.inventory;
//
//import com.badlogic.gdx.physics.box2d.Fixture;
//import com.csse3200.game.components.Component;
//import com.csse3200.game.components.player.CollectibleComponent;
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.physics.BodyUserData;
//import com.csse3200.game.services.ServiceLocator;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * A component that allows a player to interact with items
// */
//public class WeaponPickupComponent extends Component {
//    // Create logger
//    private static final Logger logger = LoggerFactory.getLogger(Component.class);
//    private Entity lastPickedUpEntity = null;
//
//    /**
//     * Construct a new empty item pickup component
//     */
//    public WeaponPickupComponent() {
//        super();
//    }
//
//    /**
//     * The create method that is called when the entity is created
//     */
//    @Override
//    public void create() {
//        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
//    }
//
//    /**
//     * A method that is called whenever the player collides with another entity. To ensure that the 'pickup'
//     * functionality only occurs when the other entity is a collectible, checks are made to ensure that the
//     * other entity has a CollectibleComponent.
//     */
//    private void onCollisionStart(Fixture me, Fixture other) {
//        // Get the entity attached to the other fixture (thing the player collided with)
//        Entity itemEntity = ((BodyUserData) other.getBody().getUserData()).entity;
//
//        // Ensure the entity is valid and has a CollectibleComponent
//        if (itemEntity == null || itemEntity.getComponent(CollectibleComponent.class) == null) {
//            return;
//        }
//
//        // Avoid picking up the same entity twice
//        if (itemEntity == lastPickedUpEntity) {
//            return;
//        }
//
//        // Get the Collectible from the entity
//        Collectible item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
//
//        if (item instanceof RangedWeapon || item instanceof MeleeWeapon) {
//            // Add the item to the player's inventory
//            entity.getComponent(InventoryComponent.class).pickup(item);
//
//            // Mark the entity for removal and disable it to prevent further interactions
//            markEntityForRemoval(itemEntity);
//            itemEntity.setEnabled(false);
//
//            // Store the last picked-up entity to avoid duplicate pickups
//            lastPickedUpEntity = itemEntity;
//
//            logger.info("Weapon picked up"); // Debug message to confirm functionality
//
//        }
//    }
//
//    /**
//     * Marks the entity for removal from the game.
//     * This method unregisters the entity from the entity service, scheduling it for later disposal at the end
//     * of the game's update cycle. This ensures that the entity is not prematurely removed from the game state.
//     *
//     * @param itemEntity the item to be disposed
//     */
//    private void markEntityForRemoval(Entity itemEntity) {
//        // Perform any last checks or cleanup before actual disposal
//        ServiceLocator.getEntityService().unregister(itemEntity);
//        ServiceLocator.getEntityService().markEntityForRemoval(itemEntity);
//        logger.info("Marking entity for removal: " + itemEntity);
//    }
//}
