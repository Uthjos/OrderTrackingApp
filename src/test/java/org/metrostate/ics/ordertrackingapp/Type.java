package org.metrostate.ics.ordertrackingapp;

public enum Type {
    togo,
    pickup,
    delivery;

    @Override
    public String toString() {
        return switch (this) {
            case togo -> "Togo";
            case pickup -> "Pickup";
            case delivery -> "Delivery";
        };
    }
}
