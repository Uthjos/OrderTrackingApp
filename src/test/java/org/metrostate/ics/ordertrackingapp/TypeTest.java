package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeTest {

    @Test
    void testToString() {
        assertEquals("Togo", Type.togo.toString());
        assertEquals("Pickup", Type.pickup.toString());
        assertEquals("Delivery", Type.delivery.toString());
    }
}