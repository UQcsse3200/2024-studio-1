package com.csse3200.game.areas.minimap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MinimapTileTest {

        @Mock
        private TextureRegion textureRegion;
        private MinimapTile minimapTile;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            minimapTile = new MinimapTile(textureRegion);
        }

        @Test
        void testGetSetId() {
            minimapTile.setId(5);
            assertEquals(5, minimapTile.getId());
        }

        @Test
        void testGetSetBlendMode() {
            assertEquals(TiledMapTile.BlendMode.ALPHA, minimapTile.getBlendMode());
            minimapTile.setBlendMode(TiledMapTile.BlendMode.NONE);
            assertEquals(TiledMapTile.BlendMode.NONE, minimapTile.getBlendMode());
        }



        @Test
        void testGetSetOffsetX() {
            minimapTile.setOffsetX(10f);
            assertEquals(10f, minimapTile.getOffsetX());
        }

        @Test
        void testGetSetOffsetY() {
            minimapTile.setOffsetY(15f);
            assertEquals(15f, minimapTile.getOffsetY());
        }

        @Test
        void testGetProperties() {
            assertNull(minimapTile.getProperties());
        }

        @Test
        void testGetObjects() {
            assertNull(minimapTile.getObjects());
        }

}
