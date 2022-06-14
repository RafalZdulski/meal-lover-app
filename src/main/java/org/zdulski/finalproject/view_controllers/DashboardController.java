package org.zdulski.finalproject.view_controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.zdulski.finalproject.data.dto.UserProxy;
import org.zdulski.finalproject.view_auxs.loading.LoadingCircles;
import org.zdulski.finalproject.view_auxs.loading.LoadingSlider;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements ViewController{

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Text username;

    @Override
    public void onEntering() {

    }

    @Override
    public void update() {

    }

    @Override
    public void onLeaving() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(UserProxy.getInstance().getUsername());

        Text text = new Text("Meanwhile, take a second and look at this cool loading animations:");
        text.setFont(Font.font("Segoe Print", 16));
        text.setWrappingWidth(200);
        text.setX(10);
        text.setY(470);
        mainPane.getChildren().add(text);

        //sliding bar loading animation
        LoadingSlider bar = new LoadingSlider(360,20, 120, 3000, 2000);
        bar.setLayoutX(0);
        bar.setLayoutY(565);
        mainPane.getChildren().add(bar);
        bar.play();

        //circle loading animation
        double radius = 35;
        int amountOfCircles = 9;
        double circlesRadius = Math.PI*radius/amountOfCircles-3;
        LoadingCircles circles = new LoadingCircles(radius, amountOfCircles, circlesRadius,
                2000, 4000);
        circles.setLayoutX(290-radius-circlesRadius);
        circles.setLayoutY(450);
        mainPane.getChildren().add(circles);
        circles.play();
    }
}
