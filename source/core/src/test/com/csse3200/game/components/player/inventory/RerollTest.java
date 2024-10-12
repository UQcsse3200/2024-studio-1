package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.inventory.usables.MedKit;
import com.csse3200.game.components.player.inventory.usables.Reroll;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class RerollTest {

    private Reroll reroll;
    private Entity player;

    private Entity collisionItemEntity;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        reroll = new Reroll();
        player = new Entity().addComponent(new ItemPickupComponent());
        collisionItemEntity = new Entity()
                .addComponent(new CollectibleComponent(new MedKit()));

    }
    @Test
    public void testGetName() {
        assertEquals("Reroll", reroll.getName());
    }

    @Test
    public void testGetItemSpecification() {
        assertEquals("reroll", reroll.getItemSpecification());
    }

    @Test
    public void testGetSpecification() {
        assertEquals("item:reroll", reroll.getSpecification());
    }


    @Test
    public void testApplyNoCollision() {
        ItemPickupComponent itemPickupComponent = player.getComponent(ItemPickupComponent.class);
        Collectible collisionItemNull = null;
        Entity collisionItemEntityNull = null;
        Collectible initialCollectible = itemPickupComponent.getItem();
        itemPickupComponent.setContact(false);
        player.getComponent(ItemPickupComponent.class).handleReroll(collisionItemNull, collisionItemEntityNull);
        assertEquals(initialCollectible, itemPickupComponent.getItem());
    }


}
