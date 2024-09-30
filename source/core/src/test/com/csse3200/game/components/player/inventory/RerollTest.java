package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class RerollTest {

    Reroll reroll = new Reroll();

    Entity entity = new Entity()
            .addComponent(new ItemPickupComponent())
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(1, 1, true, 1, 0));

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
        ItemPickupComponent itemPickupComponent = entity.getComponent(ItemPickupComponent.class);
        Collectible collisionItemNull = null;
        Entity collisionItemEntityNull = null;
        Collectible initialCollectible = itemPickupComponent.getItem();
        itemPickupComponent.setContact(false);
        entity.getComponent(ItemPickupComponent.class).handleReroll(collisionItemNull, collisionItemEntityNull);
        assertEquals(initialCollectible, itemPickupComponent.getItem());
    }

}
