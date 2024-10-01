
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

public class ShopRoom extends BaseRoom {
    public RandomNumberGenerator rng;

    public ShopRoom(NPCFactory npcFactory, CollectibleFactory collectibleFactory, TerrainFactory terrainFactory,
            List<String> roomConnections, String specification, String roomName) {
                super(terrainFactory, collectibleFactory, roomConnections, specification, roomName);
                this.rng = new RandomNumberGenerator(this.getClass().toString());
    }

    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of(
                List.of("item:medkit:buyable", "buff:heart:buyable"),
                List.of("item:medkit:buyable", "buff:heart:buyable", "item:shieldpotion:buyable"),
                List.of("item:medkit:buyable", "buff:heart:buyable","item:shieldpotion:buyable","buff:energydrink:High")
        );
    }

    @Override
    public void spawn(Entity player, MainGameArea area) {
        super.spawn(player, area);
        spawnItems(area);
        ShopRoomDisplay messageDisplay = new ShopRoomDisplay();
        messageDisplay.create();
        setIsRoomComplete();
    }

    private void spawnItems(MainGameArea area) {
        List<String> itemGroup = this.itemSpecifications.get(this.itemGroup);
        Set<String> usedCoordinates = new HashSet<>();
        final int MAXGENERATED = 10;

        String coordinate;
        if (itemGroup != null) {
            while (usedCoordinates.size() < itemGroup.size() && itemGroup.size() < MAXGENERATED) {
                int x = rng.getRandomInt(1, 8);
                int y = rng.getRandomInt(1, 8);
                coordinate = x + "_" + y;
                if (!usedCoordinates.contains(coordinate)) {
                    spawnItem(area, itemGroup.get(usedCoordinates.size()), new GridPoint2(x, y));
                    usedCoordinates.add(coordinate);
                }
            }

        }
    }


    public void checkIfRoomComplete() {
        setIsRoomComplete();
    }

}
