package com.csse3200.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private Room room;
    private Room connectedRoom;
    private Entity item;

    @BeforeEach
    void setUp() {
        room = new Room();
        connectedRoom = new Room();
        item = new Entity();
    }

    @Test
    void testConnectRoom() {
        room.connectRoom(RoomDirection.NORTH, connectedRoom);

        // Check that the rooms are connected in both directions
        assertEquals(connectedRoom, room.getConnectedRoom(RoomDirection.NORTH));
        assertEquals(room, connectedRoom.getConnectedRoom(RoomDirection.SOUTH));
    }

    @Test
    void testAddItem() {
        room.addItem(item);

        // Check that the item is added to the room
        Set<Entity> items = room.getItems();
        assertTrue(items.contains(item));
        assertEquals(1, items.size());
    }

    @Test
    void testEnterAndExit() {
        // Initially, the room should not be inside
        assertFalse(room.isInside());

        // Enter the room
        room.enter();
        assertTrue(room.isInside());

        // Exit the room
        room.exit();
        assertFalse(room.isInside());
    }

    @Test
    void testSetRoomPosition() {
        GridPoint2 position = new GridPoint2(5, 10);
        room.setRoomPosition(position);

        // Check that the room position is set correctly
        assertEquals(position, room.getRoomPos());
    }
}