package com.elypia.commandler.string.test;

import com.elypia.commandler.string.StringCommandler;
import com.elypia.commandler.string.client.*;
import com.elypia.commandler.string.modules.ValidationModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationModuleTest {

    private static StringCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        commandler = new StringCommandler(new StringClient(), ">");
        commandler.registerModule(new ValidationModule());
    }

    @Test
    public void testLength() {
        StringEvent event = new StringEvent(">valid length a jenni");

        String expected = "ajenni";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    @Test
    public void testLimit() {
        StringEvent event = new StringEvent(">valid limit 100 150");

        String expected = "250";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    @Test
    public void testOption() {
        StringEvent event = new StringEvent(">valid option seth");

        String expected = "seth";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    @Test
    public void testPeriod() {
        StringEvent event = new StringEvent(">valid period \"500 seconds\"");

        String expected = "8 minutes, and 20 seconds";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }
}
