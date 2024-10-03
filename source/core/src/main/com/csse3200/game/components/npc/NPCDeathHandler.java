package com.csse3200.game.components.npc;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This component handles the death process for NPCs.
 * It plays a death animation, disables physics and AI, and removes the entity from the game.
 */
public class NPCDeathHandler extends Component {
    public static final float DEATH_ANIMATION_DURATION = 1.0f;
    private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
    public static final List<Integer> deadEntities = new ArrayList<>();

    private AnimationRenderComponent animator;
    private boolean isDead = false;
    private Entity target;
    private int npcStrength;
    private static int deadCount;

    public NPCDeathHandler(Entity target, int npcStrength) {
        this.target = target;
        this.npcStrength = npcStrength;
    }

    public NPCDeathHandler() {

    }
    /**
     * Called when the entity is created and registered. Sets up components and listeners.
     */
    @Override
    public void create() {
        animator = entity.getComponent(AnimationRenderComponent.class);
        entity.getComponent(CombatStatsComponent.class);
        entity.getEvents().addListener("died", this::onDeath);
    }

    /**
     * Handles the death of the entity by playing the death animation,
     * triggering event for player to obtain animal's strength as score,
     * disabling physics and AI components, and scheduling the entity's
     * removal from the game.
     */
    protected void onDeath() {
        if (!isDead) {
            isDead = true;
            deadEntities.add(entity.getId());
            deadCount++;
            target.getEvents().trigger("defeatedEnemy", deadCount);
            // triggering event for player to get animal's strength as coins
            String event = "collectCoin";
            target.getEvents().trigger(event, npcStrength);
            logger.info("{}   is triggered", event);

            // Play death animation if available
            if (animator != null && animator.hasAnimation("death")) {
                animator.startAnimation("death");
            }
            // disable AI component to prevent further interaction
            for (var componentClass : List.of(AITaskComponent.class, PhysicsMovementComponent.class, HitboxComponent.class, ColliderComponent.class)) {
                var component = entity.getComponent(componentClass);
                if (component != null) {
                    component.setEnabled(false);
                }
            }

            // Schedule entity removal after the death animation completes
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    GameAreaService gameAreaService = ServiceLocator.getGameAreaService();
                    if (gameAreaService != null) {
                        gameAreaService.getGameArea().disposeEntity(entity);
                        deadEntities.remove(Integer.valueOf(entity.getId()));
                    }
                }
            }, DEATH_ANIMATION_DURATION);
        }
    }
}
