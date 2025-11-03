package org.metrostate.ics.ordertrackingapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Manages a collection of orders in the system.
 * Provides methods to add, start, complete, display, and export orders.
 */
public class OrderDriver {
    private List<Order> orders;
    private Order lastCancelledOrder = null;

    /**
     * Listener to watch for changes to an order's status -- in order to update the GUI when buttons are clicked.
     */
    public interface OrderChangeListener {
        /**
         * Called when a new order is added.
         *
         * @param order The newly added order
         */
        void orderAdded(Order order);

        /**
         * Called when an existing order is updated.
         *
         * @param order The updated order
         */
        void orderChanged(Order order);
    }

    private final List<OrderChangeListener> listeners = new ArrayList<>();

    /**
     * Adds a listener.
     *
     * @param l The listener to add
     */
    public void addListener(OrderChangeListener l) {
        if (l == null) return;
        listeners.add(l);
    }

    /**
     * Removes a listener.
     *
     * @param l The listener to remove
     */
    public void removeListener(OrderChangeListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all listeners that a new order has been added to update the GUI.
     *
     * @param o The order that was added
     */
    private void notifyOrderAdded(Order o) {
        for (OrderChangeListener l : new ArrayList<>(listeners)) {
            try { l.orderAdded(o); } catch (Exception ignored) {}
        }
    }

    /**
     * Notifies all listeners that an order has changed, updating the GUI.
     *
     * @param o The order that changed
     */
    private void notifyOrderChanged(Order o) {
        for (OrderChangeListener l : new ArrayList<>(listeners)) {
            try { l.orderChanged(o); } catch (Exception ignored) {}
        }
    }

    /**
     * Constructs a new OrderDriver with empty lists for all orders, incomplete orders, and completed orders (empty constructor).
     */
    public OrderDriver() {
        orders = new ArrayList<>();
    }

    /**
     * Adds a new order to the system.
     * The order is added to both the list of all orders and the list of incomplete orders.
     *
     * @param order The order to add
     */
    public void addOrder(Order order) {
        orders.add(order);
        notifyOrderAdded(order);
    }

    /**
     * Returns the total number of orders in the system.
     *
     * @return The number of orders
     */
    public int getOrderCount() {
        return orders.size();
    }

    /**
     * Starts an order if its status is "INCOMING".
     * Changes the status of the order to "IN PROGRESS".
     *
     * @param order The order to start
     */
    public void startOrder(Order order) {
        // only start if it's waiting, otherwise do nothing
        if (order.getStatus() == Status.waiting) {
            order.setStatus(Status.inProgress);
            notifyOrderChanged(order);
        }
    }

    /**
     * Completes an order if its status is "IN PROGRESS".
     * Changes the status to "COMPLETED", removes it from incompleteOrders, and adds it to completeOrders.
     *
     * @param order The order to complete
     */
    public void completeOrder(Order order) {
        // only complete if it's in progress, otherwise do nothing
        if (order.getStatus() == Status.inProgress) {
            order.setStatus(Status.completed);
            notifyOrderChanged(order);
        }
    }

    /**
     * Exports a single order as a JSON file to the savedOrders directory.
     *
     * @param order             The order to save
     * @param fileDirectory     The folder where the JSON file will be created
     */
    public static void orderExportJSON(Order order, String fileDirectory) {
        JSONObject OrderJSON = new JSONObject();

        OrderJSON.put("orderID", order.getOrderID());
        OrderJSON.put("date", order.getDate());
        OrderJSON.put("totalPrice", order.getTotalPrice());
        OrderJSON.put("type", order.getType());
        OrderJSON.put("status", order.getStatus());
        OrderJSON.put("company", order.getCompany());

        JSONArray orderFoodsList = new JSONArray();
        for (FoodItem food : order.getFoodList()) {
            JSONObject foodJSON = new JSONObject();
            foodJSON.put("name", food.getName());
            foodJSON.put("quantity", food.getQuantity());
            foodJSON.put("price", food.getPrice());
            orderFoodsList.put(foodJSON);
        }

        OrderJSON.put("foodList", orderFoodsList);

        String fileName = "Saved_Order" + order.getOrderID() + ".json";
        String filePath = fileDirectory + File.separator + fileName;

        File fileDir = new File(fileDirectory);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(OrderJSON.toString(4));
            fw.flush();
        } catch (IOException e) {
            System.err.println("Error saving order to JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     * Saves all orders in the driver to JSON files in the savedOrders directory.
     *
     * @param fileDirectory The directory to save orders to
     */
    public void saveAllOrdersToJSON(String fileDirectory) {
        for (Order order : orders) {
            orderExportJSON(order, fileDirectory);
        }
    }

    /**
     * Clears all orders from the system.
     */
    public void clearAllOrders() {
        orders.clear();
        lastCancelledOrder = null;
    }

    /**
     * @return List of all orders
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Cancels an order.
     *
     * @param order     The order to cancel
     * @return          True if the order was successfully cancelled, false otherwise
     */
    public boolean cancelOrderGUI(Order order) {
        if (order == null || order.getStatus() == Status.completed){
            return false;
        }
        order.setStatus(Status.cancelled);
        lastCancelledOrder = order;
        notifyOrderChanged(order);
        return true;
    }

    /**
     * Un-cancel a specific order (set status back to waiting) if it is currently cancelled.
     * Returns true if the order was un-cancelled, false otherwise.
     */
    public boolean uncancelOrder(Order order) {
        if (order == null) return false;
        if (order.getStatus() != Status.cancelled) return false;
        order.setStatus(Status.waiting);
        if (lastCancelledOrder == order) {
            lastCancelledOrder = null;
        }
        notifyOrderChanged(order);
        return true;
    }
}
