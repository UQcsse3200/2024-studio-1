package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * This component handles damage taken by NPCs in the game.
 * It updates the NPC's health and maintains a list of dead NPCs.
 */
public class NPCDamageHandlerComponent extends Component {
    private CombatStatsComponent combatStats;
    private boolean isDead = false;
    // Store all dead animals ID, so they can be removed from the game using DeathHandler
    public static final List<Integer> deadAnimals = new ArrayList<>();

    /**
     * Called when the entity is created and registered. Sets up the damage listener.
     */
    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
        entity.getEvents().addListener("healthChanged", this::onHealthChanged);
        entity.getEvents().addListener("died", this::onDeath);
    }

    /**
     * Processes the health change of the NPC.
     *
     * @param health The new health value of the NPC.
     */
    private void onHealthChanged(int health) {
        if (combatStats != null && !isDead) {
            if (health <= 0 && !isDead) {
                onDeath();
            }
        }
    }

    /**
     * Handles the death of the NPC.
     */
    private void onDeath() {
        if (!isDead) {
            isDead = true;
            deadAnimals.add(entity.getId());
            entity.getEvents().trigger("death");
        }
    }
}