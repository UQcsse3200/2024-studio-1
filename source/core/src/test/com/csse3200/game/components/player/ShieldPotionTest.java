package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.inventory.ShieldPotion;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class ShieldPotionTest {

    ShieldPotion potion = new ShieldPotion();
    Entity entity = new Entity();

    @Test
    public void testGetName() {
        Assertions.assertEquals("Shield Potion", potion.getName());
    }


    @Test
    public void testApply() {
        Assertions.assertEquals(0, potion.getCharges());
        potion.apply(entity);
        Assertions.assertEquals(2, potion.getCharges());
    }

    @Test
    public void testNegateHit() {
        potion.apply(entity);
        entity.getEvents().trigger("hit");
        Assertions.assertEquals(1, potion.getCharges());
        entity.getEvents().trigger("hit");
        Assertions.assertEquals(0, potion.getCharges());
        entity.getEvents().trigger("hit");
        Assertions.assertEquals(0, potion.getCharges());
    }

    @Test
    public void testNegateHitWhenInactive() {
        entity.getEvents().trigger("hit");
        Assertions.assertEquals(0, potion.getCharges());
    }


     //@Test
     //public void testGetIcon() {
     //Texture texture = potion.getIcon();
     //Assertions.assertNotNull(texture);
     //}
}
