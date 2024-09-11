package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Renders animations from a texture atlas on an weapon entity.
 *
 * This class solely exist to change the layer to higher than player
 * The rest is the same as AnimationRenderComponent
 */
public class WeaponAnimationRenderComponent extends AnimationRenderComponent{
    private static final int WEAPON_LAYER = 2;

    /**
     * Create the component for a given texture atlas.
     * @param atlas libGDX-supported texture atlas containing desired animations
     */
    public WeaponAnimationRenderComponent(TextureAtlas atlas) {
        super(atlas);
    }

    /**
     * Set the layer of the weapon to be higher than the player
     * @return
     */
    @Override
    public int getLayer() {
        return WEAPON_LAYER;
    }
}
