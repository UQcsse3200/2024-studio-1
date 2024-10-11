package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Component;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * A class that contains the main attributes of a buyable item, including its cost, and also
 * creates the buyable item's price tag
 */
public class BuyableComponent extends UIComponent {
    /**
     * The cost of this buyable item
     */
    int cost;
    /**
     * The price tag associated with this buyable item
     */
    Label priceTag;

    /**
     * Constructor method for a buyable item
     * @param defaultCost the cost that the buyable item is initially set to. This can be
     *                    later modified using setCost()
     */
    public BuyableComponent(int defaultCost) {
        super();
        this.cost = defaultCost;
    }

    /**
     * Method called upon creation. Also creates the price tag label for this buyable item.
     */
    @Override
    public void create() {
        super.create();

        String text = String.format("$%d", this.getCost());
        this.priceTag = new Label(text, skin, "large");
        Camera camera = ServiceLocator.getRenderService().getCamera().getCamera();

        //Get the position of this buyable item entity (z=0 since the third coordinate is irrelevant)
        Vector3 position = new Vector3(entity.getCenterPosition().x, entity.getCenterPosition().y, 0);
        Vector3 screenPosition = camera.project(position);
        //Set the position of the price tag to be just beneath the buyable item entity
        this.priceTag.setPosition(screenPosition.x, screenPosition.y - 90f);
        this.priceTag.setAlignment(Align.center);
        stage.addActor(this.priceTag);
    }

    /**
     * Removes the label
     */
    public void removeLabel() {
        this.priceTag.remove();
    }

    /**
     * Sets the cost of this buyable item
     * @param cost the new cost to set to
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Gets the cost of this buyable item
     * @return
     */
    public int getCost() {
        return this.cost;
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }
}
