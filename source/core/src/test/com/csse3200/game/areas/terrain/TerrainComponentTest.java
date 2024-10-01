package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.rendering.RenderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class TerrainComponentTest {

  private TerrainComponent terrainComponent;
  private OrthographicCamera mockCamera;
  private TiledMap mockMap;
  private TiledMapRenderer mockRenderer;
  private RenderService mockRenderService;
  private float tileSize = 32f;

  @BeforeEach
  void setUp() {
    mockCamera = mock(OrthographicCamera.class);
    mockMap = spy(TiledMap.class);
    mockRenderer = mock(TiledMapRenderer.class);
    mockRenderService = mock(RenderService.class);

    // Mock ServiceLocator
    ServiceLocator.clear();
    ServiceLocator.registerRenderService(mockRenderService);

    terrainComponent = new TerrainComponent(mockCamera, mockMap, mockRenderer, tileSize);
  }

  @AfterEach
  void tearDown() {
    ServiceLocator.clear();
  }

  @Test
  void testTileToWorldPosition() {
    assertEquals(new Vector2(0f, 0f), terrainComponent.tileToWorldPosition(0, 0));
    assertEquals(new Vector2(64f, 96f), terrainComponent.tileToWorldPosition(2, 3));
    assertEquals(new Vector2(-32f, -32f), terrainComponent.tileToWorldPosition(-1, -1));
  }

  @Test
  void testTileToWorldPositionWithGridPoint2() {
    assertEquals(new Vector2(0f, 0f), terrainComponent.tileToWorldPosition(new GridPoint2(0, 0)));
    assertEquals(new Vector2(64f, 96f), terrainComponent.tileToWorldPosition(new GridPoint2(2, 3)));
    assertEquals(new Vector2(-32f, -32f), terrainComponent.tileToWorldPosition(new GridPoint2(-1, -1)));
  }

  @Test
  void testGetTileSize() {
    assertEquals(tileSize, terrainComponent.getTileSize());
  }

  @Test
  void testGetMapBounds() {
    TiledMapTileLayer mockLayer = mock(TiledMapTileLayer.class);
    when(mockLayer.getWidth()).thenReturn(10);
    when(mockLayer.getHeight()).thenReturn(15);

    MapLayers mockLayers = mock(MapLayers.class);
    when(mockLayers.get(0)).thenReturn(mockLayer);
    when(mockMap.getLayers()).thenReturn(mockLayers);

    assertEquals(new GridPoint2(10, 15), terrainComponent.getMapBounds(0));
  }

  @Test
  void testGetMap() {
    assertEquals(mockMap, terrainComponent.getMap());
  }

  @Test
  void testDraw() {
    SpriteBatch mockBatch = mock(SpriteBatch.class);
    terrainComponent.draw(mockBatch);

    verify(mockRenderer).setView(mockCamera);
    verify(mockRenderer).render();
  }

  @Test
  void testDispose() {
    terrainComponent.dispose();
    verify(mockMap, times(1)).dispose();
    verify(mockRenderService, times(1)).unregister(terrainComponent);
  }

  /**@Test
  void testGetZIndex() {
    assertEquals(0f, terrainComponent.getZIndex());
  }*/

  @Test
  void testGetLayer() {
    assertEquals(0, terrainComponent.getLayer());
  }
}