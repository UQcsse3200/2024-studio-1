package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a entity's state and plays the animation when one
 * of the events is triggered.
 */
public class NPCAnimationController extends Component {
    AnimationRenderComponent animator;
    DirectionalNPCComponent directionalComponent;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        directionalComponent = this.entity.getComponent(DirectionalNPCComponent.class);

        entity.getEvents().addListener("idle", this::animateIdle);
        entity.getEvents().addListener("gesture", this::animateGesture);
        entity.getEvents().addListener("walk", this::animateWalk);
        entity.getEvents().addListener("run", this::animateRun);
        entity.getEvents().addListener("attack", this::animateAttack);
        entity.getEvents().addListener("death", this::animateDeath);
        entity.getEvents().addListener("hurt", this::animateHurt);
        entity.getEvents().addListener("jump", this::animateJump);
    }

    void animateIdle() {
        if (animator.hasAnimation("idle_right") && animator.hasAnimation("idle_left")) {
            triggerDirectionalAnimation("idle");
        } else if (animator.hasAnimation("idle")) {
            animator.startAnimation("idle");
        } else {
            throw new IllegalStateException("No idle animation found");
        }
    }

    void animateGesture() {
        if (animator.hasAnimation("gesture_right") && animator.hasAnimation("gesture_left")) {
            triggerDirectionalAnimation("gesture");
        } else if (animator.hasAnimation("gesture")) {
            animator.startAnimation("gesture");
        } else {
            animateIdle();
        }
    }

    void animateWalk() {
        if (animator.hasAnimation("walk_right") && animator.hasAnimation("walk_left")) {
            triggerDirectionalAnimation("walk");
        } else if (animator.hasAnimation("walk")) {
            animator.startAnimation("walk");
        } else {
            throw new IllegalStateException("No walk animation found");
        }
    }

    void animateRun() {
        if (animator.hasAnimation("run_right") && animator.hasAnimation("run_left")) {
            triggerDirectionalAnimation("run");
        } else if (animator.hasAnimation("run")) {
            animator.startAnimation("run");
        } else {
            animateWalk();
        }
    }

    void animateAttack() {
        if (animator.hasAnimation("attack_right") && animator.hasAnimation("attack_left")) {
            triggerDirectionalAnimation("attack");
        } else if (animator.hasAnimation("attack")) {
            animator.startAnimation("attack");
        } else {
            throw new IllegalStateException("No attack animation found");
        }
    }

    void animateDeath() {
        if (animator.hasAnimation("death_right") && animator.hasAnimation("death_left")) {
            triggerDirectionalAnimation("death");
        } else if (animator.hasAnimation("death")) {
            animator.startAnimation("death");
        } else {
            throw new IllegalStateException("No death animation found");
        }
    }

    void animateHurt() {
        if (animator.hasAnimation("hurt_right") && animator.hasAnimation("hurt_left")) {
            triggerDirectionalAnimation("hurt");
        } else if (animator.hasAnimation("hurt")) {
            animator.startAnimation("hurt");
        } else {
            throw new IllegalStateException("No hurt animation found");
        }
    }

    void animateJump() {
        if (animator.hasAnimation("jump_right") && animator.hasAnimation("jump_left")) {
            triggerDirectionalAnimation("jump");
        } else if (animator.hasAnimation("jump")) {
            animator.startAnimation("jump");
        } else {
            throw new IllegalStateException("No jump animation found");
        }
    }

    private void triggerDirectionalAnimation(String baseAnimation) {
        String direction = directionalComponent.getDirection();
        if (direction.equals("right")) {
            animator.startAnimation(baseAnimation + "_right");
        } else {
            animator.startAnimation(baseAnimation + "_left");
        }
    }
}
