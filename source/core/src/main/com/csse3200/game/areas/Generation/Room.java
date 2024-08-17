package com.csse3200.game.areas.generation;


import com.csse3200.game.areas.RandomNumberGenerator;
import com.csse3200.game.areas.generation.MapGenerator;
import java.util.Random;


class Room {
    private RandomNumberGenerator rng;
    private MapGenerator map; // Use MapGenerator instead of Map
    private int mapSize; // Size of branches after this point the map chances get increasingly less
    private Room[] connections; // [n, s, e, w] assume this is another Room class, if there is not a Room there = 0 
    private int animalIndex; // The index of animal array groups 
    private int itemIndex; // Index of item arrays 
    private int branchCount; // Corrected variable name for map info
    private int[] relativeLocation; // Location of the room in a grid x, y format


    // 
    public Room(RandomNumberGenerator rng, MapGenerator map, int mapSize, Room[] connections, int Branch_Count, int[] Relative_Location) {
        this.rng = rng;
        this.map = map;
        this.mapSize = mapSize;
        this.connections = connections;
        this.branchCount = branchCount;
        this.relativeLocation = relativeLocation;



        // Generate random indices for animal and item
        this.animalIndex = rng.getRandomInt(0, 12);

        // will have duds in the array for the circumstance of not all rooms having items
        this.itemIndex = rng.getRandomInt(0, 12);
    }

    // Getter methods
    public int getAnimalIndex() {
        return this.animalIndex;
    }

    public int getItemIndex() {
        return this.itemIndex;
    }

    public Room[] getConnections() {
        return this.connections;
    }

    public int getBranchCount() {
        return this.branchCount;
    }

    public int[] getRelativeLocation() {
        return this.relativeLocation;
    }

    public MapGenerator getMap() {
        return this.map;
    }

    public int getMapSize() {
        return this.mapSize;
    }

    public void createConnections() {
        
    }
}