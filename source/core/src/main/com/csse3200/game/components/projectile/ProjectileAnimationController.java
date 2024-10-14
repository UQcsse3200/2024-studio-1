package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ProjectileAnimationController extends Component {
    AnimationRenderComponent animator;
    DirectionalNPCComponent directionalComponent;

    /**
     * Base create method for adding listener
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        directionalComponent = this.entity.getComponent(DirectionalNPCComponent.class);

        entity.getEvents().addListener("fire_attack", this::animateFire);
        entity.getEvents().addListener("fire1", this::animateFireKitsune1);
        entity.getEvents().addListener("fire2", this::animateFireKitsune2);
        entity.getEvents().addListener("cthulu_bullet", this::animateCthuluBullet);
        entity.getEvents().addListener("kitsune_bullet", this::animateKitsuneBullet);
    }

    void animateCthuluBullet() {
        if (animator.hasAnimation("cthulu_bullet_right") && animator.hasAnimation("cthulu_bullet_left")) {
            triggerDirectionalAnimation("cthulu_bullet");
        } else if (animator.hasAnimation("cthulu_bullet")) {
            animator.startAnimation("cthulu_bullet");
        } else {
            throw new IllegalStateException("No cthulu_bullet animation found");
        }
    }

    void animateKitsuneBullet() {
        if (animator.hasAnimation("kitsune_bullet_right") && animator.hasAnimation("kitsune_bullet_left")) {
            triggerDirectionalAnimation("kitsune_bullet");
        } else if (animator.hasAnimation("kitsune_bullet")) {
            animator.startAnimation("kitsune_bullet");
        } else {
            throw new IllegalStateException("No kitsune_bullet animation found");
        }
    }

    /**
     * Animate Fire for dragon projectile
     */
    void animateFire() {
        if (animator.hasAnimation("fire_attack_right") && animator.hasAnimation("fire_attack_left")) {
            triggerDirectionalAnimation("fire_attack");
        } else if (animator.hasAnimation("fire_attack")) {
            animator.startAnimation("fire_attack");
        } else {
            throw new IllegalStateException("No fire_attack animation found");
        }
    }

    /**
     * Animate Kitsune fire type 1 for kitsune projectile
     */
    void animateFireKitsune1() {
        if (animator.hasAnimation("fire1_right") && animator.hasAnimation("fire1_left")) {
            triggerDirectionalAnimation("fire1");
        } else if (animator.hasAnimation("fire1")) {
            animator.startAnimation("fire1");
        } else {
            throw new IllegalStateException("No fire_attack_1 animation found");
        }
    }

    /**
     * Animate Kitsune fire type 2 for kitsune projectile
     */
    void animateFireKitsune2() {
        if (animator.hasAnimation("fire2_right") && animator.hasAnimation("fire2_left")) {
            triggerDirectionalAnimation("fire2");
        } else if (animator.hasAnimation("fire2")) {
            animator.startAnimation("fire2");
        } else {
            throw new IllegalStateException("No fire_attack_2 animation found");
        }
    }

    /**
     * Add directional suffix to trigger's call
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
