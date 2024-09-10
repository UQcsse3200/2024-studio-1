package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.GdxGame;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.ArrayList;

import static com.csse3200.game.GdxGame.ScreenType.MAIN_MENU;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ScreenSwitchCommandTest {

    private GdxGame game;
    private ArrayList<String> args;
    private ScreenSwitchCommand command;

    @BeforeEach
    void beforeEach() {
        game = mock(GdxGame.class);
        args = new ArrayList<>();
        command = new ScreenSwitchCommand(game);
    }

    @Test
    void shouldSwitch() {
        // Should be case-insensitive
        args.add("maiN_meNU");
        command.action(args);
        verify(game).setScreen(MAIN_MENU);
    }

    @Test
    void tooManyArgs() {
        args.add("maiN_meNU");
        args.add("extra_invalid_arg");
        command.action(args);
        verify(game, never()).setScreen(Mockito.any(GdxGame.ScreenType.class));
    }

    @Test
    void tooFewArgs() {
        command.action(args);
        verify(game, never()).setScreen(Mockito.any(GdxGame.ScreenType.class));
    }
}