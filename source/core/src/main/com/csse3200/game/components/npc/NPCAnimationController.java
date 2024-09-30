package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.sql.Time;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class NPCAnimationController extends Component {
    private AnimationRenderComponent animator;
    private DirectionalNPCComponent directionalComponent;
    private Boolean dead;
    private Image circle;
    private Stage stage;
    private OrthographicCamera orthographicCamera;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        directionalComponent = this.entity.getComponent(DirectionalNPCComponent.class);
        dead = false;
        Camera camera = ServiceLocator.getRenderService().getCamera().getCamera();
        if (camera instanceof OrthographicCamera) {
            orthographicCamera = (OrthographicCamera) camera;
        } else {
            throw new IllegalStateException("Camera is not an OrthographicCamera");
        }

        // Load the circle image
        circle = new Image(new Texture("images/npc/glowCircle.png"));
        circle.setSize(300f, 300);
        circle.setVisible(false);
        // Get the stage to display the circle
        stage = ServiceLocator.getRenderService().getStage();
        stage.addActor(circle); // Add circle to stage so it's rendered


        // Set up event listeners
        entity.getEvents().addListener("idle", this::animateIdle);
        entity.getEvents().addListener("gesture", this::animateGesture);
        entity.getEvents().addListener("walk", this::animateWalk);
        entity.getEvents().addListener("run", this::animateRun);
        entity.getEvents().addListener("attack", this::animateAttack);
        entity.getEvents().addListener("death", this::animateDeath);
        entity.getEvents().addListener("hurt", this::animateHurt);
        entity.getEvents().addListener("jump", this::animateJump);
        entity.getEvents().addListener("updateCircle", this::update);
        entity.getEvents().addListener("aoe_attack", this::aoeAnimation);
    }
    /**
     * Displays and animates the circle around the player when an AOE attack occurs.
     */
    private void aoeAnimation() {
        circle.setVisible(true);
        update();
        // Hide the circle after 1 seconds
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                hideCircle();
            }
        }, 1f);

    }

    /**
     * Update the circle's position to be centered around the NPC's current position in screen coordinates.
     */
    @Override
    public void update() {
        if (circle.isVisible()) {
            // Convert world coordinates to screen coordinates
            Vector3 screenPosition = orthographicCamera.project(new Vector3(entity.getCenterPosition().x,
                    entity.getCenterPosition().y, 0));

            // Set the circle's position so that it's centered on the entity in screen space
            circle.setPosition(screenPosition.x - circle.getWidth() / 2,
                    screenPosition.y - circle.getHeight() / 2);
        }
    }

    /**
     * Hides the circle after the AOE animation is done.
     */
    private void hideCircle() {
        circle.setVisible(false);
    }


    void animateIdle() {
        if (!dead) {
            if (animator.hasAnimation("idle_right") && animator.hasAnimation("idle_left")) {
                triggerDirectionalAnimation("idle");
            } else if (animator.hasAnimation("idle")) {
                animator.startAnimation("idle");
            } else {
                throw new IllegalStateException("No idle animation found");
            }
        }
    }

    void animateGesture() {
        if (!dead) {
            if (animator.hasAnimation("gesture_right") && animator.hasAnimation("gesture_left")) {
                triggerDirectionalAnimation("gesture");
            } else if (animator.hasAnimation("gesture")) {
                animator.startAnimation("gesture");
            } else {
                animateIdle();
            }
        }
    }

    void animateWalk() {
        if (!dead) {
            if (animator.hasAnimation("walk_right") && animator.hasAnimation("walk_left")) {
                triggerDirectionalAnimation("walk");
            } else if (animator.hasAnimation("walk")) {
                animator.startAnimation("walk");
            } else {
                throw new IllegalStateException("No walk animation found");
            }
        }
    }

    void animateRun() {
        if (!dead) {
            if (animator.hasAnimation("run_right") && animator.hasAnimation("run_left")) {
                triggerDirectionalAnimation("run");
            } else if (animator.hasAnimation("run")) {
                animator.startAnimation("run");
            } else {
                animateWalk();
            }
        }
    }

    void animateAttack() {
        if (!dead) {
            if (animator.hasAnimation("attack_right") && animator.hasAnimation("attack_left")) {
                triggerDirectionalAnimation("attack");
            } else if (animator.hasAnimation("attack")) {
                animator.startAnimation("attack");
            } else {
                throw new IllegalStateException("No attack animation found");
            }
        }
    }

    void animateDeath() {
        if (!dead) {
            dead = true;
            if (animator.hasAnimation("death_right") && animator.hasAnimation("death_left")) {
                triggerDirectionalAnimation("death");
            } else if (animator.hasAnimation("death")) {
                animator.startAnimation("death");
            } else {
                throw new IllegalStateException("No death animation found");
            }
        }
    }

    void animateHurt() {
        if (!dead) {
            if (animator.hasAnimation("hurt_right") && animator.hasAnimation("hurt_left")) {
                triggerDirectionalAnimation("hurt");
            } else if (animator.hasAnimation("hurt")) {
                animator.startAnimation("hurt");
            } else {
                throw new IllegalStateException("No hurt animation found");
            }
        }
    }

    void animateJump() {
        if (!dead) {
            if (animator.hasAnimation("jump_right") && animator.hasAnimation("jump_left")) {
                triggerDirectionalAnimation("jump");
            } else if (animator.hasAnimation("jump")) {
                animator.startAnimation("jump");
            } else {
                throw new IllegalStateException("No jump animation found");
            }
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