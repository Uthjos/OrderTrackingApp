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
        FXMLLoader fxmlLoader = new FXMLLoader(OrderTrackerApp.class.getResource("order-tracker-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        OrderTrackerController controller = fxmlLoader.getController();

        OrderDriver driver = new OrderDriver();
        controller.setOrderDriver(driver);

        // watch the testOrders directory
        String testOrdersPath = getTestOrdersPath();
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

    /**
     * Gets the path to the testOrders directory
     */
    private String getTestOrdersPath() {
        String projectPath = System.getProperty("user.dir");
        String testOrdersPath = Paths.get(projectPath, "src", "main", "testOrders").toString();

        File testOrdersDir = new File(testOrdersPath);
        if (!testOrdersDir.exists()) {
            testOrdersDir.mkdirs();
        }
        return testOrdersPath;
    }
}
