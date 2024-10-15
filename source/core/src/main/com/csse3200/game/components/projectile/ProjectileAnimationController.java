package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

import java.util.List;

/**
 * This class listens to events relevant to a entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ProjectileAnimationController extends Component {
    private AnimationRenderComponent animator;
    private DirectionalNPCComponent directionalComponent;

    private static final List<String> PROJECTILE_TYPES = List.of(
            "fire_attack",
            "fire1",
            "fire2",
            "cthulu_bullet",
            "kitsune_bullet"
    );

    /**
     * Base create method for adding listener
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        directionalComponent = this.entity.getComponent(DirectionalNPCComponent.class);

        PROJECTILE_TYPES.forEach(type ->
                entity.getEvents().addListener(type, () -> animateProjectile(type))
        );
    }

    private void animateProjectile(String name) {
        if (animator.hasAnimation(name + "_right") && animator.hasAnimation(name + "_left")) {
            triggerDirectionalAnimation(name);
        } else if (animator.hasAnimation(name)) {
            animator.startAnimation(name);
        } else {
            throw new IllegalStateException("No " + name + " animation found");
        }
    }

    /**
     * Add directional suffix to trigger's call
     *
     * @param baseAnimation base animation trigger's call
     */
    private void triggerDirectionalAnimation(String baseAnimation) {
        String direction = directionalComponent.getDirection();
        if (direction.equals("right")) {
            animator.startAnimation(baseAnimation + "_right");
        } else {
            animator.startAnimation(baseAnimation + "_left");
        }
    }
}
