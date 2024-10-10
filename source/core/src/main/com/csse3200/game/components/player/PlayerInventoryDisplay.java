package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.components.player.inventory.UsableItem;
import com.csse3200.game.components.player.inventory.usables.*;
import com.csse3200.game.ui.UIComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * A UI component for displaying the player's inventory (items and weapons).
 */
public class PlayerInventoryDisplay extends UIComponent {
    /**
     * Table for laying out inventory items in the UI.
     */
    private Table inventoryTable;

    /**
     * The inventory component of the player, which contains all the items
     * to be displayed.
     */
    private final InventoryComponent inventoryComponent;

    /**
     * Renderer used to draw shapes (in this case, the background of the inventory).
     */
    private final ShapeRenderer shapeRenderer;

    /**
     * Starting x-position for the background box drawn behind the inventory items.
     */
    private static final float START_X = 0f;

    /**
     * Starting y-position for the background box drawn behind the inventory items.
     */
    private static final float START_Y = 15f;

    /**
     * Width of the background box, dynamically set to the width of the screen.
     */
    private static final float WIDTH = Gdx.graphics.getWidth();

    /**
     * Height of the background box drawn behind the inventory items.
     * Estimated through trial and error
     */
    private static final float HEIGHT = 55f;

    /**
     * Map for storing labels associated with each item for easy access and
     * updating the quantity. Estimated through trial and error.
     */
    private Map<String, Label> itemLabels;  // To store labels for each item for easy updating


    /**
     * Constructor to initialise the inventory component of Player that needs to be displayed
     *
     * @param inventoryComponent containing all the items to display
     */
    public PlayerInventoryDisplay(InventoryComponent inventoryComponent) {
        this.inventoryComponent = inventoryComponent;
        shapeRenderer = new ShapeRenderer();
    }

    /**
     * Initialise UI component by creating necessary UI elements and setting up
     * event listeners
     */
    @Override
    public void create() {
        super.create();
        itemLabels = new HashMap<>();
        //itemIcons = new HashMap<>();
        addActors();
        updateInventoryUI();
        if (entity.getEvents() != null) {
            entity.getEvents().addListener("updateInventory", this::updateInventoryUI);
        }
    }

    /**
     * Displays Inventory heading and all the items and position them on UI
     * This will set up the initial layout of the inventory UI.
     * <p>
     * The {@link Table} is used to manage the layout
     */
    private void addActors() {
        inventoryTable = new Table();
        inventoryTable.bottom();
        inventoryTable.setFillParent(true);
        stage.addActor(inventoryTable);
        addItems();
    }

    /**
     * Helper method to adds and str all the 'Usable Items' a player can collect
     * to player's inventory
     */
    private void addItems() {
        for (UsableItem item : inventoryComponent.getItems()) {
            System.out.println(item.getName());
            addItem(item.getName(), item.getIcon());
        }
    }

    /**
     * Display an image and its description on UI
     *
     * @param itemName The name of the item.
     * @param itemIcon The icon associated with the item.
     */
    private void addItem(String itemName, Texture itemIcon) {
        // initialise the image, the name and the quantity as Label
        Image icon = new Image(itemIcon);
        Label quantityLabel = new Label(" x0", skin, "small");
        quantityLabel.setColor(Color.WHITE);

        inventoryTable.add(icon).bottom().left();
        inventoryTable.add(quantityLabel).left().padRight(60f);

        itemLabels.put(itemName, quantityLabel);
    }

    /**
     * Get the quantities of items in the inventory.
     *
     * @return A map of item names to their quantities.
     */
    private Map<String, Integer> getItemQuantities() {
        // initialise all items with 0 quantity
        Map<String, Integer> itemQuantities = new HashMap<>();
        for (String itemName : itemLabels.keySet()) {
            itemQuantities.put(itemName, 0);
        }
        // store the amount of each item to update the count later
        for (Collectible item : inventoryComponent.getItems()) {
            String itemName = item.getName();
            itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + 1);
        }
        return itemQuantities;
    }


    /**
     * Updates the inventory UI with the current items that player has.
     * This method will update the quantities of existing items.
     */
    public void updateInventoryUI() {

        Map<String, Integer> itemQuantities = getItemQuantities();

        // Update the displayed quantities
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            Label quantityLabel = itemLabels.get(entry.getKey());
            if (quantityLabel != null) {
                int quantity = entry.getValue();
                quantityLabel.setText(" x" + quantity);
            }
        }
    }

    /**
     * Draws white background behind inventory
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        /*
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rect(START_X, START_Y, WIDTH, HEIGHT);
        shapeRenderer.end();
        batch.begin();

         */
    }

    /**
     * Disposes off UI component when they are no longer needed by removing
     * the heading and inventory table to free up space
     */
    @Override
    public void dispose() {
        super.dispose();

        if (inventoryTable != null) {
            inventoryTable.remove();
        }
    }
}
