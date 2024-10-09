package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents an effect animation entity (e.g., stun, poison) attached to a target entity.
 */
public class EffectEntity extends Entity {
    private static final Logger logger = LoggerFactory.getLogger(EffectEntity.class);
    private final String animationName;
    private float duration;
    private final Entity targetEntity;
    private final Runnable disposeCallback;

    /**
     * Constructs an EffectEntity.
     *
     * @param atlas             The texture atlas containing the effect animation.
     * @param animationName     The name of the animation within the atlas.
     * @param duration          Duration of the effect in seconds.
     * @param target            The entity to which the effect is overlaid on.
     * @param disposeCallback   Callback to run when the effect entity is disposed.
     */
    public EffectEntity(TextureAtlas atlas, String animationName, float duration, Entity target,
                        Runnable disposeCallback) {
        this.animationName = animationName;
        this.duration = duration;
        this.targetEntity = target;
        this.disposeCallback = disposeCallback;

        // Set up the AnimationRenderComponent
        AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
        boolean added = animator.addAnimation(animationName, 0.2f, Animation.PlayMode.LOOP);
        if (!added) {
            logger.error("Failed to add animation '{}' to EffectEntity.", animationName);
        }
        animator.setOpacity(0.8f);
        animator.startAnimation(animationName);
        addComponent(animator);

        // Scale the effect entity
        scaleEffectEntity();
    }

    @Override
    public void update() {
        super.update();
        // Update position to follow the target entity
        Vector2 targetPos = targetEntity.getCenterPosition();
        Vector2 entitySize = this.getScale();
        this.setPosition(targetPos.x - entitySize.x/2, targetPos.y - entitySize.y/2);

        // Update the effect duration
        duration -= ServiceLocator.getTimeSource().getDeltaTime();
        if (duration <= 0) {
            logger.debug("Effect '{}' expired. Disposing EffectEntity.", animationName);
            dispose();
        }
    }

    private void scaleEffectEntity() {
        // Scale the effect entity based on the target entity's size and the effect type
        Vector2 targetSize = targetEntity.getScale();
        float scaleFactor = 1.0f; // Default scale factor
        if (Objects.equals(animationName, "stun")) {
            scaleFactor = 0.5f;
        }
        this.setScale(targetSize.x * scaleFactor, targetSize.y * scaleFactor);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (disposeCallback != null) {
            disposeCallback.run();
        }
    }

}