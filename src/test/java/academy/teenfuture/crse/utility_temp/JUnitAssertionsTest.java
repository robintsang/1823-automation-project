package com.example.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class JUnitAssertionsTest {

    @Test
    public void testAssertEquals() {
        String str1 = "hello";
        String str2 = "HELLO".toLowerCase();
        assertEquals(str1, str2, "The strings should be equal ignoring case");
    }

    @Test
    public void testAssertTrue() {
        int num = 6;
        assertTrue(num > 5, "The number should be greater than 5");
    }

    @Test
    public void testAssertFalse() {
        int num = 4;
        assertFalse(num > 5, "The number should be less than or equal to 5");
    }

    @Test
    public void testAssertNotNull() {
        Object obj = new Object();
        assertNotNull(obj, "The object should not be null");
    }

    @Test
    public void testAssertNull() {
        Object obj = null;
        assertNull(obj, "The object should be null");
    }

    @Test
    public void testAssertArrayEquals() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertArrayEquals(arr1, arr2, "The arrays should be equal");
    }

    @Test
    public void testAssertThrows() {
        assertThrows(NullPointerException.class, () -> {
            String str = null;
            str.length(); // This will throw NullPointerException
        }, "Expected a NullPointerException to be thrown");
    }
}