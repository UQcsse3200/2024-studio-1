
package com.csse3200.game.areas;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * A shop room (NPC Room) that users can buy items in
     * @param npcFactory         the NPC factory to use
     * @param collectibleFactory the Collectible factory to use.
     * @param terrainFactory     the terrain factory to use.
     * @param roomConnections    the keys for all the adjacent rooms.
     * @param specification      the specification for this room.
     */
    public ShopRoom(NPCFactory npcFactory, CollectibleFactory collectibleFactory, TerrainFactory terrainFactory,
            List<String> roomConnections, String specification, String roomName) {
                super(terrainFactory, collectibleFactory, roomConnections, specification, roomName);
                this.rng = new RandomNumberGenerator(this.getClass().toString());
    }

    /**
     * Item specifications for Shop Rooms based on levels
     * @return - List of lists containing possible items
     */
    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of(
                List.of("item:medkit:buyable", "buff:heart:buyable","item:medkit:buyable", "buff:heart:buyable","item:medkit:buyable", "buff:heart:buyable"),
                List.of("item:medkit:buyable", "buff:heart:buyable", "item:shieldpotion:buyable","item:medkit:buyable", "buff:heart:buyable", "item:shieldpotion:buyable"),
                List.of("item:medkit:buyable", "buff:heart:buyable","item:shieldpotion:buyable","item:medkit:buyable", "buff:heart:buyable", "item:shieldpotion:buyable")
        );
    }

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
        List<String> items = this.itemSpecifications.get(this.itemGroup);
        //Zack's code: spawn in 1 line (if there is 6 item)
        if(items != null) {
            for (int i = 0; i < items.size(); i++){
                spawnItem(area, items.get(i), new GridPoint2(i * 2 + 2, 8));
            }
        }
    }


    /**
     * Check room is complete and set it as always complete
     */
    public void checkIfRoomComplete() {
        setIsRoomComplete();
    }

}
