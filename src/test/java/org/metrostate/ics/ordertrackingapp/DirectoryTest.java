package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryTest {

    @Test
    void getPath() {
        assertEquals("orderFiles/savedOrders",Directory.savedOrders.getPath());
        assertEquals("orderFiles/testOrders",Directory.testOrders.getPath());
        assertEquals("orderFiles/importOrders",Directory.importOrders.getPath());
    }

}