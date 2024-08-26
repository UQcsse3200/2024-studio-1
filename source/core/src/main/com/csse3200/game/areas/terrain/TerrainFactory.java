package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import com.badlogic.gdx.math.GridPoint2;

import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.rendering.GridRenderer;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final GridPoint2 MAP_SIZE = new GridPoint2(15, 11);
  private final OrthographicCamera camera;
  private static GridPoint2 STAIRCASE_UP_POS = new GridPoint2(0, MAP_SIZE.y-3);
  private static GridPoint2 STAIRCASE_DOWN_POS = new GridPoint2(MAP_SIZE.x-1, 3);


  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
  }

  /**
   * Create a terrain factory
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation orientation to render terrain at
   */



  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @param terrainType Terrain to create
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    switch (terrainType) {
      case ROOM1:
        TextureRegion tileMain =
                new TextureRegion(resourceService.getAsset("images/tile_middle.png", Texture.class));
        TextureRegion tileLU =
                new TextureRegion(resourceService.getAsset("images/tile_1.png", Texture.class));
        TextureRegion tileLD =
                new TextureRegion(resourceService.getAsset("images/tile_4.png", Texture.class));
        TextureRegion tileRU =
                new TextureRegion(resourceService.getAsset("images/tile_2.png", Texture.class));
        TextureRegion tileRD =
                new TextureRegion(resourceService.getAsset("images/tile_3.png", Texture.class));
        TextureRegion tileL =
                new TextureRegion(resourceService.getAsset("images/tile_8.png", Texture.class));
        TextureRegion tileR =
                new TextureRegion(resourceService.getAsset("images/tile_6.png", Texture.class));
        TextureRegion tileU =
                new TextureRegion(resourceService.getAsset("images/tile_5.png", Texture.class));
        TextureRegion tileD =
                new TextureRegion(resourceService.getAsset("images/tile_7.png", Texture.class));
        TextureRegion tileB1 =
                new TextureRegion(resourceService.getAsset("images/tile_broken1.png", Texture.class));
        TextureRegion tileB2 =
                new TextureRegion(resourceService.getAsset("images/tile_broken2.png", Texture.class));
        TextureRegion tileB3 =
                new TextureRegion(resourceService.getAsset("images/tile_broken3.png", Texture.class));
        TextureRegion tileStaircase =
                new TextureRegion(resourceService.getAsset("images/tile_staircase.png", Texture.class));
        TextureRegion tileStaircaseDown =
                new TextureRegion(resourceService.getAsset("images/tile_staircase_down.png", Texture.class));
        TextureRegion tileStained =
                new TextureRegion(resourceService.getAsset("images/tile_blood.png", Texture.class));
        return createRoomTerrain(1f, new TextureRegion[]{
                tileMain, tileLU, tileLD, tileRU, tileRD, tileL, tileR, tileU, tileD ,tileB1, tileB2, tileB3, tileStaircase, tileStaircaseDown, tileStained});
      default:
        return null;
    }
  }

  private TerrainComponent createRoomTerrain(float tileWorldSize, TextureRegion[] tileSet) {
    GridPoint2 tilePixelSize = new GridPoint2(tileSet[0].getRegionWidth(), tileSet[0].getRegionHeight());
    TiledMap tiledMap = createRoomTiles(tilePixelSize, tileSet);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize/tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, tileWorldSize);
  }

  private TiledMap createRoomTiles(GridPoint2 tileSize, TextureRegion[] tileSet) {
    TiledMap tiledMap = new TiledMap();

    TerrainTile mainTile = new TerrainTile(tileSet[0]);
    TerrainTile luTile = new TerrainTile(tileSet[1]);
    TerrainTile ldTile = new TerrainTile(tileSet[2]);
    TerrainTile ruTile = new TerrainTile(tileSet[3]);
    TerrainTile rdTile = new TerrainTile(tileSet[4]);
    TerrainTile lTile = new TerrainTile(tileSet[5]);
    TerrainTile rTile = new TerrainTile(tileSet[6]);
    TerrainTile uTile = new TerrainTile(tileSet[7]);
    TerrainTile dTile = new TerrainTile(tileSet[8]);
    TerrainTile b1Tile = new TerrainTile(tileSet[9]);
    TerrainTile b2Tile = new TerrainTile(tileSet[10]);
    TerrainTile b3Tile = new TerrainTile(tileSet[11]);
    TerrainTile stairTile = new TerrainTile(tileSet[12]);
    TerrainTile stairDownTile = new TerrainTile(tileSet[13]);
    TerrainTile stairStained = new TerrainTile(tileSet[14]);

    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // fill room tile
    fillTiles(layer, MAP_SIZE, new TerrainTile[]{
            mainTile, luTile, ldTile, ruTile, rdTile, lTile, rTile, uTile, dTile,
            b1Tile, b2Tile, b3Tile, stairTile, stairDownTile, stairStained
    });

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private static void fillTilesAtRandom(
          TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  private void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  private void fillTiles(
          TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile[] tileList) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        if (!isBoundaryTile(x, y, mapSize)) {
          // adding broken tiles
          if (isBrokenTile()) {
            Cell cell = new Cell();
            cell.setTile(tileList[getBrokenTile()]);
            layer.setCell(x, y, cell);
          } else if (isStainedTile()) {
            Cell cell = new Cell();
            cell.setTile(tileList[14]);
            layer.setCell(x, y, cell);
          } else {
            // general tiles
            Cell cell = new Cell();
            cell.setTile(tileList[0]);
            layer.setCell(x, y, cell);
          }
        } else {
          Cell cell = new Cell();
          if (x == 0 && y == 0) {
            cell.setTile(tileList[2]);
          } else if (x == 0 && y == (mapSize.y-1)) {
            cell.setTile(tileList[1]);
          } else if (x == (mapSize.x-1) && y == 0) {
            cell.setTile(tileList[4]);
          } else if (x == (mapSize.x-1) && y == (mapSize.y-1)) {
            cell.setTile(tileList[3]);
          } else if (y > 0 && y < (mapSize.y-1) && x == 0) {
            cell.setTile(tileList[5]);
          } else if (y > 0 && y < (mapSize.y-1) && x == (mapSize.x-1)) {
            cell.setTile(tileList[6]);
          } else if (x > 0 && x < (mapSize.x-1) && y == 0) {
            cell.setTile(tileList[8]);
          } else if (x > 0 && x < (mapSize.x-1) && y == (mapSize.y-1)) {
            cell.setTile(tileList[7]);
          }

          // staircase position
          if (x == STAIRCASE_UP_POS.x && y == (STAIRCASE_UP_POS.y))
            cell.setTile(tileList[12]);

          if (x == (STAIRCASE_DOWN_POS.x) && y == STAIRCASE_DOWN_POS.y)
            cell.setTile(tileList[13]);

          layer.setCell(x, y, cell);
        }
      }
    }
  }

  private boolean isBoundaryTile(int x, int y, GridPoint2 mapSize) {
    return x == 0 || x == mapSize.x - 1 || y == 0 || y == mapSize.y - 1;
  }

  // Return true for broken tile (30% chance), false for general tile (70% chance)
  public boolean isBrokenTile() {
    // Generate a random number between 0 and 2
    int randomValue = RandomUtils.randomInt(0, 99);
    return randomValue < 5;
  }

  // Return random broken tile
  public int getBrokenTile() {
    return RandomUtils.randomInt(9, 11);
  }

  // Return true for broken tile (5% chance), false for general tile (95% chance)
  public boolean isStainedTile() {
    // Generate a random number between 0 and 2
    int randomValue = RandomUtils.randomInt(0, 99);
    return randomValue < 3;
  }

  /**
   * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
   * the same oerientation. But for demonstration purposes, the base code has the same level in 3
   * different orientations.
   */
  public enum TerrainType {
    ROOM1
  }

  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    return new GridRenderer(tiledMap, tileScale, camera);
  }

}




