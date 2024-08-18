package com.csse3200.game.components.npc;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCDeathHandler extends Component {
    private static final Logger logger = LoggerFactory.getLogger(NPCDeathHandler.class);
    private static final float DEATH_ANIMATION_DURATION = 1.0f; // Increased for visibility

    @Override
    public void create() {
        entity.getEvents().addListener("death", this::onDeath);
    }

    private void onDeath() {
        logger.info("NPC {} death animation started.", entity.getId());

        // Disable the entity's movement and other behaviors here
        // For example: entity.getComponent(AITaskComponent.class).setEnabled(false);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                logger.info("NPC {} death animation complete. Removing from game.", entity.getId());
                ServiceLocator.getEntityService().unregister(entity);
            }
        }, DEATH_ANIMATION_DURATION);
    }
}