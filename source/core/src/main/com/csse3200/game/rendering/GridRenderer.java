package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GridRenderer extends OrthogonalTiledMapRenderer {

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private float tileWidth;
    private float tileHeight;
    private int mapWidthInTiles;
    private int mapHeightInTiles;

    public GridRenderer(TiledMap map, float unitScale, OrthographicCamera camera) {
        super(map, unitScale);
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();

        // Get the dimensions of the map in tiles
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        this.mapWidthInTiles = layer.getWidth();
        this.mapHeightInTiles = layer.getHeight();

        // Calculate tile width and height in world units
        this.tileWidth = layer.getTileWidth() * unitScale;
        this.tileHeight = layer.getTileHeight() * unitScale;
    }

    @Override
    public void render() {
        super.render();
        drawGrid();
    }

    private void drawGrid() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1f);

        // Draw vertical lines
        for (int x = 0; x <= mapWidthInTiles; x++) {
            float xPos = x * tileWidth;
            shapeRenderer.line(xPos, 0, xPos, mapHeightInTiles * tileHeight);
        }

        // Draw horizontal lines
        for (int y = 0; y <= mapHeightInTiles; y++) {
            float yPos = y * tileHeight;
            shapeRenderer.line(0, yPos, mapWidthInTiles * tileWidth, yPos);
        }

        shapeRenderer.end();
    }

}
