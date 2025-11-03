package org.metrostate.ics.ordertrackingapp;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parseJSONOrder() throws IOException {
        //arrange
        File file = new File("src/main/orderFiles/testOrders/order_09-16-2025_10-00.json");
        Order order = Parser.parseJSONOrder(file);

        //assert
        assertEquals(1,order.getOrderID());
        assertEquals(Type.togo, order.getType());
        assertEquals(1758027600000L,order.getDate());

        List<FoodItem> foodItemList = new ArrayList<>();
        foodItemList.add(new FoodItem("Burger",1,4.39));
        foodItemList.add(new FoodItem("Fries",2,3.09));
        foodItemList.add(new FoodItem("Milkshake",1,5.09));

        for(int i=0;i<foodItemList.size();i++){
            assertEquals(foodItemList.get(i).getName(),order.getFoodList().get(i).getName());
            assertEquals(foodItemList.get(i).getPrice(),order.getFoodList().get(i).getPrice());
            assertEquals(foodItemList.get(i).getQuantity(),order.getFoodList().get(i).getQuantity());
        }

    }

    @Test
    void parseXMLOrder() throws IOException {
        File file = new File("src/main/orderFiles/~ordersBackup/order_09-16-2025_13-00.xml");
        Order order = Parser.parseXMLOrder(file);

        //assert
        assertEquals(1,order.getOrderID());
        assertEquals(Type.pickup, order.getType());
        assertEquals(1758038400000L,order.getDate());

        List<FoodItem> foodItemList = new ArrayList<>();
        foodItemList.add(new FoodItem("Grilled Cheese",1,4.39));
        foodItemList.add(new FoodItem("Milkshake",1,5.09));

        for(int i=0;i<foodItemList.size();i++){
            assertEquals(foodItemList.get(i).getName(),order.getFoodList().get(i).getName());
            assertEquals(foodItemList.get(i).getPrice(),order.getFoodList().get(i).getPrice());
            assertEquals(foodItemList.get(i).getQuantity(),order.getFoodList().get(i).getQuantity());
        }
    }

    //should we use?
    @Test
    void parseSavedJSONOrder() {
    }
}