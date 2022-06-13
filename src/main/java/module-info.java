module org.zdulski.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires json.simple;
    requires lombok;
    requires com.google.common;
    requires java.desktop;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens org.zdulski.finalproject to javafx.fxml;
    exports org.zdulski.finalproject;
    exports org.zdulski.finalproject.view_controllers;
    opens org.zdulski.finalproject.view_controllers to javafx.fxml;
    exports org.zdulski.finalproject.eventbus;
    exports org.zdulski.finalproject.data.dto;
    exports org.zdulski.finalproject.data.pojos;

    opens org.zdulski.finalproject.data.pojos;
}