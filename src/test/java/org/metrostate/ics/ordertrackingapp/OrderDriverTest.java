package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OrderDriverTest {
    OrderTrackerController controller;
    OrderDriver driver;
    OrderListener orderListener;

    @BeforeEach
    void setUp() {
        controller = mock(OrderTrackerController.class);
        orderListener = mock(OrderListener.class);
        driver = new OrderDriver();
    }

    @Test
    void addOrder() {
        //arrange
        Order order = mock(Order.class);
        driver.addOrder(order);

        assertEquals(order,driver.getOrders().getFirst());
    }

    @Test
    void getOrderCount1() {
        //arrange

        Order order = mock(Order.class);
        driver.addOrder(order);

        //assert count == 1
        assertEquals(1,driver.getOrderCount());
    }

    @Test
    void getOrderCount0() {
        //arrange and assert count = 0
        assertEquals(0,driver.getOrderCount());
    }

    @Test
    void startOrder() {
        //arrange
        Order order = new Order();
        driver.addOrder(order);
        driver.startOrder(order);

        //assert order is inProgress
        assertEquals(Status.inProgress,driver.getOrders().getFirst().getStatus());
    }

    @Test
    void completeOrderInProgress() {
        Order order = new Order();
        driver.addOrder(order);
        driver.startOrder(order);
        driver.completeOrder(order);
        assertEquals(Status.completed,driver.getOrders().getFirst().getStatus());
    }

    @Test
    void completeOrderInWaiting() {
        Order order = new Order();
        driver.addOrder(order);
        driver.completeOrder(order);
        assertEquals(Status.waiting,driver.getOrders().getFirst().getStatus());
    }
    @Test
    void orderExportJSON() {
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        FoodItem foodItem = new FoodItem("apple",1,3.20);
        foodItems.add(foodItem);
        Order order = new  Order(1,Type.togo,1233123,foodItems);
        driver.addOrder(order);

        File fileDirectory = new File("src/test/java/org/metrostate/ics/ordertrackingapp/testFolder");
        if (!fileDirectory.exists()){
            fileDirectory.mkdir();
        }
        OrderDriver.orderExportJSON(driver.getOrders().getFirst(), fileDirectory.getPath());
        assertTrue(fileDirectory.exists());


        File testfile = new File("src/test/java/org/metrostate/ics/ordertrackingapp/testFolder/Saved_Order1.json");
        assertTrue(testfile.exists());

        //after
        testfile.delete();
        fileDirectory.delete();
    }

    @Test
    void saveAllOrdersToJSON() {
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        FoodItem foodItem = new FoodItem("apple",1,3.20);
        foodItems.add(foodItem);
        Order order = new  Order(1,Type.togo,1233123,foodItems);
        driver.addOrder(order);

        File fileDirectory = new File("src/test/java/org/metrostate/ics/ordertrackingapp/testFolder");
        if (!fileDirectory.exists()){
            fileDirectory.mkdir();
        }

        //assert

        assertTrue(fileDirectory.exists());

        //arrange
        driver.saveAllOrdersToJSON(fileDirectory.getPath());
        File testfile = new File("src/test/java/org/metrostate/ics/ordertrackingapp/testFolder/Saved_Order1.json");

        //assert
        assertTrue(testfile.exists());

        //after
        testfile.delete();
        fileDirectory.delete();
    }

    @Test
    void clearAllOrders() {
        Order order = new  Order(1,Type.togo,1233123,null);
        driver.addOrder(order);
        driver.clearAllOrders();
        assertTrue(driver.getOrders().isEmpty());
    }

    @Test
    void getOrders() {
        //arrange
        Order order = new  Order(1,Type.togo,1233123,null);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        driver.addOrder(order);
        //assert
        assertEquals(orders,driver.getOrders());
    }

    @Test
    void cancelOrderGUI() {
        Order order = new  Order(1,Type.togo,1233123,null);
        driver.addOrder(order);
        driver.cancelOrderGUI(order);
        assertEquals(Status.cancelled,driver.getOrders().getFirst().getStatus());
    }
    @Test
    void cancelOrderGUICompleted() {
        Order order = new  Order(1,Type.togo,1233123,null);
        driver.addOrder(order);
        driver.getOrders().getFirst().setStatus(Status.completed);
        driver.cancelOrderGUI(order);
        assertEquals(Status.completed,driver.getOrders().getFirst().getStatus());

    }

    @Test
    void uncancelOrder() {
        //arrange
        Order order = new  Order(1,Type.togo,1233123,null);
        driver.addOrder(order);
        driver.cancelOrderGUI(order);

        //assert
        assertEquals(Status.cancelled,driver.getOrders().getFirst().getStatus());

        //arrange
        driver.uncancelOrder(order);
        //assert
        assertEquals(Status.waiting,driver.getOrders().getFirst().getStatus());
    }
}