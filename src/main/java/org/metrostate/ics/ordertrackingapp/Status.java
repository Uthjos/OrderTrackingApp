package org.metrostate.ics.ordertrackingapp;

/**
 * Order statuses.
 */
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
            case inProgress -> "In progress";
            case waiting -> "Waiting";
            default -> super.toString();
        };
    }
}
