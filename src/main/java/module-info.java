module org.zdulski.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires json.simple;
    requires lombok;


    opens org.zdulski.finalproject to javafx.fxml;
    exports org.zdulski.finalproject;
    exports org.zdulski.finalproject.view_controllers;
    opens org.zdulski.finalproject.view_controllers to javafx.fxml;
}