package com.csse3200.game.entities.factories;

/**
 * Interface to be implemented by callback to be supplied to door entity
 *
 */
public interface DoorCallBack {
    /**
     * When door is collided with callback is activated
     */
    void onDoorCollided();
}