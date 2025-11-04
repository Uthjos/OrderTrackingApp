package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testToString() {
        assertEquals("Cancelled", Status.cancelled.toString());
        assertEquals("In Progress", Status.inProgress.toString());
        assertEquals("Waiting", Status.waiting.toString());
        assertEquals("Completed", Status.completed.toString());
    }
}