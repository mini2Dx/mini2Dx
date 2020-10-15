package org.mini2Dx.ui.element;

import org.junit.Test;

import static org.junit.Assert.*;

public class TextBoxTest {

    @Test
    public void testTextBoxWithZeroAsCharacterLimit() {
        TextBox textBox = new TextBox();
        textBox.setCharacterLimit(0);
        simulateUserInput(textBox);

        assertEquals(textBox.getValue(), "");
        assertTrue(textBox.isCharacterLimitReached());
    }

    @Test
    public void testTextBoxWithCharacterLimit() {
        TextBox textBox = new TextBox();
        textBox.setCharacterLimit(3);
        simulateUserInput(textBox);

        assertEquals(textBox.getValue(), "012");
        assertTrue(textBox.isCharacterLimitReached());
    }

    @Test
    public void testTextBoxWithoutCharacterLimit() {
        TextBox textBox = new TextBox();
        simulateUserInput(textBox);

        assertEquals(textBox.getValue(), "01234");
        assertFalse(textBox.isCharacterLimitReached());
    }

    private void simulateUserInput(TextBox textBox) {
        String valueToAdd = "";
        for(int i = 0; i < 5; i++) {
            valueToAdd += Integer.toString(i);
            textBox.setValue(valueToAdd);
        }
    }
}
