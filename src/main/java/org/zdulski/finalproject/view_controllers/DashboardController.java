package org.zdulski.finalproject.view_controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.zdulski.finalproject.data.dto.UserProxy;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements ViewController{

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
    }
}
