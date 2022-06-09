package org.zdulski.finalproject.view_controllers;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXDrawer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.zdulski.finalproject.eventbus.*;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class MainController implements Initializable {

    @FXML
    protected BorderPane mainPane;

    private List<Node> recentlyViewedPanes;

    private Pane latestBrowseView;

    @FXML
    protected JFXDrawer menuDrawer;

    @FXML
    protected JFXDrawer searchDrawer;

    @FXML
    protected ImageView navbarLogo;

    @FXML
    protected ImageView menuBtnIcon;

    @FXML
    protected Text header;

    @FXML
    protected void openDrawer() {
        if (menuDrawer.isClosed()){
            menuDrawer.open();
        }else
            menuDrawer.close();
    }

    public MainController(){
        this.recentlyViewedPanes = new LinkedList<>();
        EventBusFactory.getEventBus().register(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navbarLogo.setImage(new Image(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/images/food-lover-logo.png"));
        menuBtnIcon.setImage(new Image(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/icons/burger-icon.png"));
        menuBtnIcon.setFitHeight(42);
        menuBtnIcon.setFitWidth(42);
        try {
            Pane menuDrawerContent = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource("/org/zdulski/finalproject/views/side-menu-view.fxml")));
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
            Pane searchDrawerContent = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource("/org/zdulski/finalproject/views/search-view.fxml")));
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
        recentlyViewedPanes.add(mainPane.getCenter());
        mainPane.setCenter(pane);
    }

    public void goToPreviousView(){
        if (recentlyViewedPanes.size() < 2)
            return;
        mainPane.setCenter(recentlyViewedPanes.get(recentlyViewedPanes.size()-1));
        recentlyViewedPanes.remove(recentlyViewedPanes.size()-1);
    }

    @Subscribe
    public void deadEventHandler(DeadEvent event){
        System.out.println("dead event:");
        System.out.println(event.getEvent().toString());
    }

    @Subscribe
    public void showMeal(ShowMealEvent event){
        System.out.println("showing meal: " + event.getMeal().getName());
        CompletableFuture<FXMLLoader> future = CompletableFuture.supplyAsync(
                () -> new FXMLLoader(getClass().getResource(View.MEAL.getUrl()))
        ).thenApply(loader -> {
            try {
                Pane pane = loader.load();
                Platform.runLater(() -> {
                    this.setCenterView(pane);
                });
                menuDrawer.close();
                MealController controller = loader.getController();
                controller.setMeal(event.getMeal());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return loader;
        }).thenApply(loader -> {
            setHeader(View.MEAL);
            return loader;
        });
    }

    @Subscribe
    public void showMeals(ShowMealsEvent event){
        if (event.getMeals() == null && latestBrowseView != null) {
            this.setCenterView(latestBrowseView);
            menuDrawer.close();
            return;
        }

        String url = event.getViewType().getUrl();
        CompletableFuture<FXMLLoader> future = CompletableFuture.supplyAsync(
                () -> new FXMLLoader(getClass().getResource(url))
        ).thenApply(loader -> {
            try {
                Pane pane = loader.load();
                if (event.getViewType() == View.BROWSE)
                    latestBrowseView = pane;
                Platform.runLater(() -> {
                    this.setCenterView(pane);
                });
                menuDrawer.close();
                MealsController controller = loader.getController();
                controller.setMeals(event.getMeals());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return loader;
        }).thenApply(loader -> {
            setHeader(event.getViewType());
            return loader;
        });
    }

    @Subscribe
    public void openDrawer(OpenSearchDrawerEvent event){
        search();
    }

    @Subscribe
    public void goBackToLastView(ReturnEvent event){
        if (searchDrawer.isOpened())
            searchDrawer.close();
        else if (menuDrawer.isOpened())
            menuDrawer.close();
        else
            goToPreviousView();
    }

    private void setHeader(View view){
        String str;
        int size = 30;
        switch (view){
            case BROWSE:
                str = "Today's Craving For?";
                break;
            case LATEST:
                str = "Recently Viewed";
                break;
            case FAVOURITE:
                str = "Your Favourites";
                break;
            default:
                str = "Food Lover";
                size = 36;
        }
        header.setText(str);
        header.setFont(Font.font("Pristina", FontWeight.BOLD, size));
    }
}