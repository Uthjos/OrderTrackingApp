module org.metrostate.ics.ordertrackingapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.metrostate.ics.ordertrackingapp to javafx.fxml;
    exports org.metrostate.ics.ordertrackingapp;
}