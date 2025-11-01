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

    // listener to watch for changes to an order's status -- in order to update GUI when buttons are clicked
    public interface OrderChangeListener {
        void orderAdded(Order order);
        void orderChanged(Order order);
    }

    private final List<OrderChangeListener> listeners = new ArrayList<>();

    public void addListener(OrderChangeListener l) {
        if (l == null) return;
        listeners.add(l);
    }

    public void removeListener(OrderChangeListener l) {
        listeners.remove(l);
    }

    //for adding new order to GUI
    private void notifyOrderAdded(Order o) {
        for (OrderChangeListener l : new ArrayList<>(listeners)) {
            try { l.orderAdded(o); } catch (Exception ignored) {}
        }
    }

    //for updating order status in GUI
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
     * Creates a JSONArray of the Orders list
     * and puts them in a file in the directory code/src/main/java/export.
     * note: export is not pretty to do that we need libraries GSON or Jackson
     *
     * @param fileName    The name of the file to export to
     * @param orderDriver The OrderDriver instance containing all orders
     * @return true if the export succeeds, false otherwise
     */
    public static boolean exportOrdersToJSON(String fileName, OrderDriver orderDriver) {

        JSONArray ordersArray = new JSONArray();
        if (orderDriver.getOrders().isEmpty()) {
            return false; // no orders to export
        }
        for (Order order : orderDriver.getOrders()) {
            JSONObject ordersJSON = new JSONObject();
            ordersJSON.put("orderID", order.getOrderID());
            ordersJSON.put("status", order.getStatus());
            ordersJSON.put("totalPrice", String.format("%.2f", order.getTotalPrice()));
            ordersJSON.put("date", order.getDate());
            ordersJSON.put("type", order.getType());
            ordersJSON.put("completeTime", System.currentTimeMillis());

            JSONArray orderFoodsList = new JSONArray();
            for (FoodItem food : order.getFoodList()) {
                JSONObject foodJSON = new JSONObject();
                foodJSON.put("name", food.getName());
                foodJSON.put("quantity", food.getQuantity());
                foodJSON.put("price", food.getPrice());
                orderFoodsList.put(foodJSON);
            }

            ordersJSON.put("foodList", orderFoodsList);

            ordersArray.put(ordersJSON);
        }

        String fileDirectory = "export";
        String filePath = fileDirectory + "/" + fileName;

        File fileDir = new File(fileDirectory);
        if (!fileDir.exists()) {
            boolean created = fileDir.mkdirs();
            if (!created) {
                System.out.println("Error creating directory: " + fileDirectory);
                return false;
            } else {
                System.out.println("Directory created: " + fileDirectory);
            }
        }

        // write ordersArray to a file as a single JSON array, with newlines between objects
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write("[\n");
            for (int i = 0; i < ordersArray.length(); i++) {
                fw.write(ordersArray.get(i).toString());
                if (i < ordersArray.length() - 1) {
                    fw.write(",\n");
                }
            }
            fw.write("\n]");
            fw.flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public static boolean individualOrderExportJSON(String fileName, Order order){
        JSONObject OrderJSON = new JSONObject();

        OrderJSON.put("orderID", order.getOrderID());
        OrderJSON.put("date", order.getDate());

        OrderJSON.put("totalPrice", String.format("%.2f", order.getTotalPrice()));
        OrderJSON.put("type", order.getType());
        OrderJSON.put("status", order.getStatus());

        JSONArray orderFoodsList = new JSONArray();
        for (FoodItem food : order.getFoodList()) {
            JSONObject foodJSON = new JSONObject();
            foodJSON.put("name", food.getName());
            foodJSON.put("quantity", food.getQuantity());
            foodJSON.put("price", food.getPrice());
            orderFoodsList.put(foodJSON);
        }


        String fileDirectory = "current_status";
        String filePath = fileDirectory + "/" + fileName;

        File fileDir = new File(fileDirectory);
        if (!fileDir.exists()) {
            boolean created = fileDir.mkdirs();
            if (!created) {
                System.out.println("Error creating directory: " + fileDirectory);
                return false;
            } else {
                System.out.println("Directory created: " + fileDirectory);
            }
        }

        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(OrderJSON.toString(4)); // pretty print with indent of 4
            fw.flush();
        } catch (IOException e) {
            return false;
        }


        return true;
    }

    /**
     * Returns a list of all orders in the system.
     *
     * @return List of all orders
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Returns a list of completed orders.
     *
     * @return List of completed orders
     */
    public List<Order> getCompleteOrders() {
        List<Order> completedOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == Status.completed) {
                completedOrders.add(order);
            }
        }
        return completedOrders;
    }

    /**
     * Returns a list of incomplete orders.
     *
     * @return List of incomplete orders
     */
    public List<Order> getIncompleteOrders() {
        List<Order> incompleteOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() != Status.completed) {
                incompleteOrders.add(order);
            }
        }
        return incompleteOrders;
    }

    public boolean cancelOrderGUI(Order order) {
        if (order == null || order.getStatus() == Status.completed){
            return false;
        }
        order.setStatus(Status.cancelled);
        lastCancelledOrder = order;
        notifyOrderChanged(order);
        return true;
    }

    public void undoCancel() {
        if (lastCancelledOrder != null && lastCancelledOrder.getStatus() == Status.cancelled) {
            Order prev = lastCancelledOrder;
            prev.setStatus(Status.waiting);
            // notify listeners about the change to the previously cancelled order
            notifyOrderChanged(prev);
            lastCancelledOrder = null;
        }
    }

    /**
     * Cancels an order by removing it from the orders list and setting its status to "CANCELED".
     *
     * @param order The order to cancel
     */
    public void cancelOrder(Order order) {
        order.setStatus(Status.cancelled);
        notifyOrderChanged(order);
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

    /**
     * Returns a list of canceled orders.
     *
     * @return List of canceled orders
     */
    public List<Order> getCancelledOrders() {
        List<Order> cancelledOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == Status.cancelled) {
                cancelledOrders.add(order);
            }
        }
        return cancelledOrders;
    }
}
