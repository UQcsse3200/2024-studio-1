package com.csse3200.game.entities.factories;

import java.util.List;


import com.csse3200.game.areas.BossRoom;
import com.csse3200.game.areas.MainRoom;
import com.csse3200.game.areas.Room;
import com.csse3200.game.areas.ShopRoom;
import com.csse3200.game.areas.GambleRoom;

import com.csse3200.game.areas.terrain.TerrainFactory;

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


    public Room createShopRoom(List<String> roomConnections, String specification, String roomName,
                               List<String> shopItemList) {
        // add connections to boss Room
        return new ShopRoom(this.npcFactory, this.collectibleFactory,
                            this.terrainFactory, roomConnections, specification, roomName, shopItemList);
    }


    public Room createGambleRoom(List<String> roomConnections, String specification, String roomName) {
        // add connections to boss Room
        return new GambleRoom(this.npcFactory, this.collectibleFactory,
                            this.terrainFactory, roomConnections, specification, roomName);
    }



}