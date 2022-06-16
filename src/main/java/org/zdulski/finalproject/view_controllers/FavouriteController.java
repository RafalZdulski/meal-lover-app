package org.zdulski.finalproject.view_controllers;

import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.eventbus.*;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;
import org.zdulski.finalproject.view_auxs.MealCellFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class FavouriteController implements MealsController {
    private List<Meal> meals = new ArrayList<>();

    @FXML
    private ListView<Meal> mealsListView;

    @FXML
    private VBox emptyInfoBox;

    public FavouriteController(){
        EventBusFactory.getEventBus().register(this);
    }

    public void setMeals(List<Meal> meals){
        if (meals.isEmpty()){
            emptyInfoBox.setVisible(true);
            return;
        }
        emptyInfoBox.setVisible(false);
        this.meals = meals;
        //this.meals.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        update();
    }

    public void setMealsByIds(Collection<String> ids){
        if (ids.isEmpty()){
            emptyInfoBox.setVisible(true);
            return;
        }
        emptyInfoBox.setVisible(false);
        meals = new ArrayList<>();
        CompletableFuture.runAsync(()-> {
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
            update();
            // meals = new MealGetterImpl().getMealsByIds(ids);
            EventBusFactory.getEventBus().post(new LoadingFinishedEvent());
        });
    }

    @Override
    public void onEntering() {

    }

    @Override
    public void update(){
        showMeals();
    }

    @Override
    public void onLeaving() {

    }

    public void showMeals(){
        ObservableList<Meal> mealDisplayed = FXCollections.observableList(meals);
        mealsListView.setItems(FXCollections.observableList(mealDisplayed));
        mealsListView.setCellFactory(new MealCellFactory(View.FAVOURITE));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Subscribe
    public void goToNextMeal(NextMealEvent event){
        if (event.getSourceView() != View.FAVOURITE)
            return;
        int i = meals.indexOf(event.getMeal());
        if (++i >= meals.size())
            //TODO ADD should also disable right action button in meal view
            return;
        Meal meal = meals.get(i);
        EventBusFactory.getEventBus().post(new ShowMealEvent(meal, MealController.Action.NEXT_MEAL, View.FAVOURITE));
    }
}
