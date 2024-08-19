package com.csse3200.game.entities.factories;

import com.csse3200.game.files.FileLoader;

import java.io.File;

public class MapFactory {
    private static final MapConfigs mapData;

    public static void loadMap (String filePath) {
        mapData = FileLoader.readClass(MapConfig.class, filePath);
        if (mapData == null){
            throw new IllegalStateException("Failed to load mapData from " + filePath);
        }
    }

}
