package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class OrderTest {

    @EnumSource(org.metrostate.ics.ordertrackingapp.Type)
    @BeforeEach
    void setUp() {
        //create a new order with predefined data
        ArrayList<FoodItem> foodList = new ArrayList<>();
        Order order = new Order(1234, Type.togo,100003442,);
    }
    @Test
    void getDate() {
    }

    @Test
    void getTotalPrice() {
    }

    @Test
    void getType() {
    }

    @Test
    void getStatus() {
    }

    @Test
    void getFoodList() {
    }

    @Test
    void getOrderID() {
    }

    @Test
    void sumPrice() {
    }

    @Test
    void addFoodItem() {
    }

    @Test
    void setStatus() {
    }

    @Test
    void testToString() {
    }

    @Test
    void getCompany() {
    }

    @Test
    void setCompany() {
    }

    @Test
    void parseOrderStatus() {
    }

    @Test
    void parseOrderType() {
    }

    @Test
    void displayStatus() {
    }

    @Test
    void displayType() {
    }
}