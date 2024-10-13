package com.csse3200.game.areas;

import com.csse3200.game.areas.generation.MapGenerator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.configs.MapLoadConfig;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.RoomFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
/**
 * This is the main game mode.
 */
public class MainGameLevelFactory implements LevelFactory {
    private static final int DEFAULT_MAP_SIZE = 40;
    private static final Logger log = LoggerFactory.getLogger(MainGameLevelFactory.class);
    private int levelNum;
    private final Map<String, Room> rooms;
    private LevelMap map;
    private boolean shouldLoad;
    private List<String> loadedRooms;
    private MapLoadConfig config;


    public MainGameLevelFactory(boolean shouldLoad, MapLoadConfig config) {
        this.shouldLoad = shouldLoad;
        this.config = config;
        this.rooms = new HashMap<>();
        if (!shouldLoad) this.loadedRooms = new ArrayList<>();
        else this.loadedRooms = config.roomsCompleted;
    }

    /**
     * List of all the items the game contains as buuyables. Will be used to randomly pick 6 items that will
     * be spawned in the shop room.
     * @return List of items specifications.
     */
    protected List<String> getShopRoomItems() {
        return List.of("buff:heart:buyable","item:medkit:buyable", "item:shieldpotion:buyable",
                        "item:bandage:buyable", "buff:energydrink:Low:buyable", "buff:energydrink:Low:buyable",
                        "buff:syringe:buyable", "buff:armor:buyable", "buff:damagebuff:buyable",
                        "item:beartrap:buyable", "item:targetdummy:buyable", "item:reroll:buyable",
                        "buff:feather:buyable", "item:heart:buyable", "buff:divinepotion:buyable"
        );
    }

    /**
     * Takes the list of all buyable items and makes a random list of 6 items to be spawned on the shop floor
     * @return List of 6 items.
     */
    private List<String> createShopItemsList() {
        List<String> items = getShopRoomItems();
        List<String> itemsToSpawn = new ArrayList<>();
        //Zack's code: spawn in 1 line (if there is 6 item)
        if(items != null) {
            for (int i = 0; i < 6; i++){
                if (ServiceLocator.getRandomService() != null) {
                    int itemIndex = ServiceLocator.getRandomService().getRandomNumberGenerator(getClass()).getRandomInt(0, 14);
                    itemsToSpawn.add(items.get(itemIndex));
                }
            }
        }
        return itemsToSpawn;
    }

    @Override
    public Level create(int levelNumber) {
        String seed = "seed";
        // default seed for junit tests
        if (!shouldLoad) {
            map = new LevelMap(seed + levelNumber, DEFAULT_MAP_SIZE);

        } else {
            // For loaded games, append the level number to the loaded seed
            map = new LevelMap(config.seed + config.currentLevel, config.mapSize);
        }

        RoomFactory roomFactory = new RoomFactory(
                new NPCFactory(),
                new CollectibleFactory(),
                new TerrainFactory(levelNumber)
        );

        Set<String> roomKeySet = map.mapData.getPositions().keySet();
        for (String roomKey : roomKeySet) {
            int itemIndex = map.mapData.getRoomDetails().get(roomKey).get("item_index");
            int animalIndex = map.mapData.getRoomDetails().get(roomKey).get("animal_index");
            int roomType = map.mapData.getRoomDetails().get(roomKey).get("room_type");
            switch (roomType) {
                case MapGenerator.BOSSROOM:
                    rooms.put(roomKey, roomFactory.createBossRoom(
                            map.mapData.getPositions().get(roomKey),
                            "0,0,14,10," + levelNumber + "," + levelNumber, roomKey));
                    break;
                case MapGenerator.NPCROOM:
                    //If the game is loaded the items to be spawned is loaded from the config
                    //if not uses the createShopItems method to create a random list which is then loaded into the shop
                    // room to be spawned.
                    List<String> itemsToBeSpawned = new ArrayList<>();
                    if (shouldLoad) {
                        itemsToBeSpawned = config.shopRoomItems;
                    } else {
                        itemsToBeSpawned = createShopItemsList();
                    }
                    ShopRoom shop = (ShopRoom) roomFactory.createShopRoom(
                            map.mapData.getPositions().get(roomKey),
                            "0,0,14,10," + 0 + "," + levelNumber, roomKey, itemsToBeSpawned);
                    rooms.put(roomKey, shop);
                    break;
                case MapGenerator.GAMEROOM:
                    rooms.put(roomKey, roomFactory.createGambleRoom(
                            map.mapData.getPositions().get(roomKey),
                            "0,0,14,10," + levelNumber + "," + levelNumber, roomKey));
                    break;
                default:
                    rooms.put(roomKey, roomFactory.createRoom(
                            map.mapData.getPositions().get(roomKey),
                            "0,0,14,10," + animalIndex + "," + itemIndex, roomKey));
                    break;
            }
        }

        if (shouldLoad) {
            setRoomsComplete(loadedRooms);
            shouldLoad = false;
        }
        // Store the current level number
        this.levelNum = levelNumber;

        return new Level(map, levelNumber, rooms);
    }


    /**
     * Exports the map data to a JSON file.
     *
     * @param filePath The path of the file to write the JSON data to.
     */
    public void saveMapData(String filePath, String level) {
        List<String> compRooms = new ArrayList<String>();
        List<String> items = new ArrayList<String>();
        MapLoadConfig config = new MapLoadConfig();
        String gameSeed = map.mapData.getMapSeed();
        String seedOnly = gameSeed.substring(0, gameSeed.length() - 1);
        config.seed = seedOnly;
        config.currentLevel = level; 
        config.currentRoom = ServiceLocator.getGameAreaService().getGameController().getCurrentRoom().getRoomName();

        for (Room room : rooms.values()) {
            if (room.getIsRoomComplete()){
                if(map.mapData.getRoomDetails().get(room.getRoomName()) != null) {
                    if(map.mapData.getRoomDetails().get(room.getRoomName()).get("room_type") != 1) {
                        compRooms.add(room.getRoomName());
                    }
                }
            }
        }
        ShopRoom shopRoom = (ShopRoom)rooms.get(ServiceLocator.getGameAreaService().getGameController().getFlaggedRoom("NPC"));
        List<String> shopSave = shopRoom.itemsSpawned;
        config.shopRoomItems.addAll(shopSave);
        config.roomsCompleted = compRooms;
        config.mapSize = map.getMapSize();
        FileLoader.writeClass(config, filePath, FileLoader.Location.EXTERNAL);
    }

    /**
     * Sets the rooms that have been completed in the saved game as completed in the loaded
     * game.
     * @param roomNames Room keys that have been completed.
     */

    public void setRoomsComplete(List<String> roomNames) {
        for (String roomName : roomNames) {
            rooms.get(roomName).setRoomComplete();
        }
    }

    public int getCurrentLevel() {
        return levelNum;
    }


}
