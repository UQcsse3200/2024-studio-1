package com.csse3200.game.components.npc;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCDamageHandlerComponent extends Component {
    private CombatStatsComponent combatStats;
    private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);

    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
        entity.getEvents().addListener("takeDamage", this::onTakeDamage);
    }

    private void onTakeDamage(int damage) {
        if (combatStats != null) {
            combatStats.takeDamage(damage);
            logger.info("NPC took " + damage + " damage. Current health: " + combatStats.getHealth());
        }
    }
}