package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This component handles damage taken by NPCs in the game.
 * It updates the NPC's health, triggers death events, and maintains a list of dead NPCs.
 */
public class NPCDamageHandlerComponent extends Component {
    public static final Logger logger = LoggerFactory.getLogger(NPCDamageHandlerComponent.class);
    public CombatStatsComponent combatStats;
    public boolean isDead = false;
    // Store all dead animals ID, so they can be removed from the game using DeathHandler
    public static final List<Integer> deadAnimals = new ArrayList<>();

    /**
     * Called when the entity is created and registered. Sets up the damage listener.
     */
    @Override
    public void create() {
        // Retrieve the CombatStatsComponent and set up the damage listener
        combatStats = entity.getComponent(CombatStatsComponent.class);
        entity.getEvents().addListener("takeDamage", this::onTakeDamage);
        // Currently, the DamageTester causes 0 damage to all NPCs
    }

    /**
     * Processes the damage received by the NPC.
     * If the NPC's health reaches 0, it triggers the death event and adds the NPC's ID to the dead animals list.
     *
     * @param damage The amount of damage to apply to the NPC.
     */
    void onTakeDamage(int damage) {
        // Only process damage if the NPC is alive and has combat stats
        if (combatStats != null && !isDead) {
            combatStats.takeDamage(damage);
            //logger.info("NPC {} took {} damage. Current health: {}", entity.getId(), damage, combatStats.getHealth());

            // Check if the NPC has died from this damage
            if (combatStats.getHealth() <= 0 && !isDead) {
                isDead = true;
                deadAnimals.add(entity.getId());
                logger.info("NPC {} has died. Triggering death event.", entity.getId());
                entity.getEvents().trigger("death");
            }
        }
    }
}