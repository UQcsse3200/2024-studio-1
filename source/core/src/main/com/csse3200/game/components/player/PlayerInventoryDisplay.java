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
    private final InventoryComponent inventoryComponent;
    private Label heading;

    /**
     * Constructor to initialise the inventory component of Player that needs to be displayed
     *
     * @param inventoryComponent containing all the items to display
     */
    public PlayerInventoryDisplay(InventoryComponent inventoryComponent) {
        this.inventoryComponent = inventoryComponent;
    }


    /**
     * Initialise UI component by creating necessary UI elements and setting up
     * event listeners
     */
    @Override
    public void create() {
        super.create();
        addActors();
        updateInventoryUI();

        if (entity.getEvents() != null) {
            entity.getEvents().addListener("updateInventory", this::updateInventoryUI);
        }

    }

    /**
     * Displays Inventory heading and all the items and position them on UI
     * This will set up the initial layout of the inventory UI.
     *
     * The {@link Table} is used to manage the layout
     */
    private void addActors() {
        inventoryTable = new Table();
        inventoryTable.bottom().left();
        inventoryTable.setFillParent(true);
        inventoryTable.padTop(50f).padLeft(5f);
        setHeading();
        addItems();

    }

    /**
     * Sets and displays the heading text for the inventory list.
     * The heading is displayed at the top of inventory and provides a title
     * for the items listed below.
     */
    void setHeading() {
        CharSequence headingText = "Collected: ";
        heading = new Label(headingText, skin, "large");
        inventoryTable.add(heading);
        stage.addActor(inventoryTable);
        inventoryTable.row();
    }

    /**
     * Takes all items stored in player's inventory and displays it one by one
     * on UI, displaying one on each line
     */
    void addItems() {
        for (Collectible item : inventoryComponent.getInventory().getItems()) {

            CharSequence itemText = item.getName();
            Label itemLabel = new Label(itemText, skin, "large");
            inventoryTable.add(itemLabel).pad(5f);
            // move to next line
            inventoryTable.row();
        }
    }


    /**
     * Updates the inventory UI with the current items that player has.
     * This method will clear the existing inventory and repopulate it.
     */
    public void updateInventoryUI() {
        inventoryTable.clear();
        setHeading();
        addItems();

    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }


    /**
     * Disposes off UI component when they are no longer needed by removing
     * the heading and inventory table to free up space
     */
    @Override
    public void dispose() {
        super.dispose();
        if (heading != null) {
            heading.remove();
        }
        if (inventoryTable != null) {
            inventoryTable.remove();
        }
    }

}