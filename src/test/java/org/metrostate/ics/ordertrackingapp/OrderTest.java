package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;

import org.metrostate.ics.ordertrackingapp.*;


class OrderTest {
    Order order;


    @BeforeEach
    void setUp() {
        //arrange set of food items
        FoodItem foodItem1 = new FoodItem("Apple",1,1.5);
        FoodItem foodItem2 = new FoodItem("Orange",2,2.30);
        FoodItem foodItem3 = new FoodItem("Taco",3,3.99);
        FoodItem foodItem4 = new FoodItem("Milk",4,4.10);

        //arrange new test order with fooditems
        order = new Order();
        order.addFoodItem(foodItem1);
        order.addFoodItem(foodItem2);
        order.addFoodItem(foodItem3);
        order.addFoodItem(foodItem4);
    }

    @Test
    void sumPrice() {
        assertEquals(34.47,order.sumPrice());
    }

    @Test
    void addFoodItem() {
        //create a test FoodItem and add it to order
        FoodItem foodItem = new FoodItem("addFoodItemTest",1,100);
        order.addFoodItem(foodItem);

        assertTrue(order.getFoodList().contains(foodItem));
    }

    @Test
    void setStatus() {
        //check successful set of all Status enums
        for(int i=0; i <Status.values().length; i++){
            order.setStatus(Status.values()[i]);

            assertEquals(Status.values()[i],order.getStatus());
        }
    }

    //curious if we want to make this or not
    /*
    @Test
    void testToString() {

    }
    */

    @Test
    void setCompany() {
        order.setCompany("company");
        assertEquals("company",order.getCompany());
    }

    //has no usages
    @Test
    void parseOrderStatus() {

    }
    //has no usages
    @Test
    void parseOrderType() {
    }
    //wouldn't this be not necessary if we just use the toString?
    @Test
    void displayStatus() {

    }
    //wouldn't this be not necessary if we just use the toString?
    @Test
    void displayType() {
    }
}