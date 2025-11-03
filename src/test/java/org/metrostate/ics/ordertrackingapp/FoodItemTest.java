package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodItemTest {
    @Test
    void getName() {
        FoodItem testItem = new FoodItem("Apple",4,3.22);
        assertEquals("Apple",testItem.getName());
    }

    @Test
    void getQuantity() {
        FoodItem testItem = new FoodItem("Apple",4,3.22);
        assertEquals(4,testItem.getQuantity());
    }

    @Test
    void getPrice() {
        FoodItem testItem = new FoodItem("Apple",4,3.22);
        assertEquals(3.22,testItem.getPrice());
    }

    @Test
    void testToString() {
        FoodItem testItem = new FoodItem("Apple",4,3.22);

        assertEquals("\n  " + 4 + "x " + "Apple" + " - " + String.format("$%.2f", 3.22) + " each", testItem.toString());
    }
}