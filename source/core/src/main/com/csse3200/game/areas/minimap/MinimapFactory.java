package com.csse3200.game.areas.minimap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.areas.Level;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class MinimapFactory {

    private static final GridPoint2 MINIMAP_SIZE = new GridPoint2(5, 5);

    private final Level level;
    private final float mapScale;
    private final Skin minimapSkin;
    private final TextureRegion[] tileTextures;
    private TiledMapTileLayer layer;
    private String currentRoomKey;
    private int[] currentRoomLoc;
    private final OrthographicCamera camera;

    /**
     * Constructor for minimap display.
     *
     * @param level The game level containing the map and room information.
     * @param mapScale The scale of the minimap.
     */
    public MinimapFactory(Level level, float mapScale) {
        this.level = level;
        this.currentRoomKey = level.getStartingRoomKey();
        this.currentRoomLoc = parseRoomKey(currentRoomKey);
        this.mapScale = mapScale;
        this.camera = (OrthographicCamera) ServiceLocator.getRenderService().getCamera().getCamera();

        // Load skin for minimap
        this.minimapSkin = new Skin(Gdx.files.internal("skins/minimap/minimap.json"),
                ServiceLocator.getResourceService().getAsset("skins/minimap/minimap.atlas", TextureAtlas.class));

        // load textures
        this.tileTextures = new TextureRegion[]{
                minimapSkin.getRegion("1000"),
                minimapSkin.getRegion("1100"),
                minimapSkin.getRegion("1010"),
                minimapSkin.getRegion("1001"),
                minimapSkin.getRegion("1110"),
                minimapSkin.getRegion("1101"),
                minimapSkin.getRegion("1111"),
                minimapSkin.getRegion("0100"),
                minimapSkin.getRegion("0110"),
                minimapSkin.getRegion("0111"),
                minimapSkin.getRegion("0101"),
                minimapSkin.getRegion("0010"),
                minimapSkin.getRegion("0011"),
                minimapSkin.getRegion("0001"),
                minimapSkin.getRegion("1011"),
                minimapSkin.getRegion("0000"),
                minimapSkin.getRegion("1000_gambling"),
                minimapSkin.getRegion("1100_gambling"),
                minimapSkin.getRegion("1010_gambling"),
                minimapSkin.getRegion("1001_gambling"),
                minimapSkin.getRegion("1110_gambling"),
                minimapSkin.getRegion("1101_gambling"),
                minimapSkin.getRegion("1111_gambling"),
                minimapSkin.getRegion("0100_gambling"),
                minimapSkin.getRegion("0110_gambling"),
                minimapSkin.getRegion("0111_gambling"),
                minimapSkin.getRegion("0101_gambling"),
                minimapSkin.getRegion("0010_gambling"),
                minimapSkin.getRegion("0011_gambling"),
                minimapSkin.getRegion("0001_gambling"),
                minimapSkin.getRegion("1011_gambling"),
                minimapSkin.getRegion("1000_boss"),
                minimapSkin.getRegion("0100_boss"),
                minimapSkin.getRegion("0010_boss"),
                minimapSkin.getRegion("0001_boss")

        };
    }

    public MinimapComponent createMinimap() {
        return createMinimapGrid();
    }

    private MinimapComponent createMinimapGrid() {
        GridPoint2 tilePixelSize = new GridPoint2(tileTextures[0].getRegionWidth(), tileTextures[0].getRegionHeight());
        TiledMap tiledMap = createRoomTiles(tilePixelSize);
        TiledMapRenderer renderer = createRenderer(tiledMap, mapScale/tilePixelSize.x);
        return new MinimapComponent(camera, tiledMap, renderer, mapScale);
    }

    private TiledMap createRoomTiles(GridPoint2 tileSize) {
        TiledMap tiledMap = new TiledMap();

        this.layer = new TiledMapTileLayer(MINIMAP_SIZE.x, MINIMAP_SIZE.y, tileSize.x, tileSize.y);

        // fill room tile
        fillRooms(layer);

        tiledMap.getLayers().add(layer);

        return tiledMap;
    }

    /**
     * Fills the rooms on the minimap relative to the player's current position.
     *
     * @param tiledMapTileLayer The map layer to be filled.
     */
    private void fillRooms(TiledMapTileLayer tiledMapTileLayer) {
        // Central room (player's current room) is at (2, 2) on the minimap
        int centerX = MINIMAP_SIZE.x / 2;
        int centerY = MINIMAP_SIZE.y / 2;

        // Loop over the 5x5 minimap grid
        for (int i = 0; i < MINIMAP_SIZE.x; i++) {
            for (int j = 0; j < MINIMAP_SIZE.y; j++) {
                // Calculate the actual room location relative to the player's current room
                int roomX = currentRoomLoc[0] + (i - centerX);
                int roomY = currentRoomLoc[1] + (j - centerY);

                String roomKey = roomX + "_" + roomY;

                // Check if the room exists and get its connections
                List<String> connections;

                try {
                    connections = level.getMap().getRoomConnections(roomKey);
                } catch (IllegalArgumentException e) {
                    connections = null;
                }

                if (connections != null) {
                    // Determine which directions the room has connections
                    String connectionCode = getConnectionCode(connections);

                    // Assign the correct tile based on the connection code
                    int tileIndex = getTileIndexForConnections(connectionCode);
                    tiledMapTileLayer.setCell(i, j, new TiledMapTileLayer.Cell().setTile(new MinimapTile(tileTextures[tileIndex])));
                } else {
                    // If no room exists, set an empty tile
                    tiledMapTileLayer.setCell(i, j, new TiledMapTileLayer.Cell().setTile(new MinimapTile(tileTextures[15]))); // 0000
                }
            }
        }
    }

    /**
     * fills the renderer
     * @param tiledMap tiled map
     * @param tileScale tile scale
     * @return tiled map renderer
     */
    private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
    }

    /**
     * Parses a room key (formatted as "x_y") into integer coordinates.
     *
     * @param roomKey The key of the room.
     * @return An array containing the x and y coordinates of the room.
     */
    private int[] parseRoomKey(String roomKey) {
        String[] parts = roomKey.split("_");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return new int[]{x, y};
    }

    /**
     * Generates a connection code based on which directions have connected rooms.
     *
     * @param connections List of connected rooms in the order: up, right, down, left.
     * @return A string representing the connection code (e.g., "1101" for north, east, and west).
     */
    private String getConnectionCode(List<String> connections) {
        // North connection (up)
        return (connections.get(0).isEmpty() ? "0" : "1") +

                // East connection (right)
                (connections.get(1).isEmpty() ? "0" : "1") +

                // South connection (down)
                (connections.get(2).isEmpty() ? "0" : "1") +

                // West connection (left)
                (connections.get(3).isEmpty() ? "0" : "1");
    }

    /**
     * Gets the tile index from the connection code.
     *
     * @param connectionCode A binary string representing connections (e.g., "1100").
     * @return The tile index corresponding to the connection code.
     */
    private int getTileIndexForConnections(String connectionCode) {
        switch (connectionCode) {
            case "1000": return 0;
            case "1100": return 1;
            case "1010": return 2;
            case "1001": return 3;
            case "1110": return 4;
            case "1101": return 5;
            case "1111": return 6;
            case "0100": return 7;
            case "0110": return 8;
            case "0111": return 9;
            case "0101": return 10;
            case "0010": return 11;
            case "0011": return 12;
            case "0001": return 13;
            case "1011": return 14;
            case "1000_gambling": return 16;
            case "1100_gambling": return 17;
            case "1010_gambling": return 18;
            case "1001_gambling": return 19;
            case "1110_gambling": return 20;
            case "1101_gambling": return 21;
            case "1111_gambling": return 22;
            case "0100_gambling": return 23;
            case "0110_gambling": return 24;
            case "0111_gambling": return 25;
            case "0101_gambling": return 26;
            case "0010_gambling": return 27;
            case "0011_gambling": return 28;
            case "0001_gambling": return 29;
            case "1011_gambling": return 30;
            case "1000_boss": return 31;
            case "0100_boss": return 32;
            case "0010_boss": return 33;
            case "0001_boss": return 34;
            default: return 15; // 0000 (no connections)
        }
    }

    /**
     * Updates the minimap when the player changes rooms.
     *
     * @param newRoomKey The key of the new room the player has entered.
     */
    public void updateMinimap(String newRoomKey) {
        this.currentRoomKey = newRoomKey;
        this.currentRoomLoc = parseRoomKey(newRoomKey);
        fillRooms(layer);
    }

}

