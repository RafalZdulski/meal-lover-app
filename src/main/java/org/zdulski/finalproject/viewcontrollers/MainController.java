package org.zdulski.finalproject.viewcontrollers;

import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    protected BorderPane mainPane;

    @FXML
    protected JFXDrawer drawer;

    @FXML
    protected void onMainMenuBtnClicked() {
        if (drawer.isClosed()){
            drawer.open();
        }else
            drawer.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Pane drawerContent = FXMLLoader.load(getClass()
                    .getResource("/org/zdulski/finalproject/views/side-menu-view.fxml"));
            drawer.setSidePane(drawerContent);
            drawer.setDefaultDrawerSize(200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}