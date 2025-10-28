module org.metrostate.ics.ordertrackingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens org.metrostate.ics.ordertrackingapp to javafx.fxml;
    exports org.metrostate.ics.ordertrackingapp;
}