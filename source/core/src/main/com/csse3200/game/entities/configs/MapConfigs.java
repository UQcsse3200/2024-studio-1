package com.csse3200.game.entities.configs;

import java.util.List;
import java.util.Map;

public class MapConfigs {
    public Map<String, List<String>> room_connections;
    public Map<String, RoomInfo> room_info;
    public String player_location;
    public long seed;
    public int map_size;

    public static class RoomInfo {
        public Integer animal_index;
        public Integer item_index;
    }
}
