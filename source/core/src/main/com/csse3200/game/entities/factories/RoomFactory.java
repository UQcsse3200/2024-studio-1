package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.RoomDirection;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

/**
 * Factory to create room entities.
 *
 * <p>Each room entity type should have a creation method that returns a corresponding entity.
 */
public class RoomFactory {

    /**
     * Constructs room
     * @param isInside should it be drawn on map
     * @return room
     */
    public static Room createRoom(boolean isInside) {
        Room room = (Room) new Room(isInside)
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.ROOM)); // TODO: correct physics for ROOM
        return room;
    }

    /**
     * Adds connections to other rooms from this room
     * If in some direction you don't want to connect a room, just pass null
     *
     * @param mainRoom main room
     * @param north room in north
     * @param south room in south
     * @param east room in east
     * @param west room in west
     * @return connected room
     */
    public static Room createConnections(Room mainRoom, Room north, Room south, Room east, Room west) {
        if (east != null) mainRoom.connectRoom(RoomDirection.EAST, east);
        if (south != null) mainRoom.connectRoom(RoomDirection.SOUTH, south);
        if (west != null) mainRoom.connectRoom(RoomDirection.WEST, west);
        if (north != null) mainRoom.connectRoom(RoomDirection.NORTH, north);
        return mainRoom;
    }

    private RoomFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}