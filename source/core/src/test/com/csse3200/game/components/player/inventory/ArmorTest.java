package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.buffs.Armor;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
public class ArmorTest {

    Armor armor = new Armor();
    Entity entity;
    CombatStatsComponent attacker;

    @BeforeEach
    public void setup() {
        // Mock the ResourceService to avoid NullPointerException
        ResourceService mockResourceService = mock(ResourceService.class);
        ServiceLocator.registerResourceService(mockResourceService);

        // Initialize entity and attacker components
        entity = new Entity().addComponent(new CombatStatsComponent(100, 5, true, 0, 0));
        attacker = new CombatStatsComponent(100, 15);
    }

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
        assertEquals(20, armor.getArmorValue());
    }

    @Test
    public void testBaseEntityArmor() {
        assertEquals(0, entity.getComponent(CombatStatsComponent.class).getArmor());
    }

    @Test
    public void testIncreaseEntityArmor() {
        armor.effect(entity);
        assertEquals(20, entity.getComponent(CombatStatsComponent.class).getArmor());
    }

    @Test
    public void testDamageNoArmor() {
        entity.getComponent(CombatStatsComponent.class).hit(attacker);
        assertEquals(85, entity.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    public void testDamageReduction() {
        armor.effect(entity);
        armor.effect(entity);
        assertEquals(40, entity.getComponent(CombatStatsComponent.class).getArmor());
        entity.getComponent(CombatStatsComponent.class).hit(attacker);
        assertEquals(entity.getComponent(CombatStatsComponent.class).getHealth(), 88);
    }
}
