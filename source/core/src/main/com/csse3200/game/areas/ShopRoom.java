
package com.csse3200.game.areas;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.ShopRoomDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.RandomNumberGenerator;

/**
 * Room for Player to Shop in to buy 'buyable' items within
 */
public class ShopRoom extends BaseRoom {
    public RandomNumberGenerator rng;
    public List<String> itemsSpawned;
    /**
     * A shop room (NPC Room) that users can buy items in
     * @param npcFactory         the NPC factory to use
     * @param collectibleFactory the Collectible factory to use.
     * @param terrainFactory     the terrain factory to use.
     * @param roomConnections    the keys for all the adjacent rooms.
     * @param specification      the specification for this room.
     */
    public ShopRoom(NPCFactory npcFactory, CollectibleFactory collectibleFactory, TerrainFactory terrainFactory,
            List<String> roomConnections, String specification, String roomName, List<String> shopItems) {
                super(terrainFactory, collectibleFactory, roomConnections, specification, roomName);
                this.rng = new RandomNumberGenerator(this.getClass().toString());
                this.itemsSpawned = shopItems;

    }

    /**
     * Item specifications for Shop Rooms based on levels
     * @return - List of lists containing possible items
     */
    @Override
    protected List<List<String>> getItemSpecifications(){
        if(itemsSpawned != null) {
            return List.of(itemsSpawned);
        } return List.of(List.of("buff:heart:buyable"));}

    /**
     * Spawn the area and player with necessary area conditions
     * @param player the player that will play this room.
     * @param area the game area to spawn this room into.
     */
    @Override
    public void spawn(Entity player, MainGameArea area) {
        super.spawn(player, area);
        spawnItems(area);
        ShopRoomDisplay messageDisplay = new ShopRoomDisplay();
        messageDisplay.create();
        checkIfRoomComplete();
    }

    /**
     * Randomly position items on the board for user to purchase
     * @param area the game are to spawn room into
     */
    private void spawnItems(MainGameArea area) {
        List<String> items = this.itemSpecifications.getFirst();
        //Zack's code: spawn in 1 line (if there is 6 item)
        if(items != null) {
            for (int i = 0; i < itemsSpawned.size(); i++){
                spawnItem(area, itemsSpawned.get(i), new GridPoint2(i * 2 + 2, 8));
            }
        }
    }

    public void removeItemFromList(String item) {
        itemsSpawned.remove(item);
    }

    /**
     * Check room is complete and set it as always complete
     */
    public void checkIfRoomComplete() {
        setRoomComplete();
    }
}
