package com.csse3200.game.entities.configs;

import java.util.List;
import java.util.Map;

public class MapConfigs extends BaseEntityConfig {
    public Map<String, List<String>> roomConnections;
    public Map<String, RoomInfo> roomInfo;
    public String playerLocation;
    public String seed;
    public int mapSize;

    public static class RoomInfo {
        public Integer animalIndex;
        public Integer itemIndex;
    }
}
