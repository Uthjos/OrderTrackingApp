package org.metrostate.ics.ordertrackingapp;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.application.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderTrackerController {
    @FXML
    private VBox ordersContainer;

    @FXML
    private ScrollPane scrollPane;

    private List<String> orderFiles;
    private OrderListener orderListener;

    /**
     * Initializes the controller
     * Called automatically by JavaFX after FXML is loaded
     */
    @FXML
    public void initialize() {
        this.orderFiles = new ArrayList<>();
        // No need to create header here - it's in the FXML now
    }

    /**
     * Sets the OrderListener reference so it can be stopped on exit
     */
    public void setOrderListener(OrderListener orderListener) {
        this.orderListener = orderListener;
    }

    /**
     * Exits the application gracefully
     * Called by the Exit button in the FXML
     */
    @FXML
    private void exitApplication() {
        System.out.println("Exiting application...");

        // Stop the OrderListener thread
        if (orderListener != null) {
            orderListener.stop();
            System.out.println("OrderListener stopped.");
        }

        // Exit the JavaFX application
        Platform.exit();

        // Ensure JVM exits
        System.exit(0);
    }

    /**
     * Adds a new order file to the display
     * This method is called by the OrderListener when a new file is detected
     */
    public void addOrderFile(File file) {
        if (file == null || ordersContainer == null) {
            return;
        }

        String fileName = file.getName();

        // avoid duplicates
        if (orderFiles.contains(fileName)) {
            return;
        }

        orderFiles.add(fileName);

        VBox fileBox = createFileDisplay(fileName);
        // Insert at top of the orders list
        ordersContainer.getChildren().addFirst(fileBox);
    }

    /**
     * Creates a VBox display for a single order file
     */
    private VBox createFileDisplay(String fileName) {
        VBox fileBox = new VBox(5);
        fileBox.setPadding(new Insets(10));
        fileBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9;");

        // file name label
        Label fileNameLabel = new Label(fileName);
        fileNameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        // order company label, determined by file extension
        String orderCompany = fileName.toLowerCase().endsWith(".json") ? "FoodHub" : "GrubStop";
        Label typeLabel = new Label("Company: " + orderCompany);
        typeLabel.setFont(Font.font("System", 14));

        // color company
        if (orderCompany.equals("FoodHub")) {
            typeLabel.setStyle("-fx-text-fill: #2196f3;");
        } else {
            typeLabel.setStyle("-fx-text-fill: #ff9800;");
        }

        // no parse yet
        Label placeholderLabel = new Label("Pending parse implementation...");
        placeholderLabel.setFont(Font.font("System", FontPosture.ITALIC, 12));
        placeholderLabel.setStyle("-fx-text-fill: #666666;");

        fileBox.getChildren().addAll(fileNameLabel, typeLabel, placeholderLabel);

        return fileBox;
    }
}
