package com.csse3200.game.components.npc;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * This component handles the death process for NPCs.
 * It plays a death animation, disables physics and AI, and removes the entity from the game.
 */
public class NPCDeathHandler extends Component {
    public static final float DEATH_ANIMATION_DURATION = 1.0f;
    public static final List<Integer> deadEntities = new ArrayList<>();

    private AnimationRenderComponent animator;
    private CombatStatsComponent combatStats;
    private boolean isDead = false;
    private Entity target;

    public NPCDeathHandler(Entity target) {
        this.target = target;
    }

    public NPCDeathHandler() {

    }
    /**
     * Called when the entity is created and registered. Sets up components and listeners.
     */
    @Override
    public void create() {
        animator = entity.getComponent(AnimationRenderComponent.class);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        entity.getEvents().addListener("died", this::onDeath);
    }

    /**
     * Handles the death of the entity by playing the death animation,
     * disabling physics and AI components, and scheduling the entity's removal from the game.
     */
    protected void onDeath() {
        if (!isDead) {
            isDead = true;
            deadEntities.add(entity.getId());
            target.getEvents().trigger("collectCoin");

            // Play death animation if available
            if (animator != null && animator.hasAnimation("death")) {
                animator.startAnimation("death");
            }

            // Disable physics and AI components to prevent further interaction
            //entity.getComponent(PhysicsComponent.class).setEnabled(false);
            //entity.getComponent(AITaskComponent.class).setEnabled(false);

            // Schedule entity removal after the death animation completes
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    ServiceLocator.getGameAreaService().getGameArea().disposeEntity(entity);
                    deadEntities.remove(Integer.valueOf(entity.getId()));
                }
            }, DEATH_ANIMATION_DURATION);
        }
    }
}
