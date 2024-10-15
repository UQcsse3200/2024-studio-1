package com.csse3200.game.areas.generation;

/**
 * The various different kinds of Room this Generator Generates.
 */
public enum RoomType {
    BASE_ROOM(0),
    BOSS_ROOM(1),
    NPC_ROOM(2),
    GAME_ROOM(3);

    public final int num;

    RoomType(int num) {
        this.num = num;
    }
}