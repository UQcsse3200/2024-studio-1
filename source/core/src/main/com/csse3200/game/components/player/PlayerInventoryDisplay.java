package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.CombatStatsComponent;
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
    private ShapeRenderer shapeRenderer;
    private int start_pos;
    private Map<String, Label> itemLabels;  // To store labels for each item for easy updating
    private Map<String, Image> itemIcons;   // To store images for each item for easy updating

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
        itemIcons = new HashMap<>();
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
        inventoryTable.bottom();
        inventoryTable.setFillParent(true);
        //inventoryTable.align(Align.bottom);
        //inventoryTable.padTop(50f).padLeft(5f);
        setHeading();
        addItems();
    }

    /**
     * Sets and displays the heading text for the inventory list.
     * The heading is displayed at the top of inventory and provides a title
     * for the items listed below.
     */
    void setHeading() {
        //CharSequence headingText = "Collected:";
        //heading = new Label(headingText, skin, "small");
        //inventoryTable.add(heading);
        stage.addActor(inventoryTable);
        //inventoryTable.row();
    }

    /**
     * Helper method to adds and str all the 'Usable Items' a player can collect
     * to player's inventory
     */
    private void addItems() {
        addItem("Medkit", new MedKit().getIcon());
        addItem("Shield", new ShieldPotion().getIcon());
        addItem("Bandage", new Bandage().getIcon());
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
        Label nameLabel = new Label(itemName, skin, "small");
        nameLabel.setColor(Color.WHITE);
        Label quantityLabel = new Label(" x0", skin, "small");
        quantityLabel.setColor(Color.WHITE);


        inventoryTable.add(icon).bottom().left();
        inventoryTable.add(nameLabel).left();
        inventoryTable.add(quantityLabel).left().padRight(60f);
        // inventoryTable.row();

        itemIcons.put(itemName, icon);
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
        for (Collectible item : inventoryComponent.getInventory().getItems()) {
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
                quantityLabel.setColor(Color.WHITE);
            }
        }
    }


    @Override
    public void draw(SpriteBatch batch) {

        batch.end();

        // temporarily estimated the dimensions for the box through trial and error
        float x = 500f;
        float y = 17f;
        float width = 1000f;
        float height = 55f;

        // Draw the white background box just behind the inventory items
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rect(x , y,  width, height );
        shapeRenderer.end();

        batch.begin();


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
