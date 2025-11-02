package org.metrostate.ics.ordertrackingapp;

import java.util.ArrayList;
import java.util.List;

import static org.metrostate.ics.ordertrackingapp.Status.*;

/**
 * Represents a customer's order in the system.
 */
public class Order {
    private int orderId;
    private long date;
    private double totalPrice;
    private Type type;
    private Status status;
    private ArrayList<FoodItem> foodList;
    private String company;

    /**
     * Creates an empty order with the status set to "INCOMING" and an empty food list (empty constructor).
     */
    public Order() {
        this.foodList = new ArrayList<>();
        this.status = waiting;
        this.company = null;
    }

    /**
     * Creates a new order with the given ID, type, date, and an initial list of food items.
     *
     * @param orderId   The unique ID of the order
     * @param type      The type of order
     * @param date      The timestamp of the order
     * @param foodList  The initial list of food items
     */
    public Order(int orderId, Type type, long date, List<FoodItem> foodList) {
        this.orderId = orderId;
        this.type = type;
        this.date = date;
        if (foodList != null) {
            this.foodList = new ArrayList<>(foodList);
        } else {
            this.foodList = new ArrayList<>();
        }
        this.status = waiting;
        this.totalPrice = sumPrice();
        this.company = null;
    }

    /**
     * Constructor that is used to restore previous state after
     * failure to close
     *
     * @param orderId
     * @param date
     * @param totalPrice
     * @param type
     * @param status
     * @param foodList
     */
    public Order(int orderId, long date, double totalPrice, Type type, Status status, List<FoodItem> foodList) {
        this.orderId = orderId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.type = type;
        this.status = status;
        this.foodList = new ArrayList<>(foodList);
        this.company = null;
    }

    /**
     * Gets the date when the order was placed.
     *
     * @return The order date
     */
    public long getDate() {
        return date;
    }

    /**
     * Gets the total price of the order.
     *
     * @return The total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Gets the type of the order.
     *
     * @return The order type
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the current status of the order.
     *
     * @return The order status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets the list of food items in the order.
     *
     * @return The list of food items
     */
    public ArrayList<FoodItem> getFoodList() {
        return foodList;
    }

    /**
     * Gets the unique ID of the order.
     *
     * @return The order ID
     */
    public int getOrderID() {
        return orderId;
    }

    /**
     * Recalculates the total price of the current food list.
     *
     * @return The sum of all food items' prices multiplied by their quantities
     */
    public double sumPrice() {
        double sum = 0.0;

        if (foodList == null) {
            return 0;
        }

        // Calculates the total price of all items in the food list
        for (FoodItem item : foodList) {
            sum = sum + item.getPrice() * item.getQuantity();
        }

        return sum;
    }

    /**
     * Adds a single FoodItem to the order's food list.
     *
     * @param f The FoodItem to add
     * @return  true if the item was added successfully, false if the item is null
     */
    public boolean addFoodItem(FoodItem f) {
        if (f == null) {
            return false;
        }

        if (foodList == null) {
            foodList = new ArrayList<>();
        }

        boolean priceUpdate = foodList.add(f);

        if (priceUpdate) {
            totalPrice = sumPrice();
        }

        return priceUpdate;
    }

    /**
     * Updates the status of the order.
     *
     * @param newStatus The new status
     */
    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * Returns a formatted string representing the order, including all food items.
     *
     * @return A formatted string of the order
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(FoodItem foodItem: foodList){
            s.append(foodItem.toString());
        }
        java.time.ZonedDateTime zdt = java.time.Instant.ofEpochMilli(this.date).atZone(java.time.ZoneId.of("America/Chicago"));
        String formattedDate = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z").format(zdt);

        return "Order #" + orderId + "\n" +
                formattedDate + "\n\n" +
                //"Status: " + status + '\n' +
                "Status: " + displayStatus() + '\n' +
                //"Type: " + type + '\n' +
                "Type: " + displayType() + '\n' +
                "Items: " + s +
                String.format("\n\nTotal Price: $%.2f", totalPrice);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public static Status parseOrderStatus(String stringStatus){
        if(stringStatus == null){
            return null;
        }

        try {
            return Status.valueOf(stringStatus);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status string: " + stringStatus);
            return null;
        }
    }

    public static Type parseOrderType(String stringType){
        if(stringType == null){
            return null;
        }

        try {
            return Type.valueOf(stringType);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid type string: " + stringType);
            return null;
        }
    }

    public String displayStatus() {
        switch (this.status) {
            case completed:
                return "Completed";
            case waiting:
                return "Waiting";
            case inProgress:
                return "In Progress";
            case cancelled:
                return "Cancelled";
            default:
                return "Error: No Status";
        }
    }

    public String displayType() {
        switch (this.type) {
            case togo:
                return "Togo";
            case pickup:
                return "Pickup";
            case delivery:
                return "Delivery";
            default:
                return "Error: No Type";
        }
    }
}
