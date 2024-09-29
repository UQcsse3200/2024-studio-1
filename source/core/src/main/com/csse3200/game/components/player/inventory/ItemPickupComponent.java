package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.areas.MainGameArea;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.RandomNumberGenerator;
import java.util.Random;

/**
 * A component that allows a player to interact with items
 */
public class ItemPickupComponent extends Component {
    private Entity lastPickedUpEntity = null;
    private boolean contact = false;
    Collectible item = null;
    Entity itemEntity = null;
    CollectibleFactory collectibleFactory = new CollectibleFactory();
    Random random;

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
        super.create();
        this.random = new Random();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        entity.getEvents().addListener("pickup", ()->handleItemPickup(item, itemEntity));
        entity.getEvents().addListener("rerollUsed", ()->handleReroll(item, itemEntity));
        entity.getEvents().addListener("purchaseItem", ()->checkItemPurchase(item,itemEntity));
    }


    /**
     * Handles player collision with another entity. The pickup functionality only occurs
     * if the collided entity is a collectible.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        contact = true;
        Entity otherEntity = ((BodyUserData) other.getBody().getUserData()).entity;

        if (!isCollectible(otherEntity) || otherEntity == lastPickedUpEntity) {
            return; // Not a collectible or already picked up
        }

        itemEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
    }

    public boolean isInContact() {
        return contact;
    }

    public Collectible getItem() {
        return item;
    }

    /**
     * Determine when the player collision has ended
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        contact = false;
    }

    /**
     * Checks if the given entity is collectible
     * @param entity to check
     * @return true if entity is collectible, otherwise false
     */
    public boolean isCollectible(Entity entity) {
        return entity.getComponent(CollectibleComponent.class) != null;
    }

    private void handleReroll(Collectible collisionItem, Entity collisionItemEntity) {
        if (collisionItem == null || collisionItemEntity == null) {
            return;
        }

        int xPosition = (int) collisionItemEntity.getPosition().x;
        int yPosition = (int) collisionItemEntity.getPosition().y;

        GridPoint2 itemEntityPosition = new GridPoint2(xPosition, yPosition);
        markEntityForRemoval(collisionItemEntity);

        int randomInt = this.random.nextInt(5);
        Entity newItem = this.randomItemGenerator(randomInt);
//        Entity newItem = testCollectibleFactory.createCollectibleEntity("item:shieldpotion");

        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(newItem, itemEntityPosition, true, true);
//        area.spawnEntityAt(newItem, itemEntityPosition, true, true);
        entity.getEvents().trigger("updateItemsReroll", newItem, collisionItemEntity);
        itemEntity = newItem;
        item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
    }

    public int getTestFunds() {
        return 9;
    }
    private void checkItemPurchase(Collectible item, Entity itemEntity) {
        int playerFunds = getTestFunds();
        if (item == null || itemEntity == null) {
            return;
        }
        if ((itemEntity.getComponent(BuyableComponent.class) != null) && contact) {
            int cost = itemEntity.getComponent(BuyableComponent.class).getCost();
            if (playerFunds >= cost) {
                entity.getComponent(InventoryComponent.class).pickup(item);
                markEntityForRemoval(itemEntity);
            }
            else {
                entity.getEvents().trigger("insufficientFunds");
            }
        }
    }

    /**
     * Handles the logic for picking up the item based on its type
     * @param item the item to be picked up
     * @param itemEntity the item entity
     */
    private void handleItemPickup(Collectible item, Entity itemEntity) {
        if (item == null || itemEntity == null || itemEntity.getComponent(BuyableComponent.class) != null) {
            return;
        }

        InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
        if(contact) {
            inventory.pickup(item);
            markEntityForRemoval(itemEntity);
        }

        lastPickedUpEntity = itemEntity; //Update the last picked up entity
        this.item = null;
        this.itemEntity = null;
    }

    /**
     * Marks the entity for removal from the game.
     * @param itemEntity the item to be disposed
     */
    private void markEntityForRemoval(Entity itemEntity) {
        ServiceLocator.getEntityService().markEntityForRemoval(itemEntity);
    }

    private Entity randomItemGenerator(int randomNum) {
        String specification = null;
        switch (randomNum) {
            case 0 -> specification = "item:shieldpotion";
            case 1 -> specification = "item:bandage";
            case 2 -> specification = "item:medkit";
            case 3 -> specification = "buff:energydrink:High";
            case 4 -> specification = "buff:energydrink:Low";
            case 5 -> specification = "buff:energydrink:Medium";
        }

        return collectibleFactory.createCollectibleEntity(specification);
    }
}
