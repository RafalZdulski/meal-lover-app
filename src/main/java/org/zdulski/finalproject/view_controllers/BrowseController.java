package org.zdulski.finalproject.view_controllers;

import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.zdulski.finalproject.config.PropertyManager;
import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.NextMealEvent;
import org.zdulski.finalproject.eventbus.OpenSearchDrawerEvent;
import org.zdulski.finalproject.eventbus.ShowMealEvent;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;
import org.zdulski.finalproject.view_auxs.MealCellFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class BrowseController implements MealsController {
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

    @FXML
    private Button searchBtn;

    @FXML
    private Button nextPageBtn;

    @FXML
    private Button previousPageBtn;

    public BrowseController(){
        EventBusFactory.getEventBus().register(this);
    }

    public void setAllMeals() {
        //TODO IMPROVE there is 282 dishes in database, shouldn't it be fetched in parts? I do not need to fetch all 282 dishes at once
        //maybe it would be better to fetch only ids and fully fetch only those meals that are displayed on current page
        CompletableFuture.runAsync(() -> {
            meals = new MealGetterImpl().getAllMeals();
        }).thenRunAsync( () -> {
            int pages = (int) Math.ceil(meals.size()/ (double) numberOfMealsDisplayed);
            lastPage.setText(String.valueOf(pages));
        }).thenRunAsync(this::update);
    }

    public void setMeals(List<Meal> meals){
        CompletableFuture.runAsync(() -> {
            if (meals != null) {
                this.meals = meals;
                int pages = (int) Math.ceil(this.meals.size() / (double) numberOfMealsDisplayed);
                lastPage.setText(String.valueOf(pages));
                update();
            }
        }).thenRunAsync(() -> {
            if (this.meals.isEmpty()){
                setAllMeals();
                int pages = (int) Math.ceil(this.meals.size() / (double) numberOfMealsDisplayed);
                lastPage.setText(String.valueOf(pages));
                update();
            }
        });
    }

    @Override
    public void onEntering() {

    }

    @Override
    public void update(){
        showMeals();
        showPages();
    }

    @Override
    public void onLeaving() {

    }

    private void showPages() {
        actualPage.setText(String.valueOf(page+1));
    }

    public void showMeals(){
        CompletableFuture.runAsync(() -> {
            int from = page * numberOfMealsDisplayed;
            int to = Math.min(from + numberOfMealsDisplayed, meals.size());
            ObservableList<Meal> mealDisplayed = FXCollections.observableList(meals.subList(from, to));
            mealsListView.setItems(FXCollections.observableList(mealDisplayed));
            //mealsListView.setCellFactory(new MealCellFactory());
        });
    }


    @FXML
    public void nextPage(){
        page = Math.min(page+1, meals.size()/numberOfMealsDisplayed);
        if (page*numberOfMealsDisplayed >= meals.size()){
            page--;
            return;
        }
        mealsListView.scrollTo(0);
        update();
    }

    @FXML
    public void previousPage(){
        int previousPage = page;
        page = Math.max(page-1,0);
        if (previousPage == page)
            return;
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
        mealsListView.setCellFactory(new MealCellFactory(View.BROWSE));

        String iconsFolder = System.getProperty("user.dir") + "/" +
                PropertyManager.getInstance().getProperty("iconsFolder") + "/" ;
        ImageView imageView;

        imageView = new ImageView(iconsFolder + "magnifying-glass-icon.png");
        imageView.setFitWidth(32); imageView.setFitHeight(32);
        searchBtn.setGraphic(imageView);

        imageView = new ImageView(iconsFolder + "next-page-icon.png");
        imageView.setFitWidth(24); imageView.setFitHeight(24);
        nextPageBtn.setGraphic(imageView);

        imageView = new ImageView(iconsFolder + "previous-page-icon.png");
        imageView.setFitWidth(24); imageView.setFitHeight(24);
        previousPageBtn.setGraphic(imageView);
    }

    @FXML
    public void searchBtnClicked(){
        EventBusFactory.getEventBus().post(new OpenSearchDrawerEvent());
    }

    @Subscribe
    public void goToNextMeal(NextMealEvent event){
        if (event.getSourceView() != View.BROWSE)
            return;
        int i = meals.indexOf(event.getMeal());
        if (++i >= meals.size())
            //TODO ADD should also disable right action button in meal view
            return;
        Meal meal = meals.get(i);
        EventBusFactory.getEventBus().post(new ShowMealEvent(meal, MealController.Action.NEXT_MEAL, View.BROWSE));
    }
}
