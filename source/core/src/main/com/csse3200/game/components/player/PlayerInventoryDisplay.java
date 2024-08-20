package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.ui.UIComponent;


/**
 * A UI component for displaying the player's inventory (items and weapons).
 */
public class PlayerInventoryDisplay extends UIComponent {
    private Table inventoryTable;
    private InventoryComponent inventoryComponent;
    private Label heading;

    @Override
    public void create() {
        super.create();
        inventoryComponent = entity.getComponent(InventoryComponent.class); // Ensure the inventory is properly fetched
        addActors();
        entity.getEvents().addListener("updateInventory", this::updateInventoryUI);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * This will set up the initial layout of the inventory UI.
     */
    private void addActors() {
        inventoryTable = new Table();
        inventoryTable.bottom().left();
        inventoryTable.setFillParent(true);
        inventoryTable.padTop(50f).padLeft(5f);
        setHeading();


        // Initial UI update

    }

    void setHeading() {
        CharSequence headingText = "Collected";
        heading = new Label(headingText, skin, "large");
        inventoryTable.add(heading);
        stage.addActor(inventoryTable);
    }


    /**
     * Updates the inventory UI with the current items and weapons the player has.
     * This method will clear the existing inventory and repopulate it.
     */
    public void updateInventoryUI() {

    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
        heading.remove();
    }
}