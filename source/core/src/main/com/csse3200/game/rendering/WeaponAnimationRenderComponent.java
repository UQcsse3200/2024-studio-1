package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class WeaponAnimationRenderComponent extends AnimationRenderComponent{
    private static final int WEAPON_LAYER = 2;

    public WeaponAnimationRenderComponent(TextureAtlas atlas) {
        super(atlas);
    }

    @Override
    public int getLayer() {
        return WEAPON_LAYER;
    }
}
