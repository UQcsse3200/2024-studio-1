package com.csse3200.game.components.npc;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCDeathHandler extends Component {
    private static final Logger logger = LoggerFactory.getLogger(NPCDeathHandler.class);
    private static final float DEATH_ANIMATION_DURATION = 1.0f;

    private AnimationRenderComponent animator;

    @Override
    public void create() {
        entity.getEvents().addListener("death", this::onDeath);
        animator = entity.getComponent(AnimationRenderComponent.class);
    }

    private void onDeath() {
        logger.info("NPC {} death animation started.", entity.getId());

        // Play death animation
        if (animator != null && animator.hasAnimation("death")) {
            animator.startAnimation("death");
        }

        // Disable physics and AI components
        entity.getComponent(PhysicsComponent.class).setEnabled(false);
        entity.getComponent(AITaskComponent.class).setEnabled(false);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                logger.info("NPC {} death animation complete. Removing from game.", entity.getId());
                ServiceLocator.getEntityService().unregister(entity);
                entity.dispose();
                NPCDamageHandlerComponent.deadAnimals.remove(Integer.valueOf(entity.getId()));
            }
        }, DEATH_ANIMATION_DURATION);
    }
}