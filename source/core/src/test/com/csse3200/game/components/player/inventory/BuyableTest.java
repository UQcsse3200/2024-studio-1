package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.inventory.usables.MedKit;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

public class BuyableTest {
    ItemFactory itemFactory = new ItemFactory();
    Entity entity;
    Entity itemEntity;
    @Mock
    private EntityService entityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock EntityService and register it using ServiceLocator
        entityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(entityService);

        entity = new Entity()
                .addComponent(new InventoryComponent())
                .addComponent(new ItemPickupComponent());
                entity.addComponent(new CoinsComponent());
        itemEntity = new Entity()
                .addComponent(new BuyableComponent(10))
                .addComponent(new CollectibleComponent(new MedKit()));
    }

    @Test
    public void testCreateBuyableMedkit() {
        assertInstanceOf(MedKit.class, itemFactory.create("medkit:buyable"));
    }

    @Test
    public void sufficientFundsTest() {
        //Set the cost of the buyable item to 0 instead of 10
        itemEntity.getComponent(BuyableComponent.class).setCost(0);
        ItemPickupComponent itemPickupComponent = entity.getComponent(ItemPickupComponent.class);
        itemPickupComponent.setContact(true);
        Collectible item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
        Array<UsableItem> expectedAfterInventory = entity.getComponent(InventoryComponent.class).getItems();
        expectedAfterInventory.add((UsableItem) item);

        itemPickupComponent.checkItemPurchase(item, itemEntity);
        Array<UsableItem> inventoryAfter = entity.getComponent(InventoryComponent.class).getItems();

        //It is expected that when the item is affordable, the inventory should be the original, but with the
        //newly purchased item appended
        assertEquals(expectedAfterInventory, inventoryAfter);
    }

    @Test
    public void insufficientFundsTest() {
        //By default, the item cost is 10
        //For testing, the player's funds are set to 9 (refer to getTestFunds() in ItemPickupComponent)
        ItemPickupComponent itemPickupComponent = entity.getComponent(ItemPickupComponent.class);
        itemPickupComponent.setContact(true);
        Collectible item = itemEntity.getComponent(CollectibleComponent.class).getCollectible();
        Array<UsableItem> expectedAfterInventory = entity.getComponent(InventoryComponent.class).getItems();

        itemPickupComponent.checkItemPurchase(item, itemEntity);
        Array<UsableItem> inventoryAfter = entity.getComponent(InventoryComponent.class).getItems();

        //It is expected that the inventory should not append any new items, since the buyable item cannot be
        //purchased as it is unaffordable
        assertEquals(expectedAfterInventory, inventoryAfter);
    }


}
