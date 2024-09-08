package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory to create animation components.
 */
public class AnimationFactory {

    public AnimationRenderComponent createAnimationComponent(String textureAtlasFilename) {
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset(textureAtlasFilename, TextureAtlas.class));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-left", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-up", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-right", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-down", 0.2f, Animation.PlayMode.LOOP);
        return animator;
    }
}
