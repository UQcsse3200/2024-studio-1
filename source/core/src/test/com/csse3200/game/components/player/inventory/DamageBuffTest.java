package com.csse3200.game.components.player.inventory;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DamageBuffTest {
    DamageBuff damageBuff = new DamageBuff();
    Entity entity = new Entity().addComponent(new CombatStatsComponent(100, 5, true, 0, 0));
    CombatStatsComponent attacker = new CombatStatsComponent(100,15);
    @Test
    public void testGetName() {assertEquals("Damage Buff", damageBuff.getName());}

    @Test
    public void testGetItemSpecification() {assertEquals("damagebuff", damageBuff.getBuffSpecification());}

    @Test
    public void testGetBuff() {assertEquals(5, damageBuff.getBuff());}

    @Test
    public void testBaseDamageBuff() {assertEquals(0, entity.getComponent(CombatStatsComponent.class).getDamageBuff());}

    @Test
    public void testDamageBuff()
    {
        damageBuff.effect(entity);
        assertEquals(5, entity.getComponent(CombatStatsComponent.class).getDamageBuff());
    }


}
