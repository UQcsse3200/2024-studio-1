package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Renders animations from a texture atlas on an entity.
 */
public class EffectAnimationRenderComponent extends AnimationRenderComponent {
    private static final int EFFECT_LAYER = 3;

    /**
     * Create the component for a given texture atlas.
     * @param atlas libGDX-supported texture atlas containing desired animations
     */
    public EffectAnimationRenderComponent(TextureAtlas atlas) {
        super(atlas);
    }

    /**
     * Set the layer of the effect to be higher than the player
     * @return The layer of the effect
     */
    @Override
    public int getLayer() {
        return EFFECT_LAYER;
    }
}
