package org.zdulski.finalproject.view_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.zdulski.finalproject.config.PropertyManager;
import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.data.dto.UserProxy;
import org.zdulski.finalproject.eventbus.ChangeViewEvent;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ShowMealEvent;
import org.zdulski.finalproject.eventbus.ShowMealsEvent;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class DrawerController implements Initializable {
    @FXML
    private ImageView drawerLogo;

    @FXML
    private ImageView drawerFiller;

    @FXML
    private Button randomBtn;

    @FXML
    private Button browseBtn;

    @FXML
    private Button favouriteBtn;

    @FXML
    private Button latestBtn;

    @FXML
    private Button getInspiredBtn;

    @FXML
    private Button exitBtn;

    @FXML
    public void onRandomClick(){
        CompletableFuture.runAsync(() -> {
            Meal meal = new MealGetterImpl().getRandomMeal();
            EventBusFactory.getEventBus().post(new ShowMealEvent(meal, MealController.Action.RANDOM_MEAL, View.MEAL));
        });
    }

    @FXML
    public void onBrowseClick(){
        //List<Meal> meals = new MealGetterImpl().getAllMeals();
        EventBusFactory.getEventBus().post(new ShowMealsEvent(null, View.BROWSE));
    }

    @FXML
    public void onFavouriteClick(){

        //for now this simple multi threading is enough but
        //TODO FIX move fetching meal details form drawer to favourite controller
        List<Meal> meals = new ArrayList<>();
        CompletableFuture.runAsync(()-> {
            Set<String> ids = UserProxy.getInstance().getFavourites();
            CountDownLatch latch = new CountDownLatch(ids.size());
            ids.forEach(id -> {
                new Thread(() -> {
                    meals.add(new MealGetterImpl().getMealById(id));
                    latch.countDown();
                }).start();
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // meals = new MealGetterImpl().getMealsByIds(ids);
            EventBusFactory.getEventBus().post(new ShowMealsEvent(meals, View.FAVOURITE));
        });
    }

    @FXML
    public void onLastViewedClick(){
        CompletableFuture.runAsync(() -> {
            List<String> ids = UserProxy.getInstance().getLatest();
            List<Meal> meals = new MealGetterImpl().getMealsByIds(ids);
            EventBusFactory.getEventBus().post(new ShowMealsEvent(meals, View.LATEST));
        });
    }

    @FXML
    public void onInspirationClick(){
        EventBusFactory.getEventBus().post(new ChangeViewEvent(View.BY_INGREDIENTS));
    }

    @FXML
    public void onExitClick(){
       System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO IMPROVE drawerFiller should be filling empty space between drawer options (now its dimensions are calculated by hand)
        drawerFiller.setImage(new Image(System.getProperty("user.dir")
                + "/src/main/resources/org/zdulski/finalproject/images/drawer-food-filler.jpg"));
        drawerLogo.setImage(new Image(System.getProperty("user.dir")
                + "/src/main/resources/org/zdulski/finalproject/images/food-lover-red-banner.png"));
        randomBtn.setGraphic(getMenuDrawerIcon("dice.png"));
        browseBtn.setGraphic(getMenuDrawerIcon("listing-icon.png"));
        favouriteBtn.setGraphic(getMenuDrawerIcon("favorite.png"));
        latestBtn.setGraphic(getMenuDrawerIcon("clock-icon.png"));
        getInspiredBtn.setGraphic(getMenuDrawerIcon("fork-and-knife-icon.png"));
        exitBtn.setGraphic(getMenuDrawerIcon("exit.png"));
    }

    private ImageView getMenuDrawerIcon(String name){
        Image image = new Image(System.getProperty("user.dir") + "/"
                + PropertyManager.getInstance().getProperty("iconsFolder") + "/" + name);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(28);
        imageView.setFitHeight(28);
        imageView.setSmooth(true);
        imageView.setOpacity(0.80);
        return imageView;
    }

}
