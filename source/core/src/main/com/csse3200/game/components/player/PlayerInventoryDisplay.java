package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.ui.UIComponent;


/**
 * A UI component for displaying the player's inventory (items and weapons).
 */
public class PlayerInventoryDisplay extends UIComponent {
    private Table inventoryTable;
    private InventoryComponent inventoryComponent = new InventoryComponent();
    private Label heading;



    @Override
    public void create() {
        super.create();
        addActors();
        updateInventoryUI();
        entity.getEvents().addListener("updateInventory", this::updateInventoryUI);
        inventoryComponent.getInventory().addItem(new MeleeWeapon() {
            @Override
            public void attack() {
                //
            }

            @Override
            public String getName() {
                return "Melee";
            }

            @Override
            public Texture getIcon() {
                return null;
            }
        });
        inventoryComponent.getInventory().addItem(new RangedWeapon() {
            @Override
            public void shoot(Vector2 direction) {
                //
            }

            @Override
            public String getName() {
                return "ranged";
            }

            @Override
            public Texture getIcon() {
                return null;
            }
        });
        updateInventoryUI();

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
        //inventoryTable.row();
        addItems();


        // Initial UI update

    }

    void setHeading() {
        CharSequence headingText = "Collected: ";
        heading = new Label(headingText, skin, "large");
        inventoryTable.add(heading);
        stage.addActor(inventoryTable);
        inventoryTable.row();
    }

    void addItems() {
        for (Collectible item : inventoryComponent.getInventory().getItems()) {

            System.out.println(item.getName());
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
        heading.remove();
    }
}