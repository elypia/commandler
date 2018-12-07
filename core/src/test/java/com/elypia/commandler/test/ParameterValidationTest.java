package com.elypia.commandler.test;

import com.elypia.commandler.impl.TestApp;
import com.elypia.commandler.impl.modules.ValidationModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterValidationTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(ValidationModule.class);
    }

    @Test
    public void testLength() {
        String expected = "ajenni";
        String actual = app.execute(">pvalid length a jenni");

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidLength() {
        String expected = "Invalid";
        String actual = app.execute(">pvalid length an jenni");

        assertEquals(expected, actual);
    }

    @Test
    public void testLimit() {
        String expected = "250";
        String actual = app.execute(">pvalid limit 100 150");

        assertEquals(expected, actual);
    }

    @Test
    public void testOption() {
        String expected = "seth";
        String actual = app.execute(">pvalid option seth");

        assertEquals(expected, actual);
    }

    @Test
    public void testPeriod() {
        String expected = "8 minutes, and 20 seconds";
        String actual = app.execute(">pvalid period \"500 seconds\"");

        assertEquals(expected, actual);
    }
}
