package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCDamageHandlerComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(NPCDamageHandlerComponent.class);
    private CombatStatsComponent combatStats;
    private boolean isDead = false;

    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
        entity.getEvents().addListener("takeDamage", this::onTakeDamage);
    }

    private void onTakeDamage(int damage) {
        if (combatStats != null && !isDead) {
            combatStats.takeDamage(damage);
            logger.info("NPC {} took {} damage. Current health: {}", entity.getId(), damage, combatStats.getHealth());

            if (combatStats.getHealth() <= 0) {
                isDead = true;
                logger.info("NPC {} has died. Triggering death event.", entity.getId());
                entity.getEvents().trigger("death");
            }
        }
    }
}