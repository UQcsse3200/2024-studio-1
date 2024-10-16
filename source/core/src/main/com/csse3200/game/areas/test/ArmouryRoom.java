package com.csse3200.game.areas.test;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.BaseRoom;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple room with everything needed for manual testing.
 */
public class ArmouryRoom extends BaseRoom {
    private static final Logger log = LoggerFactory.getLogger(ArmouryRoom.class);

    /**
     * Constructs a new BaseRoom with the given parameters.
     */
    public ArmouryRoom(List<String> connections) {
        super(new TerrainFactory(0),
                new CollectibleFactory(),
                connections,
                "0,0,14,10,0,0",
                "test"
        );
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of();
    }

    @Override
    public void checkComplete() {
        // do nothing.
    }

    private ArrayList<GridPoint2> getItemPositions() {
        ArrayList<GridPoint2> positions = new ArrayList<>();
        for (int x = this.minGridPoint.x; x <= this.maxGridPoint.x; x++) {
            for (int y = this.minGridPoint.y; y <= this.maxGridPoint.y; y++) {
                // If pos is on edge of screen
                Set<Integer> verticalEdges = Set.of(this.minGridPoint.x, this.maxGridPoint.x);
                Set<Integer> horizontalEdges = Set.of(this.minGridPoint.y, this.maxGridPoint.y);

                if (verticalEdges.contains(x) || horizontalEdges.contains(y)) {
                    positions.add(new GridPoint2(x, y));
                }
            }
        }

        return positions;
    }

    @Override
    public void spawn(Entity player, GameArea area) {
        super.spawn(player, area);
        ArrayList<GridPoint2> positions = getItemPositions();

        for (String spec : collectibleFactory.getAllSpecs()) {
            if (positions.isEmpty()) {
                log.error("not enough room for every item");
                return;
            }
            this.spawnItem(area, spec, positions.removeFirst());
        }
    }
}
