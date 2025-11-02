package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.internal.matchers.Or;

class OrderTest {

    @Mock
    Order order;
    @Mock
    FoodItem foodItem;
    @Mock
    ArrayList<FoodItem> foodItemList;


    @Test
    void getFoodList() {
    }

    @Test
    void sumPrice() {
        //Arrange Mock items
        when(foodItem.getPrice())
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