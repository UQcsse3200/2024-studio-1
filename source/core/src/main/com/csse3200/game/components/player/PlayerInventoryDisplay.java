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
     * Creates reusable ui styles and adds actors to the stage.
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
     * Creates actors and positions them on the stage using a table.
     * This will set up the initial layout of the inventory UI.
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
     * Sets and Displays the heading text of inventory i.e. "Collected: "
     */
    void setHeading() {
        CharSequence headingText = "Collected: ";
        heading = new Label(headingText, skin, "large");
        inventoryTable.add(heading);
        stage.addActor(inventoryTable);
        inventoryTable.row();
    }

    void addItems() {
        for (Collectible item : inventoryComponent.getInventory().getItems()) {

            CharSequence itemText = item.getName();
            Label itemLabel = new Label(itemText, skin, "large");
            inventoryTable.add(itemLabel).pad(5f);
            inventoryTable.row();
        }
    }


    /**
     * Updates the inventory UI with the current items and weapons the player has.
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

    public Table getInventoryTable() {
        return inventoryTable;
    }
}