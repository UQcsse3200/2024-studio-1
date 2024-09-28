package com.csse3200.game.entities.factories;

import java.util.List;

import com.csse3200.game.areas.Rooms.BossRoom;
import com.csse3200.game.areas.Rooms.MainRoom;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Room;

/**
 * Factory to create room entities.
 * <p>
 * Each room entity type should have a creation method that returns a corresponding entity.
 */
public class RoomFactory {
    private final NPCFactory npcFactory;
    private final CollectibleFactory collectibleFactory;
    private final TerrainFactory terrainFactory;

    /**
     * Construct a new  Room Factory
     * @param npcFactory the npc factory to populate the room with.
     * @param collectibleFactory the collectible factory to populate the room with.
     * @param terrainFactory the terrain factory to re-skin the room with.
     */
    public RoomFactory(NPCFactory npcFactory,
                       CollectibleFactory collectibleFactory,
                       TerrainFactory terrainFactory) {
        this.npcFactory = npcFactory;
        this.collectibleFactory = collectibleFactory;
        this.terrainFactory = terrainFactory;
    }

    /**
     * Constructs room
     * @return room
     */
    public Room createRoom(List<String> roomConnections, String specification, String roomName) {
        return new MainRoom(this.npcFactory, this.collectibleFactory, 
                             this.terrainFactory, roomConnections, specification, roomName);
    }

    /**
     * Constructs boss room
     * @return room
     */
    public Room createBossRoom(List<String> roomConnections, String specification, String roomName) {
        // add connections to boss Room
        return new BossRoom(this.npcFactory, this.collectibleFactory,
                            this.terrainFactory, roomConnections, specification, roomName);
    }

//    /**
//     * Adds connections to other rooms from this room
//     * If in some direction you don't want to connect a room, just pass null
//     *
//     * @param mainRoom main room
//     * @param north room in north
//     * @param south room in south
//     * @param east room in east
//     * @param west room in west
//     * @return connected room
//     */
//    public static Room createConnections(Room mainRoom, Room north, Room south, Room east, Room west) {
//        if (east != null) mainRoom.connectRoom(RoomDirection.EAST, east);
//        if (south != null) mainRoom.connectRoom(RoomDirection.SOUTH, south);
//        if (west != null) mainRoom.connectRoom(RoomDirection.WEST, west);
//        if (north != null) mainRoom.connectRoom(RoomDirection.NORTH, north);
//        return mainRoom;
//    }
}