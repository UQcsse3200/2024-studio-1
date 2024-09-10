package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.ShieldComponent;
import com.csse3200.game.entities.Entity;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class ArmorTest {
    Armor armor = new Armor();

    Entity entity = new Entity().addComponent(new CombatStatsComponent(100, 5, true, 0));

    CombatStatsComponent attacker = new CombatStatsComponent(100, 15);

    @Test
    public void testGetName() {
        assertEquals("Armor", armor.getName());
    }

    @Test
    public void testGetItemSpecification() {
        assertEquals("armor", armor.getBuffSpecification());
    }

    @Test
    public void testGetSpecification() {
        assertEquals("buff:armor", armor.getSpecification());
    }

    @Test
    public void testArmorItem() {
        assertEquals(10, armor.getArmorValue());
    }
    @Test
    public void testBaseEntityArmor() {
        assertEquals(0, entity.getComponent(CombatStatsComponent.class).getArmor());
    }

    @Test
    public void testIncreaseEntityArmor() {
        armor.effect(entity);
        assertEquals(10, entity.getComponent(CombatStatsComponent.class).getArmor());
    }

    @Test
    public void testDamageNoArmor() {
        entity.getComponent(CombatStatsComponent.class).hit(attacker);
        assertEquals(entity.getComponent(CombatStatsComponent.class).getHealth(), 85);
    }

    @Test
    public void testDamageReduction() {
        armor.effect(entity);
        armor.effect(entity);
        assertEquals(20, entity.getComponent(CombatStatsComponent.class).getArmor());
        entity.getComponent(CombatStatsComponent.class).hit(attacker);
        assertEquals(entity.getComponent(CombatStatsComponent.class).getHealth(), 87);
    }
}
