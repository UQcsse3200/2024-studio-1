package com.csse3200.game.components.npc;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

public class NPCDamageTester {

    /**
     * Tests damage application to all NPCs with a NPCDamageHandlerComponent.
     * This method can be called periodically to simulate damage to NPCs.
     */
    public static void testNPCDamage() {
        // Test method to apply damage to NPCs every 5 seconds
        Entity[] entities = ServiceLocator.getEntityService().getEntities();
        for (Entity entity : entities) {
            NPCDamageHandlerComponent damageHandler = entity.getComponent(NPCDamageHandlerComponent.class);
            if (damageHandler != null) {
                int damageAmount = 0; // Adjust as needed
                entity.getEvents().trigger("takeDamage", damageAmount);
            }
        }
    }
}