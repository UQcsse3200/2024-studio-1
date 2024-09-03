package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.ui.UIComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * A UI component for displaying the player's inventory (items and weapons).
 */
public class PlayerInventoryDisplay extends UIComponent {
    private Table inventoryTable;
    private final InventoryComponent inventoryComponent;
    private Label heading;
    private Map<String, Label> itemLabels;  // To store labels for each item for easy updating
    private Map<String, Image> itemIcons;   // To store images for each item for easy updating

    /**
     * Constructor to initialise the inventory component of Player that needs to be displayed
     *
     * @param inventoryComponent containing all the items to display
     */
    public PlayerInventoryDisplay(InventoryComponent inventoryComponent) {
        this.inventoryComponent = inventoryComponent;
        for (Collectible inventoryComponent1 : inventoryComponent.getInventory().getItems()) {
            System.out.println("CURRENTLY HAS THIS INVENTORY\n"+inventoryComponent1);
        }
    }

    /**
     * Initialise UI component by creating necessary UI elements and setting up
     * event listeners
     */
    @Override
    public void create() {
        super.create();
        itemLabels = new HashMap<>();
        itemIcons = new HashMap<>();
        addActors();

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
        CharSequence headingText = "Collected:";
        heading = new Label(headingText, skin, "small");
        inventoryTable.add(heading);
        stage.addActor(inventoryTable);
        inventoryTable.row();
    }

    /**
     * Takes all items stored in player's inventory and displays them with
     * their amount and icon on UI, displaying one on each line.
     */
    void addItems() {
        // Display all items with their initial quantities (0)
        addItem("Medkit", new MedKit().getIcon());
        addItem("Shield Potion", new ShieldPotion().getIcon());
        addItem("Bandage", new Bandage().getIcon());

    }

    /**
     * Adds an item to the inventory UI with an icon and initial quantity.
     * @param itemName The name of the item.
     * @param itemIcon The icon associated with the item.
     */
    private void addItem(String itemName, Texture itemIcon) {
        Image icon = new Image(itemIcon);
        Label quantityLabel = new Label(" x0", skin, "small");
        Label nameLabel = new Label(itemName, skin, "small");

        inventoryTable.add(icon).bottom().left();
        inventoryTable.add(nameLabel).left();
        inventoryTable.add(quantityLabel).left();
        inventoryTable.row();

        itemIcons.put(itemName, icon);
        itemLabels.put(itemName, quantityLabel);
    }

    /**
     * Updates the inventory UI with the current items that player has.
     * This method will update the quantities of existing items.
     */
    public void updateInventoryUI() {

        // Update with actual quantities from inventory
        Map<String, Integer> itemQuantities = new HashMap<>();
        for (Collectible item : inventoryComponent.getInventory().getItems()) {
            String itemName = item.getName();
            itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + 1);
        }

        // Update the displayed quantities
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            Label quantityLabel = itemLabels.get(entry.getKey());
            if (quantityLabel != null) {
                quantityLabel.setText(" x" + entry.getValue());
            }
        }
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
