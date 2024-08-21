package com.csse3200.game.entities;

public enum RoomDirection {

    NORTH(1),
    SOUTH(0),
    EAST(3),
    WEST(2);

    private final int oppositeDirection;

    private RoomDirection(int opposite) {
        oppositeDirection = opposite;
    }

    public RoomDirection getOppositeDirection() {
        return RoomDirection.values()[oppositeDirection];
    }
}
