package com.csse3200.game.areas.minimap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

public class MinimapTile implements TiledMapTile {
    private int id;
    private BlendMode blendMode = BlendMode.ALPHA;
    private TextureRegion textureRegion;
    private float offsetX;
    private float offsetY;

    public MinimapTile(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public BlendMode getBlendMode() {
        return blendMode;
    }

    @Override
    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    @Override
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    @Override
    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public float getOffsetX() {
        return offsetX;
    }

    @Override
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    @Override
    public float getOffsetY() {
        return offsetY;
    }

    @Override
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    /**
     * Not required for game, unimplemented
     * @return null
     */
    @Override
    public MapProperties getProperties() {
        return null;
    }

    /**
     * Not required for game, unimplemented
     * @return null
     */
    @Override
    public MapObjects getObjects() {
        return null;
    }
}