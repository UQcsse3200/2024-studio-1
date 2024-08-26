package com.csse3200.game.entities;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** room entity */
public class Room extends Entity {

    private final Map<RoomDirection, Room> connections;
    private final Set<Entity> items;
    private boolean isInside;
    private GridPoint2 roomPos;

    /**
     * Constructs room
     */
    public Room() {
        this(false);
    }

    /**
     * Constructs room
     * @param isInside true if this room is on the map, false if the room is not visible
     */
    public Room(boolean isInside) {
        this.connections = new HashMap<>();
        this.items = new HashSet<>();
        this.isInside = isInside;
    }

    public void connectRoom(RoomDirection direction, Room connectedRoom) {
        if (connections.get(direction) != connectedRoom) {
            connections.put(direction, connectedRoom);
            connectedRoom.connectRoom(direction.getOppositeDirection(), this);
        }
    }

    public Room getConnectedRoom(RoomDirection roomDirection) {
        return connections.get(roomDirection);
    }

    public void addItem(Entity item) {
        items.add(item);
    }

    public Set<Entity> getItems() {
        return items;
    }

    /** to trigger entry */
    public void enter() {
        this.isInside = true;
    }

    /** to trigger exit */
    public void exit() {
        this.isInside = false;
    }

    public boolean isInside() {
        return isInside;
    }

    public void setRoomPosition(GridPoint2 roomPos) {
        this.roomPos = roomPos;
    }

    public GridPoint2 getRoomPos() {
        return roomPos;
    }

}
