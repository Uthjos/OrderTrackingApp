package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodItemTest {
    @Test
    void getName() {
        FoodItem testItem = new FoodItem("Apple",4,3.22);
        assertEquals("Apple",testItem.getName());
    }

    @Test
    void setName() {
    }

    @Test
    void getQuantity() {
    }

    @Test
    void setQuantity() {
    }

    @Test
    void getPrice() {
    }

    @Test
    void setPrice() {
    }

    @Test
    void testToString() {
    }
}