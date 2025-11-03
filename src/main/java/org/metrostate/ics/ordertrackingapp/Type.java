package org.metrostate.ics.ordertrackingapp;

/**
 * Order types.
 */
public enum Type {
    togo,
    pickup,
    delivery;

    @Override
    public String toString() {
        return switch (this) {
            case togo -> "To-go";
            case pickup -> "Pickup";
            case delivery -> "Delivery";
            default -> super.toString();
        };
    }
}

