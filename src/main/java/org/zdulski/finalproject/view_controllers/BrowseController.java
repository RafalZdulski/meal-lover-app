package org.zdulski.finalproject.view_controllers;

import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.zdulski.finalproject.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;
import org.zdulski.finalproject.view_auxs.MealCellFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

//TODO IMPORTANT make i parallel, cuz now it operates on main thread
public class BrowseController implements Initializable {
    private List<Meal> meals = new ArrayList<>();
    private final int numberOfMealsDisplayed = 16;
    private int page = 0;

    @FXML
    private ListView<Meal> mealsListView;

    @FXML
    private Text actualPage;

    @FXML
    private Text firstPage;

    @FXML
    private Text lastPage;

    public BrowseController(){
        EventBusFactory.getEventBus().register(this);
    }

    public void setAllMeals() {
        //TODO there is 282 dishes in database, shouldn't it be fetched in parts? I do not need to fetch all 282 dishes at once
        //maybe it would be better to fetch only ids and fully fetch only those meals that are displayed on current page
        CompletableFuture<List<Meal>> allMeals = CompletableFuture.supplyAsync(() -> {
            meals = new MealGetterImpl().getAllMeals();
            return meals;
        }).thenApply( meals1 -> {
            firstPage.setText("1");
            int pages = (int) Math.ceil(meals.size()/ (double) numberOfMealsDisplayed);
            lastPage.setText(String.valueOf(pages));
            return meals1;
        }).thenApply(meals1 -> {
            update();
            return meals1;
        });
    }

    @Subscribe
    public void setMeals(List<Meal> meals){
        new Thread(() -> {
            this.meals = meals;
            int pages = (int) Math.ceil(this.meals.size() / (double) numberOfMealsDisplayed);
            lastPage.setText(String.valueOf(pages));
            update();
        }).start();
    }

    private void update(){
        showMeals();
        showPages();
    }

    private void showPages() {
        //TODO beautify it, and make it that you can go to specified page by clicking on it
        //int pages = (int) Math.ceil(meals.size()/ (double) numberOfMealsDisplayed);
        actualPage.setText(String.valueOf(page+1));
    }

    public void showMeals(){
        //TODO IMPORTANT OPTIMIZATION refactor it so it would only load dishes only from actual page not all
        int from = page * numberOfMealsDisplayed;
        int to = Math.min(from + numberOfMealsDisplayed, meals.size());
        ObservableList<Meal> mealDisplayed = FXCollections.observableList(meals.subList(from,to));
        mealsListView.setItems(FXCollections.observableList(mealDisplayed));
        mealsListView.setCellFactory(new MealCellFactory());
    }


    @FXML
    public void nextPage(){
        page = Math.min(page+1,meals.size()/numberOfMealsDisplayed);
        mealsListView.scrollTo(0);
        update();
    }

    @FXML
    public void previousPage(){
        page = Math.max(page-1,0);
        mealsListView.scrollTo(0);
        update();
    }

    public void goToPage(int page){
        this.page = page;
        mealsListView.scrollTo(0);
        update();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
