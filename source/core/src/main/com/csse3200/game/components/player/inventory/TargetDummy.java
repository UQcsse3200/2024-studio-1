package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DeployableItemFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ServiceLocator;

public class TargetDummy extends UsableItem {
    NPCFactory npcFactory = new NPCFactory();
    @Override
    public void apply(Entity entity) {
        spawnTargetDummy(entity);
    }

    private void spawnTargetDummy(Entity entity) {
        Entity targetDummy = new DeployableItemFactory().createTargetDummy();

        int xPos = (int) entity.getPosition().x;
        int yPos = (int) entity.getPosition().y;

        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(targetDummy, new GridPoint2(xPos, yPos), true, true);
        ServiceLocator.getGameAreaService().getGameArea().updateEnemyTargets(targetDummy);
        // Entity bear = npcFactory.create("Bear", targetDummy);
        //ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(bear, new GridPoint2(5, 8), true, true);
    }

    @Override
    public String getName() {
        return "Target Dummy";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/target_dummy.png");

    }

    @Override
    public String getItemSpecification() {
        return "targetdummy";
    }

}
