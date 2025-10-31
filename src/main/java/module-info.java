module org.metrostate.ics.ordertrackingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.xml;


    opens org.metrostate.ics.ordertrackingapp to javafx.fxml;
    exports org.metrostate.ics.ordertrackingapp;
}