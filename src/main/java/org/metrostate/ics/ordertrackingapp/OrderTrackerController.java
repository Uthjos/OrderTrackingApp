package org.metrostate.ics.ordertrackingapp;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderTrackerController {
    @FXML
    private VBox ordersContainer;

    @FXML
    private VBox detailContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button cancelButton;

    @FXML
    private Button undoButton;

    @FXML
    private ComboBox<String> statusFilter;

    @FXML
    private ComboBox<String> typeFilter;

    private List<String> orderFiles;
    private OrderListener orderListener;
    private OrderDriver orderDriver;
    private VBox selectedFileBox = null;
    private Order selectedOrder = null;

    private final String BASE_BOX_STYLE = "-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #DFE8E8; -fx-cursor: hand;";

    @FXML
    public void initialize() {
        this.orderFiles = new ArrayList<>();
        // keep default behavior if scrollPane is present
        // refer to scrollPane to avoid unused-field warning; keep default behavior if null
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
        }
        // Cancel buttons are disabled at startup
        if (cancelButton != null){
            cancelButton.setDisable(true);
        }
        if (undoButton != null){
            undoButton.setDisable(true);
        }

        // Connects cancel and undo buttons to their action methods
        if (cancelButton != null) {
            cancelButton.setOnAction(e -> cancelSelectedOrder());
        }
        if (undoButton != null) {
            undoButton.setOnAction(e -> undoCancel());
        }

        if (statusFilter != null) {
            statusFilter.getItems().add("All");
            for (Status s : Status.values()) {
                String display = s.name().substring(0, 1).toUpperCase() + s.name().substring(1);
                statusFilter.getItems().add(display);
            }
            statusFilter.setValue("All");
            statusFilter.setOnAction(e -> applyFilters());
        }

        if (typeFilter != null) {
            typeFilter.getItems().add("All");
            for (Type t : Type.values()) {
                String display = formatType(t.name());
                typeFilter.getItems().add(display);
            }
            typeFilter.setValue("All");
            typeFilter.setOnAction(e -> applyFilters());
        }
    }

    public void setOrderListener(OrderListener orderListener) {

        this.orderListener = orderListener;
    }

    public void setOrderDriver(OrderDriver driver) {
        this.orderDriver = driver;
    }

    /**
     * Called by the Exit button in the FXML
     */
    @FXML
    private void exitApplication() {

        // stop the OrderListener thread
        if (orderListener != null) {
            orderListener.stop();
        }
        Platform.exit();
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

        // parse the order on FX thread
        orderFiles.add(fileName);
        new Thread(() -> {
            Order order = null;
            if (fileName.toLowerCase().endsWith(".json")) {
                try {
                    order = Parser.parseJSONOrder(file);
                } catch (IOException e) {
                    // leave null
                }
            }
            if (fileName.toLowerCase().endsWith(".xml")) {
                try {
                    order = Parser.parseXMLOrder(file);
                } catch (IOException e) {
                    // leave null
                }
            }

            final Order fOrder = order;
            Platform.runLater(() -> {
                VBox fileBox = createFileDisplay(fileName, fOrder);
                // insert at top of the orders list
                ordersContainer.getChildren().addFirst(fileBox);
                applyFilters();
            });
        }).start();
    }

    /**
     * Creates a VBox display for a single order
     * show order id, status, and type
     */
    private VBox createFileDisplay(String fileName, Order order) {
        VBox fileBox = new VBox(6);
        fileBox.setPadding(new Insets(10));
        fileBox.setStyle(BASE_BOX_STYLE);

        // top row: Order #id: + status
        HBox topRow = new HBox(8);
        Label orderTitle = new Label();
        orderTitle.setFont(Font.font("System", FontWeight.BOLD, 14));

        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 13));

        // second row: type  and company
        HBox secondRow = new HBox(8);
        Label typeLabel = new Label();
        typeLabel.setFont(Font.font("System", 12));
        Label companyLabel = new Label();
        companyLabel.setFont(Font.font("System", 12));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (order != null) {
            orderTitle.setText("Order #" + order.getOrderID() + ":");

            // status text and color
            String statusText = order.displayStatus();
            statusLabel.setText(statusText);
            statusLabel.setStyle("-fx-text-fill: " + statusColor(order.getStatus()) + ";");

            // type formatting
            String type = String.valueOf(order.displayType());
            String formattedType = formatType(type);
            typeLabel.setText(formattedType);
            typeLabel.setStyle("-fx-text-fill: " + typeColor(formattedType) + "; -fx-font-weight: bold;");

            // determine company from file extension
            String company = fileName.toLowerCase().endsWith(".json") ? "FoodHub" : "GrubStop";
            companyLabel.setText(company);
        } else {
            orderTitle.setText(fileName);
            statusLabel.setText("");
            typeLabel.setText("Parse error");
            typeLabel.setFont(Font.font("System", FontPosture.ITALIC, 12));
            companyLabel.setText("");
        }

        // add details to rows and add rows to the details box on the right
        topRow.getChildren().addAll(orderTitle, statusLabel);
        secondRow.getChildren().addAll(typeLabel, spacer, companyLabel);
        fileBox.getChildren().addAll(topRow, secondRow);

        // click behavior: show details on right pane if parsed
        fileBox.setOnMouseClicked(evt -> {
            selectFileBox(fileBox);
            selectedOrder = order;
            if (order != null) showOrderDetails(order);
            if (cancelButton != null) {
                cancelButton.setDisable(order == null || order.getStatus() == Status.completed);
            }
        });

        return fileBox;
    }

    private void showOrderDetails(Order order) {
        if (detailContainer == null) return;
        detailContainer.getChildren().clear();

        Label header = new Label("Order Details - #" + order.getOrderID());
        header.setFont(Font.font("System", FontWeight.BOLD, 16));

        TextArea details = new TextArea(order.toString());
        details.setEditable(false);
        details.setWrapText(true);
        details.setPrefWidth(300);
        details.setPrefHeight(400);

        detailContainer.getChildren().addAll(header, details);
    }

    //visual indicator for selected file box
    private void selectFileBox(VBox box) {
        if (selectedFileBox != null) {
            selectedFileBox.setStyle(BASE_BOX_STYLE);
        }
        if (box != null) {
            // selected order style around box
            String SELECTED_BOX_STYLE = BASE_BOX_STYLE + " -fx-effect: dropshadow(gaussian, rgba(158,158,158,0.6), 14, 0.5, 0, 0); -fx-border-color: #9e9e9e; -fx-border-width: 1;";
            box.setStyle(SELECTED_BOX_STYLE);
            selectedFileBox = box;
        } else {
            selectedFileBox = null;
        }
    }
    //helper to format order types
    private String formatType(String raw) {
        if (raw == null) return "";
        String t = raw.trim().toLowerCase();
        if (t.equals("togo")){
            return "To-go";
        }
        // capitalize first letter
        if (t.isEmpty()) return t;
        return t.substring(0,1).toUpperCase() + t.substring(1);
    }

    // text color helpers
    private String statusColor(Status status) {
        if (status == null) return "#666666";
        if (status == Status.completed) { return "#2e7d32"; } // green
        if (status == Status.waiting) { return "#fb8c00"; } // orange
        if (status == Status.inProgress) { return "#1565c0"; } // blue
        if (status == Status.cancelled) { return "#c62828"; } // red
        return "#666666";
    }
    private String typeColor(String formattedType) {
        if (formattedType == null) return "#444444";
        String t = formattedType.toLowerCase();
        if (t.contains("to-go")) return "#6a1b9a"; // purple
        if (t.contains("pickup")) return "#2e7d32"; // green
        if (t.contains("delivery")) return "#1565c0"; // blue
        return "#444444";
    }

    private void cancelSelectedOrder() {
        if (selectedOrder == null || orderDriver == null) return;

        // Confirmation message
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Order");
        alert.setHeaderText("Cancel order?");
        alert.setContentText("Order #" + selectedOrder.getOrderID());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (orderDriver.cancelOrderGUI(selectedOrder)) {
                    ordersContainer.getChildren().remove(selectedFileBox);
                    showOrderDetails(selectedOrder);
                    // Enables undo button
                    if (undoButton != null) undoButton.setDisable(false);
                }
            }
            else {
                // Confirmation message, cancel aborted
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Cancellation Aborted");
                info.setHeaderText(null);
                info.setContentText("The order was not cancelled.");
                info.showAndWait();
            }
        });
    }
    private void undoCancel() {
        if (orderDriver == null) return;
        orderDriver.undoCancel();
        if (selectedOrder != null && selectedOrder.getStatus() != Status.cancelled) {
            VBox box = createFileDisplay("Order #" + selectedOrder.getOrderID(), selectedOrder);
            ordersContainer.getChildren().add(0, box);
            if (undoButton != null) undoButton.setDisable(true);
        }
    }

    private void applyFilters() {
        if (ordersContainer == null || orderDriver == null){
            return;
        }

        String selectedStatus;
        if (statusFilter != null) {
            selectedStatus = statusFilter.getValue();
        } else {
            selectedStatus = "All";
        }

        String selectedType;
        if (typeFilter != null) {
            selectedType = typeFilter.getValue();
        } else {
            selectedType = "All";
        }

        ordersContainer.getChildren().clear();

        for (Order order : orderDriver.getOrders()) {
            boolean statusMatch = selectedStatus.equals("All") ||
                    order.getStatus().name().equalsIgnoreCase(selectedStatus);
            boolean typeMatch = selectedType.equals("All") ||
                    formatType(order.getType().name()).equalsIgnoreCase(selectedType);

            if (statusMatch && typeMatch) {
                VBox box = createFileDisplay("Order #" + order.getOrderID(), order);
                ordersContainer.getChildren().add(box);
            }
        }
    }
}