package org.zdulski.finalproject.view_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.zdulski.finalproject.dto.Meal;
import org.zdulski.finalproject.view_auxs.MealCellFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LatestController implements MealsController {
    private List<Meal> meals = new ArrayList<>();

    @FXML
    private ListView<Meal> mealsListView;

    public LatestController(){
    }

    public void setMeals(List<Meal> meals){
        new Thread(() -> {
            this.meals = meals;
            update();
        }).start();
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
        mealsListView.setCellFactory(new MealCellFactory());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
