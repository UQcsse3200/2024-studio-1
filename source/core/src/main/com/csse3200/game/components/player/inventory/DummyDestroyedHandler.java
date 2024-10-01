package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

/**
 * A component that handles the destruction of a target dummy entity when the 'dummyDestroyed' event is triggered.
 */
public class DummyDestroyedHandler extends Component {

    /**
     * This initialises the component and registers the event listener for dummy destruction
     */
    public void create() {
        super.create();
        entity.getEvents().addListener("dummyDestroyed", this::handleDummyRemoval);
    }

    /**
     * Handles the removal of the dummy entity
     */
    public void handleDummyRemoval() {
        ServiceLocator.getEntityService().markEntityForRemoval(entity);
    }
}