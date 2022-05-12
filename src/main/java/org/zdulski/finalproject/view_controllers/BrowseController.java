package org.zdulski.finalproject.view_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.zdulski.finalproject.mealdb.ApiController;
import org.zdulski.finalproject.mealdb.dto.Meal;
import org.zdulski.finalproject.view_auxs.MealCellFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

//TODO IMPORTANT make i parallel, cuz now it operates on main thread
public class BrowseController {
    private List<Meal> meals = new ArrayList<>();
    private final int numberOfMealsDisplayed = 12;
    private int page = 0;

    @FXML
    private ListView mealsListView;

    @FXML
    private Text actualPage;

    @FXML
    private Text firstPage;

    @FXML
    private Text lastPage;

    public void getAllMeals() {
        //TODO there is 282 dishes in database, shouldn't it be fetched in parts?
        CountDownLatch latch = new CountDownLatch('z'-'a'+1);
        for (char letter = 'a'; letter <= 'z'; letter++){
            char finalLetter = letter;
            new Thread(() ->{
                meals.addAll(new ApiController().getMealsByFirstLetter(finalLetter));
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("meals.size = " + meals.size());

        firstPage.setText("0");
        int pages = (int) Math.ceil(meals.size()/ (double) numberOfMealsDisplayed);
        lastPage.setText(String.valueOf(pages));

        update();
    }

    private void update(){
        showMeals();
        showPages();
    }

    private void showPages() {
        //TODO beautify it, and make it that you can go to specified page by clicking on it
        //int pages = (int) Math.ceil(meals.size()/ (double) numberOfMealsDisplayed);
        actualPage.setText(String.valueOf(page));
    }

    public void showMeals(){
        int from = page * numberOfMealsDisplayed;
        int to = Math.min(from + numberOfMealsDisplayed, meals.size());
        ObservableList mealDisplayed = FXCollections.observableList(meals.subList(from,to));
        mealsListView.setItems(FXCollections.observableList(mealDisplayed));
        mealsListView.setCellFactory(new MealCellFactory());
    }

    @FXML
    public void nextPage(){
        page = Math.min(page+1,meals.size()/numberOfMealsDisplayed);
        update();
    }

    @FXML
    public void previousPage(){
        page = Math.max(page-1,0);
        update();
    }

    public void goToPage(int page){
        this.page = page;
        update();
    }
}
