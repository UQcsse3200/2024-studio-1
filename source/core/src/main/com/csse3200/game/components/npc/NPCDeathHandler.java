package com.csse3200.game.components.npc;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This component handles the death process for NPCs.
 * It plays a death animation, disables physics and AI, and removes the entity from the game.
 */
public class NPCDeathHandler extends Component {
    public static final float DEATH_ANIMATION_DURATION = 1.0f;

    public AnimationRenderComponent animator;

    /**
     * Called when the entity is created and registered. Sets up the death event listener and gets the animator component.
     */
    @Override
    public void create() {
        // Set up the death event listener and get the animator component
        entity.getEvents().addListener("death", this::onDeath);
        animator = entity.getComponent(AnimationRenderComponent.class);
    }

    /**
     * Handles the death of the NPC by playing the death animation,
     * disabling physics and AI components, and scheduling the NPC's removal from the game.
     */
    void onDeath() {

        // Play death animation if available
        if (animator != null && animator.hasAnimation("death")) {
            animator.startAnimation("death");
        }

        // Disable physics and AI components to prevent further interaction
        entity.getComponent(PhysicsComponent.class).setEnabled(false);
        entity.getComponent(AITaskComponent.class).setEnabled(false);

        // Schedule entity removal after the death animation completes
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                ServiceLocator.getEntityService().unregister(entity);
                entity.dispose();
                NPCDamageHandlerComponent.deadAnimals.remove(Integer.valueOf(entity.getId()));
            }
        }, DEATH_ANIMATION_DURATION);
    }
}