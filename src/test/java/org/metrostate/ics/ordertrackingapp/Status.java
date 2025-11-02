package org.metrostate.ics.ordertrackingapp;

public enum Status {
    cancelled,
    completed,
    inProgress,
    waiting;

    @Override
    public String toString() {
        return switch (this) {
            case cancelled -> "Cancelled";
            case completed -> "Completed";
            case inProgress -> "In Progress";
            case waiting -> "Waiting";
        };
    }
}
