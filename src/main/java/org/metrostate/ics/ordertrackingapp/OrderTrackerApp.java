package org.metrostate.ics.ordertrackingapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Main application class for the Order Tracking System.
 * Sets up the JavaFX application and starts the OrderListener to monitor the testOrders directory.
 */
public class OrderTrackerApp extends Application {
    private OrderListener orderListener;

    @Override
    public void start(Stage stage) throws IOException {
        // try to find the FXML file, if we run with coverage it looks through a different path
        // so it has to be found more explicitly
        java.net.URL fxmlUrl = OrderTrackerApp.class.getResource("order-tracker-view.fxml"); //regularly
        if (fxmlUrl == null) { //with coverage
            fxmlUrl = Thread.currentThread().getContextClassLoader()
                    .getResource("org/metrostate/ics/ordertrackingapp/order-tracker-view.fxml");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        OrderTrackerController controller = fxmlLoader.getController();

        OrderDriver driver = new OrderDriver();
        controller.setOrderDriver(driver);

        // watch the testOrders directory
        String testOrdersPath = Directory.getDirectory(Directory.testOrders);
        orderListener = new OrderListener(testOrdersPath, controller::addOrderFile);

        controller.setOrderListener(orderListener);

        orderListener.start();

        stage.setTitle("Order Tracking System");
        stage.setScene(scene);
        stage.show();

        // stop the listener when the application closes
        stage.setOnCloseRequest(event -> {
            if (orderListener != null) {
                orderListener.stop();
            }
        });
    }


}
