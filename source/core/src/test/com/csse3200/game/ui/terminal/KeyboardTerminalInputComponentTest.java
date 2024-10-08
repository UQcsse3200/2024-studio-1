package com.csse3200.game.ui.terminal;

import com.badlogic.gdx.Input;
import com.csse3200.game.components.player.KeyMapping;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class KeyboardTerminalInputComponentTest {
  @Test
  void shouldToggleTerminalOpenClose() {
    Terminal terminal = spy(Terminal.class);
    KeyboardTerminalInputComponent terminalInput = new KeyboardTerminalInputComponent(terminal);

    terminal.setClosed();

    terminalInput.keyDown(Input.Keys.F1);
    assertTrue(terminal.isOpen());

    terminalInput.keyDown(Input.Keys.F1);
    assertFalse(terminal.isOpen());

    verify(terminal, times(2)).toggleIsOpen();
    verify(terminal).setOpen();
    verify(terminal, times(2)).setClosed();
  }

  /*
  @Test
  void getItemTest() {
    KeyboardPlayerInputComponent inputComponent = new KeyboardPlayerInputComponent();
    KeyMapping.KeyBinding keyBinding = KeyMapping.KeyBinding.USE_1;
    assertEquals(1, inputComponent.getItemNum(keyBinding));
  }

   */

  @Test
  void shouldUpdateMessageOnKeyTyped() {
    Terminal terminal = mock(Terminal.class);
    when(terminal.isOpen()).thenReturn(true);
    KeyboardTerminalInputComponent terminalInput = new KeyboardTerminalInputComponent(terminal);

    terminalInput.keyTyped('a');
    terminalInput.keyTyped('b');
    verify(terminal).appendToMessage('a');
    verify(terminal).appendToMessage('b');

    terminalInput.keyTyped('\b');
    verify(terminal).handleBackspace();

    terminalInput.keyTyped('\n');
    verify(terminal).processMessage();
  }

  @Test
  void shouldHandleMessageWhenTerminalOpen() {
    Terminal terminal = mock(Terminal.class);
    KeyboardTerminalInputComponent terminalInput = new KeyboardTerminalInputComponent(terminal);

    when(terminal.isOpen()).thenReturn(true);
    assertTrue(terminalInput.keyDown('a'));
    assertTrue(terminalInput.keyUp('a'));

    when(terminal.isOpen()).thenReturn(false);
    assertFalse(terminalInput.keyDown('a'));
    assertFalse(terminalInput.keyUp('a'));
  }
}
