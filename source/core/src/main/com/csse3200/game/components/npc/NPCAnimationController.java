package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.DirectionalNPCComponent;
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
    }

    void animateIdle() {
        if (directionalComponent.isDirectable()) {
            triggerDirectionalAnimation("idle");
        } else {
            animator.startAnimation("idle");
        }
    }
    void animateGesture() {
        if (animator.hasAnimation("gesture")) {
            if (directionalComponent.isDirectable()) {
                triggerDirectionalAnimation("gesture");
            } else {
                animator.startAnimation("gesture");
            }
        } else {
            animateIdle();
        }
    }
    void animateWalk() {
        if (directionalComponent.isDirectable()) {
            triggerDirectionalAnimation("walk");
        } else {
            animator.startAnimation("walk");
        }
    }
    void animateRun() {
        if (animator.hasAnimation("run")) {
            if (directionalComponent.isDirectable()) {
                triggerDirectionalAnimation("run");
            } else {
                animator.startAnimation("run");
            }
        } else {
            animateWalk();
        }
    }
    void animateAttack() {
        if (directionalComponent.isDirectable()) {
            triggerDirectionalAnimation("attack");
        } else {
            animator.startAnimation("attack");
        }
    }
    void animateDeath() {
        if (directionalComponent.isDirectable()) {
            triggerDirectionalAnimation("death");
        } else {
            animator.startAnimation("death");
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
