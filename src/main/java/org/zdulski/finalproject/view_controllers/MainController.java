package org.zdulski.finalproject.view_controllers;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXDrawer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdulski.finalproject.eventbus.*;
import org.zdulski.finalproject.view_auxs.loading.LoadingSlider;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class MainController implements Initializable {

    private final static Logger LOG = LogManager.getLogger(MainController.class);

    @FXML
    protected BorderPane mainPane;

    private List<Node> recentlyViewedPanes;

    private Pane latestBrowseView;

    private Pane getInspiredView;

    @FXML
    private AnchorPane loadingPane;

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

        try {
            Pane dashboardPane = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource("/org/zdulski/finalproject/views/dashboard-view.fxml")));
            this.setCenterView(dashboardPane);
            dashboardPane.getChildren().forEach(el -> el.setOnMouseClicked(e -> menuDrawer.open()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoadingSlider slider = new LoadingSlider(360,6, 120,2500,2000);
        slider.play();
        loadingPane.getChildren().add(slider);
        closeLoadingSlider();
    }

    public void search(){
        if (searchDrawer.isClosed()){
            searchDrawer.open();
        }else
            searchDrawer.close();
    }

    public void setCenterView(Pane pane) {
        recentlyViewedPanes.add(mainPane.getCenter());
        Platform.runLater(()->mainPane.setCenter(pane));
    }

    public void goToPreviousView(){
        if (recentlyViewedPanes.size() < 2)
            return;
        mainPane.setCenter(recentlyViewedPanes.get(recentlyViewedPanes.size()-1));
        recentlyViewedPanes.remove(recentlyViewedPanes.size()-1);
        //TODO ADD refreshing pane after switching to it
    }

    @Subscribe
    public void deadEventHandler(DeadEvent event){
        LOG.warn("dead event: " + event.getEvent().toString());
    }

    @Subscribe
    public void showMeal(ShowMealEvent event){
        CompletableFuture.supplyAsync(
                () -> new FXMLLoader(getClass().getResource(View.MEAL.getUrl()))
        ).thenApplyAsync(loader -> {
            try {
                Pane pane = loader.load();
                this.setCenterView(pane);
                menuDrawer.close();
                searchDrawer.close();
                MealController controller = loader.getController();
                controller.setMeal(event.getMeal());
                controller.setSourceView(event.getSourceView());
                controller.setRightActionBtn(event.getAction());
            } catch (IOException e) {
                LOG.error("couldn't show meal: " + event.getMeal().getName());
                e.printStackTrace();
            }
            LOG.info("showing meal: " + event.getMeal().getName());
            return loader;
        }).thenApplyAsync(loader -> {
            setHeader(View.MEAL);
            return loader;
        });
    }

    @Subscribe
    public void showMeals(ShowMealsEvent event){
        this.openLoadingSlider();
        if (event.getMeals() == null && latestBrowseView != null) {
            this.setCenterView(latestBrowseView);
            menuDrawer.close();
            searchDrawer.close();
            this.setHeader(event.getViewType());
            this.closeLoadingSlider();
            return;
        }

        String url = event.getViewType().getUrl();
        CompletableFuture.supplyAsync(
                () -> new FXMLLoader(getClass().getResource(url))
        ).thenApplyAsync(loader -> {
            try {
                Pane pane = loader.load();
                if (event.getViewType() == View.BROWSE)
                    latestBrowseView = pane;
                this.setCenterView(pane);
                menuDrawer.close();
                searchDrawer.close();
                MealsController controller = loader.getController();
                controller.setMeals(event.getMeals());
            } catch (IOException e) {
                LOG.error("couldn't show meals, as" + event.getViewType());
                e.printStackTrace();
            }
            LOG.info("showing " + event.getMeals().size() + " meals, as: " + event.getViewType());
            return loader;
        }).thenApplyAsync(loader -> {
            setHeader(event.getViewType());
            return loader;
        });
    }

    @Subscribe
    public void showMealsByIds(ShowMealsByIdsEvent event){
        this.openLoadingSlider();

        String url = event.getViewType().getUrl();
        CompletableFuture.supplyAsync(
                () -> new FXMLLoader(getClass().getResource(url))
        ).thenApplyAsync(loader -> {
            try {
                Pane pane = loader.load();
                if (event.getViewType() == View.BROWSE)
                    latestBrowseView = pane;
                this.setCenterView(pane);
                menuDrawer.close();
                searchDrawer.close();
                MealsController controller = loader.getController();
                controller.setMealsByIds(event.getIds());
            } catch (IOException e) {
                LOG.error("couldn't show meals, as" + event.getViewType());
                e.printStackTrace();
            }
            LOG.info("showing " + event.getIds().size() + " meals, as: " + event.getViewType());
            return loader;
        }).thenApplyAsync(loader -> {
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
        //TODO FIX distinguish between closing drawers and going to previous view
        if (searchDrawer.isOpened())
            searchDrawer.close();
        else if (menuDrawer.isOpened())
            menuDrawer.close();
        else
            this.goToPreviousView();
    }

    @Subscribe
    public void changeView(ChangeViewEvent event){
        if (event.getView() == View.BY_INGREDIENTS && getInspiredView != null) {
            setCenterView(getInspiredView);
            setHeader(View.BY_INGREDIENTS);
            menuDrawer.close();
            searchDrawer.close();
            return;
        }

        String url = event.getView().getUrl();
        CompletableFuture.supplyAsync(
                () -> new FXMLLoader(getClass().getResource(url))
        ).thenApplyAsync(loader -> {
            try {
                Pane pane = loader.load();
                this.setCenterView(pane);
                if (event.getView() == View.BY_INGREDIENTS)
                    getInspiredView = pane;
                menuDrawer.close();
                searchDrawer.close();
            } catch (IOException e) {
                LOG.error("couldn't load" + event.getView());
                e.printStackTrace();
            }
            LOG.info("showing " + event.getView());
            return loader;
        }).thenApplyAsync(loader -> {
            setHeader(event.getView());
            return loader;
        });
    }

    private void setHeader(View view){
        String str;
        int size = 30;
        switch (view) {
            case BROWSE -> str = "Today's Craving For?";
            case LATEST -> str = "Recently Viewed";
            case FAVOURITE -> str = "Your Favourites";
            case BY_INGREDIENTS -> str = "What's in Your Fridge";
            case RECOMMENDED -> str = "We Recommend You";
            default -> {
                str = "Food Lover";
                size = 36;
            }
        }
        header.setText(str);
        header.setFont(Font.font("Pristina", FontWeight.BOLD, size));
    }


    private void openLoadingSlider(){
        CompletableFuture.runAsync(() -> {
            KeyValue scaleY = new KeyValue(loadingPane.getChildren().get(0).scaleYProperty(), 1);
            KeyValue layoutY = new KeyValue(loadingPane.getChildren().get(0).layoutYProperty(), 0);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500), scaleY, layoutY);
            Timeline open = new Timeline(keyFrame);
            open.play();
            //views load too fast for this to be noticeable, so I made it artificial
//            try {
//                TimeUnit.MILLISECONDS.sleep(1500);
//                closeLoadingSlider();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        });
    }

    private void closeLoadingSlider(){
        KeyValue scaleY = new KeyValue(loadingPane.getChildren().get(0).scaleYProperty(), 0);
        KeyValue layoutY = new KeyValue(loadingPane.getChildren().get(0).layoutYProperty(), -3);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), scaleY, layoutY);
        Timeline close = new Timeline(keyFrame);
        close.play();
    }

    @Subscribe
    public void finishedLoading(LoadingFinishedEvent event){
        closeLoadingSlider();
    }
}