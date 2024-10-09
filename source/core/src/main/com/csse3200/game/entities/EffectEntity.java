package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an effect animation entity (e.g., stun, poison) attached to a target entity.
 */
public class EffectEntity extends Entity {
    private static final Logger logger = LoggerFactory.getLogger(EffectEntity.class);
    private final String animationName;
    private float duration;
    private final Entity targetEntity;

    /**
     * Constructs an EffectEntity.
     *
     * @param atlas         The texture atlas containing the effect animation.
     * @param animationName The name of the animation within the atlas.
     * @param duration      Duration of the effect in seconds.
     * @param target        The entity to which the effect is applied.
     */
    public EffectEntity(TextureAtlas atlas, String animationName, float duration, Entity target) {
        this.animationName = animationName;
        this.duration = duration;
        this.targetEntity = target;

        // Set up the AnimationRenderComponent
        AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
        boolean added = animator.addAnimation(animationName, 0.2f, Animation.PlayMode.LOOP);
        if (!added) {
            logger.error("Failed to add animation '{}' to EffectEntity.", animationName);
        }
        animator.setOpacity(0.8f);
        animator.startAnimation(animationName);
        addComponent(animator);
    }

    @Override
    public void update() {
        super.update();
        // Update position to follow the target entity
        Vector2 targetPos = targetEntity.getPosition();
        this.setPosition(targetPos.x, targetPos.y);

        // Update the effect duration
        duration -= ServiceLocator.getTimeSource().getDeltaTime();
        if (duration <= 0) {
            logger.debug("Effect '{}' expired. Disposing EffectEntity.", animationName);
            dispose();
        }
    }
}