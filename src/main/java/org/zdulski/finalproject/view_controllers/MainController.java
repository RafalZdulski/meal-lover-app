package org.zdulski.finalproject.view_controllers;

import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.zdulski.finalproject.mediators.MainMediator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    //TODO add magnifying glass icon next to burger and add search by area, category, (name?) functionality

    @FXML
    protected BorderPane mainPane;

    private Node latestPane;

    @FXML
    protected JFXDrawer menuDrawer;

    @FXML
    protected JFXDrawer searchDrawer;

    @FXML
    protected ImageView navbarLogo;

    @FXML
    protected ImageView menuBtnIcon;

    @FXML
    protected ImageView searchBtnIcon;

    @FXML
    protected void openDrawer() {
        if (menuDrawer.isClosed()){
            menuDrawer.open();
        }else
            menuDrawer.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainMediator.getInstance().setController(this);
        try {
            //relative path does not work - I do not know exactly why
            navbarLogo.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/images/food-lover-logo.png")));
            menuBtnIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/icons/menu-icon.png")));
            searchBtnIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/icons/search-icon.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Pane menuDrawerContent = FXMLLoader.load(getClass()
                    .getResource("/org/zdulski/finalproject/views/side-menu-view.fxml"));
            menuDrawer.setSidePane(menuDrawerContent);
            menuDrawer.setDefaultDrawerSize(200);

            //closed drawer was overlaying views underneath making impossible to click things, this the solution that seems to work
            menuDrawer.setVisible(false);
            menuDrawer.setOnDrawerOpening(event -> menuDrawer.setVisible(true));
            menuDrawer.setOnDrawerClosed(event -> menuDrawer.setVisible(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Pane searchDrawerContent = FXMLLoader.load(getClass()
                    .getResource("/org/zdulski/finalproject/views/search-view.fxml"));
            searchDrawer.setSidePane(searchDrawerContent);
            searchDrawer.setDefaultDrawerSize(270);

            //closed drawer was overlaying views underneath making impossible to click things, this the solution that seems to work
            searchDrawer.setVisible(false);
            searchDrawer.setOnDrawerOpening(event -> searchDrawer.setVisible(true));
            searchDrawer.setOnDrawerClosed(event -> searchDrawer.setVisible(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void search(){
        if (searchDrawer.isClosed()){
            searchDrawer.open();
        }else
            searchDrawer.close();
    }

    public void setCenterView(Pane pane) {
        latestPane = mainPane.getCenter();
        mainPane.setCenter(pane);
    }

    public void setLatestPaneAsMain(){
        mainPane.setCenter(latestPane);
    }
}