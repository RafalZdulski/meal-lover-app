package org.zdulski.finalproject.view_controllers;

import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.zdulski.finalproject.mediator_controllers.ViewMediator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    protected BorderPane mainPane;

    @FXML
    protected JFXDrawer drawer;

    @FXML
    protected ImageView navbarLogo;

    @FXML
    protected void onMainMenuBtnClicked() {
        if (drawer.isClosed()){
            drawer.open();
        }else
            drawer.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewMediator.getInstance().setMainPane(mainPane);
        try {
            //relative path does not work - I do not know exactly why
            navbarLogo.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/images/food-lover-logo.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Pane drawerContent = FXMLLoader.load(getClass()
                    .getResource("/org/zdulski/finalproject/views/side-menu-view.fxml"));
            drawer.setSidePane(drawerContent);
            drawer.setDefaultDrawerSize(200);
            //closed drawer was overlaying views underneath making impossible to click things
            //this the solution that seems to work
            drawer.setOnDrawerOpening(event -> drawer.setVisible(true));
            drawer.setOnDrawerClosed(event -> drawer.setVisible(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}