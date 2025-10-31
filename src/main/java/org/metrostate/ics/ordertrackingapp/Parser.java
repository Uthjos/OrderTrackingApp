package org.metrostate.ics.ordertrackingapp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

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
        Type orderType;
        List<FoodItem> foodItemList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(new JSONTokener(new FileReader(file)));
        JSONObject orderJson = (JSONObject) jsonObject.get("order");
        orderDate = (long) orderJson.get("order_date");
        orderType = (Type) orderJson.get("type");
        JSONArray itemArray = (JSONArray) orderJson.get("items");
        for (Object o : itemArray) {
            int quantity = (int) (long) ((JSONObject) o).get("quantity");
            double price = (double) ((JSONObject) o).get("price");
            String name = (String) ((JSONObject) o).get("name");
            foodItemList.add(new FoodItem(name, quantity, price));

        }
        return new Order(getNextOrderNumber(),orderType,orderDate,foodItemList);
    }

    public static Order parseXMLOrder(File file) throws IOException {
        long orderDate = 0;
        Type orderType = null;
        List<FoodItem> foodItemList = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            NodeList nList = doc.getElementsByTagName("order");
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    orderDate = Long.parseLong(eElement.getElementsByTagName("name").item(0).getTextContent());
                    orderType = Type.valueOf(eElement.getElementsByTagName("OrderType").item(0).getTextContent());

                }
            }

            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                String name;
                int quantity;
                double price;
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    quantity = Integer.parseInt(eElement.getElementsByTagName("quantity").item(0).getTextContent());
                    price = Double.parseDouble(eElement.getElementsByTagName("price").item(0).getTextContent());

                    foodItemList.add(new FoodItem(name, quantity, price));
                }
            }


        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }


        return new Order (getNextOrderNumber(), orderType, orderDate, foodItemList);
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
        File file = new File("code/src/main/java/Resources/order_09-16-2025_10-00.json");
        Order myOrder = Parser.parseJSONOrder(file);
        System.out.println(myOrder);
    }
}
