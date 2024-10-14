package com.csse3200.game.areas.minimap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinimapComponent extends RenderComponent {
    private static final Logger logger = LoggerFactory.getLogger(MinimapComponent.class);
    private static final int MINIMAP_LAYER = 1;

    private final TiledMap tiledMap;
    private final TiledMapRenderer tiledMapRenderer;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;

    private final float tileSize;

    public MinimapComponent(
            OrthographicCamera camera,
            TiledMap map,
            TiledMapRenderer renderer,
            float tileSize) {
        this.camera = camera;
        this.tiledMap = map;
        this.tileSize = tileSize;
        this.tiledMapRenderer = renderer;
        this.shapeRenderer = new ShapeRenderer();
    }

    public Vector2 tileToWorldPosition(GridPoint2 tilePos) {
        return tileToWorldPosition(tilePos.x, tilePos.y);
    }

    public Vector2 tileToWorldPosition(int x, int y) {
        return new Vector2(x * tileSize, y * tileSize);
    }

    public float getTileSize() {
        return tileSize;
    }

    public GridPoint2 getMapBounds(int layer) {
        TiledMapTileLayer minimapLayer = (TiledMapTileLayer)tiledMap.getLayers().get(layer);
        return new GridPoint2(minimapLayer.getWidth(), minimapLayer.getHeight());
    }

    public TiledMap getMap() {
        return tiledMap;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.end();
        // Render the minimap
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        drawPlayerLocator();

        batch.begin();
    }

    private void drawPlayerLocator() {
        // Start ShapeRenderer in filled mode
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Set the color to green
        shapeRenderer.setColor(Color.GREEN);

        // Calculate the position
        Vector2 position = tileToWorldPosition(2, 2);

        float locatorSize = tileSize/4;

        position.x += (tileSize / 2) - (locatorSize/2);
        position.y += (tileSize / 2) - (locatorSize/2);

        // Draw the player locator
        shapeRenderer.rect(position.x, position.y, locatorSize, locatorSize);

        // End ShapeRenderer
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        if (tiledMap != null) {
            tiledMap.dispose();
        }
        shapeRenderer.dispose();
        try {
            if (ServiceLocator.getRenderService() != null) {
                ServiceLocator.getRenderService().unregister(this);
            }
        } catch (NullPointerException e) {
            // Log or handle the case where RenderService is not available
            logger.error("RenderService not available during dispose");
        }
    }

    @Override
    public float getZIndex() {
        return 0f;
    }

    @Override
    public int getLayer() {
        return MINIMAP_LAYER;
    }

}
