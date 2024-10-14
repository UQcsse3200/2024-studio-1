package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.areas.Room;
import com.csse3200.game.areas.ShopRoom;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.services.ServiceLocator;

import java.util.Random;

/**
 * A component that allows a player to interact with items
 */
public class ItemPickupComponent extends Component {
    Collectible item = null;
    Entity itemEntity = null;
    CollectibleFactory collectibleFactory;
    Random random;
    private Entity lastPickedUpEntity = null;
    private boolean contact = false;

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
        this.collectibleFactory = new CollectibleFactory();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        entity.getEvents().addListener("pickup", () -> handleItemPickup(item, itemEntity));
        entity.getEvents().addListener("rerollUsed", () -> handleReroll(item, itemEntity));
        entity.getEvents().addListener("purchaseItem", () -> checkItemPurchase(item, itemEntity));
    }


    /**
     * Handles player collision with another entity. The pickup functionality only occurs
     * if the collided entity is a collectible.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        this.setContact(true);
        Entity otherEntity = ((BodyUserData) other.getBody().getUserData()).entity;

        if (!isCollectible(otherEntity) || otherEntity == lastPickedUpEntity) {
            return; // Not a collectible or already picked up
        }

        itemEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
    }

    /**
     * A method that returns whether 'contact' flag
     *
     * @return boolean value of the contact flag
     */
    public boolean isInContact() {
        return contact;
    }

    /**
     * Gets the collectible item in collision
     *
     * @return the collectible item (null if no item is in collision)
     */
    public Collectible getItem() {
        return item;
    }

    /**
     * Sets the 'contact' flag to a specified boolean value
     */
    public void setContact(boolean contact) {
        this.contact = contact;
    }

    /**
     * Determine when the player collision has ended
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        this.setContact(false);
    }

    /**
     * Checks if the given entity is collectible
     *
     * @param entity to check
     * @return true if entity is collectible, otherwise false
     */
    public boolean isCollectible(Entity entity) {
        return entity.getComponent(CollectibleComponent.class) != null;
    }

    /**
     * Handles the reroll item effect. When the reroll item is applied to another item in collision,
     * a new item spawns in place of the original item.
     */
    public void handleReroll(Collectible collisionItem, Entity collisionItemEntity) {
        if (collisionItem == null || collisionItemEntity == null) {
            return;
        }

        int xPosition = (int) collisionItemEntity.getPosition().x;
        int yPosition = (int) collisionItemEntity.getPosition().y;

        GridPoint2 itemEntityPosition = new GridPoint2(xPosition, yPosition);
        markEntityForRemoval(collisionItemEntity);

        int randomInt = this.random.nextInt(15);
        Entity newItem = this.randomItemGenerator(randomInt);
//        Entity newItem = testCollectibleFactory.createCollectibleEntity("item:shieldpotion");

        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(newItem, itemEntityPosition, true, true);
//        area.spawnEntityAt(newItem, itemEntityPosition, true, true);
        entity.getEvents().trigger("updateItemsReroll", newItem, collisionItemEntity);
        itemEntity = newItem;
        item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
    }

    /**
     * A method temporarily used to represent the 'funds' of the player
     *
     * @return an integer value representing the player's funds
     */
    public int getTestFunds() {
        return 9;
    }

    /**
     * Handles the event where the player attempts to purchase a buyable item
     *
     * @param item       the collectible item that the player is attempting to purchase
     * @param itemEntity the entity representation of the item that the player is attempting to purhase
     */
    public void checkItemPurchase(Collectible item, Entity itemEntity) {
        if (entity.getComponent(CoinsComponent.class) == null) {
            entity.getEvents().trigger("insufficientFunds");
            // Depending on how you want to handle no coins component
            return;
        }
        CoinsComponent coinsComponent = entity.getComponent(CoinsComponent.class);
        int playerFunds = coinsComponent.getCoins();
        if (item == null || itemEntity == null) {
            return;
        }
        if ((itemEntity.getComponent(BuyableComponent.class) != null) && contact) {
            int cost = itemEntity.getComponent(BuyableComponent.class).getCost();
            if (playerFunds >= cost) {
                coinsComponent.spend(cost);
                entity.getComponent(InventoryComponent.class).pickup(item);
                itemEntity.getComponent(BuyableComponent.class).removeLabel();

                GameController controller = ServiceLocator.getGameAreaService().getGameController();

                if (controller != null) {
                    Room room = controller.getCurrentRoom();
                    if (room instanceof ShopRoom shop) {
                        shop.removeItemFromList(item.getSpecification() + ":buyable");
                        markEntityForRemoval(itemEntity);
                    }
                }
            }
            else {
                entity.getEvents().trigger("insufficientFunds");
            }
        }
    }

    /**
     * Handles the logic for picking up the item based on its type
     *
     * @param item       the item to be picked up
     * @param itemEntity the item entity
     */
    private void handleItemPickup(Collectible item, Entity itemEntity) {
        if (item == null || itemEntity == null || itemEntity.getComponent(BuyableComponent.class) != null) {
            return;
        }

        InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
        if (contact) {
            inventory.pickup(item);
            markEntityForRemoval(itemEntity);
            this.itemEntity.getEvents().trigger("itemChose");
        }

        lastPickedUpEntity = itemEntity; //Update the last picked up entity
        this.item = null;
        this.itemEntity = null;
    }

    /**
     * Marks the entity for removal from the game.
     *
     * @param itemEntity the item to be disposed
     */
    private void markEntityForRemoval(Entity itemEntity) {
        ServiceLocator.getEntityService().markEntityForRemoval(itemEntity);
    }

    /**
     * Randomly generates a new item to spawn, based on the random integer passed in
     *
     * @param randomNum an integer representing a random number passed in
     * @return an entity representation of a new collectible item
     */
    private Entity randomItemGenerator(int randomNum) {
        String specification = null;
        switch (randomNum) {
            case 0 -> specification = "item:shieldpotion";
            case 1 -> specification = "item:bandage";
            case 2 -> specification = "item:medkit";
            case 3 -> specification = "buff:energydrink:High";
            case 4 -> specification = "buff:energydrink:Low";
            case 5 -> specification = "buff:energydrink:Medium";
            case 6 -> specification = "buff:syringe";
            case 7 -> specification = "buff:armor";
            case 8 -> specification = "buff:damagebuff";
            case 9 -> specification = "item:beartrap";
            case 10 -> specification = "item:targetdummy";
            case 11 -> specification = "item:reroll";
            case 12 -> specification = "buff:feather";
            case 13 -> specification = "item:heart";
            case 14 -> specification = "buff:divinepotion";
        }

        return this.collectibleFactory.createCollectibleEntity(specification);
    }
}
