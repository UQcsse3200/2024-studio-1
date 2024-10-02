package com.csse3200.game.services;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.DialogComponent;

/** Service for displaying alert boxes in the game. */
public class AlertBoxService {

    private final Stage stage;
    private final Skin skin;

    public AlertBoxService(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

    /**
     * Displays an alert box with the specified message.
     *
     * @param title   Title of the alert box.
     * @param message Message to display.
     */
    public void showAlert(String title, String message) {
        Dialog dialog = new Dialog(title, skin);
        dialog.text(message);
        dialog.button("OK");
        dialog.show(stage);
    }

    public void confirmDialogBox(Entity entity, String text, ConfirmationListener listener) {
        entity.getComponent(DialogComponent.class).showDialog(text);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    if(entity.getComponent(DialogComponent.class).dismissDialog())
                        listener.onYes();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Displays a confirmation dialog with Yes and No buttons.
     *
     * @param title     Title of the dialog.
     * @param message   Message to display.
     * @param listener  Listener to handle Yes or No response.
     */
    public void showConfirmationDialog(String title, String message, ConfirmationListener listener) {
        Dialog dialog = new Dialog(title, skin) {
            @Override
            protected void result(Object object) {
                boolean confirmed = (Boolean) object;
                if (confirmed) {
                    listener.onYes();
                } else {
                    listener.onNo();
                }
            }
        };

        dialog.text(message);
        dialog.button("Yes", true);  // "Yes" button returns true
        dialog.button("No", false);  // "No" button returns false
        dialog.show(stage);
    }

    // confirmDialog method
    public void confirmDialog(String title, String message, ConfirmationListener listener) {
        Dialog dialog = new Dialog(title, skin) {
            @Override
            protected void result(Object object) {
                boolean confirmed = (Boolean) object;
                if (confirmed) {
                    listener.onYes();
                } else {
                    listener.onNo();
                }
            }

            @Override
            public Dialog show(Stage stage) {
                super.show(stage);

                // Add a key listener for the Enter key
                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if (keycode == Input.Keys.ENTER) {
                            result(true);  // Trigger "Confirm"
                            hide();        // Close the dialog
                            return true;
                        }
                        return false;
                    }
                });

                return this;
            }
        };

        dialog.text(message);
        dialog.button("Confirm", true);  // "Confirm" button returns true
        dialog.show(stage);
    }

    /**
     * Listener interface for handling Yes/No responses.
     */
    public interface ConfirmationListener {
        void onYes();
        void onNo();
    }
}
