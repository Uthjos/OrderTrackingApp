package org.metrostate.ics.ordertrackingapp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Parser adapter class that reads a JSON file and creates a corresponding Order object.
 * Populates the Order with data from the JSON file.
 * Created for ICS 372-01
 * @author Joseph Murtha hw4546dw
 */
public class Parser {
    private static int nextOrderNumber = 1;
    /**
     * Parser method creates order object with data populated from given JSON file.
     * Generates a random orderID for the Order.
     *
     * @param file              JSON file to be read
     * @return                  Order object populated with data from the JSON file
     * @throws IOException      if the file cannot be read
     */
	public static Order parseJSONOrder(File file) throws IOException {
        long orderDate;
        String orderType;
        List<FoodItem> foodItemList = new ArrayList<>();

        try (FileReader fr = new FileReader(file)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(fr));
            JSONObject orderJson = jsonObject.getJSONObject("order");
            orderDate = orderJson.getLong("order_date");
            orderType = orderJson.getString("type");
            JSONArray itemArray = orderJson.getJSONArray("items");
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject item = itemArray.getJSONObject(i);
                int quantity = item.getInt("quantity");
                double price = item.getDouble("price");
                String name = item.getString("name");
                foodItemList.add(new FoodItem(name, quantity, price));
            }
        }
         return new Order(getNextOrderNumber(),orderType,orderDate,foodItemList);
    }

    /**
     * Static helper method
     * returns next order number and increments the counter
     * @return int, next Order ID number
     */
    private static int getNextOrderNumber(){
        return nextOrderNumber++;
    }
    /**
     * Main test method for the Parser class.
     * Uses a hardcoded JSON file to test the parser method.
     * Prints to console.
     */
    public static void main(String[] args) throws IOException {
        File file = new File("src/main/testOrders/order_09-16-2025_10-00.json");
        Order myOrder = Parser.parseJSONOrder(file);
        System.out.println(myOrder);
    }
}
