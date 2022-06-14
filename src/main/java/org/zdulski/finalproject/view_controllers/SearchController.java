package org.zdulski.finalproject.view_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.zdulski.finalproject.config.PropertyManager;
import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ReturnEvent;
import org.zdulski.finalproject.eventbus.ShowMealsEvent;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;
import org.zdulski.finalproject.mealdbAPI.SearchEngineImpl;
import org.zdulski.finalproject.view_auxs.loading.LoadingCircles;
import org.zdulski.finalproject.view_auxs.search.filters.FilterPillsDisplay;
import org.zdulski.finalproject.view_auxs.search.filters.FilterWindow;
import org.zdulski.finalproject.view_auxs.search.filters.FilterWrap;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SearchController implements Initializable {

    private List<FilterWrap> areas;

    private List<FilterWrap> categories;

    private LoadingCircles loadingCircles;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button addAreaBtn;

    @FXML
    private Button addCategoryBtn;

    @FXML
    private Button clearAllBtn;

    @FXML
    private Button clearAreaBtn;

    @FXML
    private Button clearCategoryBtn;

    @FXML
    private Button clearNameBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField nameField;

    @FXML
    private VBox filterPillsVBox;

    @FXML
    private Button returnBtn;

    @FXML
    void addArea() {
        new FilterWindow(areas);
    }

    @FXML
    void addCategory() {
        new FilterWindow(categories);
    }

    @FXML
    void clearAll() {
        clearName();
        clearAreas();
        clearCategories();
    }

    @FXML
    void clearAreas() {
        areas.forEach(f -> f.setCheck(false));
    }

    @FXML
    void clearCategories() {
        categories.forEach(f -> f.setCheck(false));
    }

    @FXML
    void clearName() {
        nameField.clear();
    }

    @FXML
    void searchBtnClicked() {
        loadingCircles.setVisible(true);
        final String[] wordsFilters = nameField.getText().length()==0? new String[0] : nameField.getText().split("\\s+");
        final String[] areasFilter = areas.stream().filter(FilterWrap::getCheckValue).map(FilterWrap::toString).toArray(String[]::new);
        final String[] categoryFilters = categories.stream().filter(FilterWrap::getCheckValue).map(FilterWrap::toString).toArray(String[]::new);

        //get all meals when filtered by empty filters
        if (wordsFilters.length + areasFilter.length + categoryFilters.length < 1) {
            CompletableFuture.runAsync(()-> {
                List<Meal> meals = new MealGetterImpl().getAllMeals();
                EventBusFactory.getEventBus().post(new ShowMealsEvent(meals, View.BROWSE));
            });
            return;
        }

        CompletableFuture.supplyAsync(() -> {
            Set<String> ids = new SearchEngineImpl().getIDs(wordsFilters, areasFilter, categoryFilters);
            return ids;
        }).thenApply(ids -> {
            if (!ids.isEmpty()) {
                List<Meal> meals = new MealGetterImpl().getMealsByIds(ids);
                System.out.println(meals.size());
                EventBusFactory.getEventBus().post(new ShowMealsEvent(meals, View.BROWSE));
            } else {
                //TODO ADD popup message saying there is nothing to show;
                System.err.println("nothing found");
            }
            loadingCircles.setVisible(false);
            return ids;
        });
    }

    @FXML
    public void returnBtnClicked(){
        EventBusFactory.getEventBus().post(new ReturnEvent());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MealGetterImpl dbAccess = new MealGetterImpl();
        areas = dbAccess.getAreas().stream().map(FilterWrap::new).collect(Collectors.toList());
        categories = dbAccess.getCategories().stream().map(FilterWrap::new).collect(Collectors.toList());

        filterPillsVBox.getChildren().add(new FilterPillsDisplay(areas));
        filterPillsVBox.getChildren().add(new FilterPillsDisplay(categories));

        returnBtn.setGraphic(getButtonIcon("arrow-return-right.png"));
        addAreaBtn.setGraphic(getButtonIcon("add-icon.png"));
        addCategoryBtn.setGraphic(getButtonIcon("add-icon.png"));
        clearAllBtn.setGraphic(getButtonIcon("backspace.png"));
        clearAreaBtn.setGraphic(getButtonIcon("backspace.png"));
        clearCategoryBtn.setGraphic(getButtonIcon("backspace.png"));
        clearNameBtn.setGraphic(getButtonIcon("backspace.png"));
        searchBtn.setGraphic(getButtonIcon("magnifying-glass-icon.png"));

        loadingCircles = initLoadingCircles();
        loadingCircles.setVisible(false);
    }

    public SearchController(){

    }

    private ImageView getButtonIcon(String name){
        Image image = new Image(System.getProperty("user.dir") + "/"
                + PropertyManager.getInstance().getProperty("iconsFolder") + "/" + name);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setSmooth(true);
        imageView.setOpacity(0.80);
        return imageView;
    }

    private LoadingCircles initLoadingCircles(){
        double radius = 70;
        int amountOfCircles = 8;
        double circlesRadius = Math.PI*radius/amountOfCircles-6;
        LoadingCircles circles = new LoadingCircles(radius, amountOfCircles, circlesRadius,
                2000, 4000);
        circles.setLayoutX(60);
        circles.setLayoutY(200);
        mainPane.getChildren().add(circles);
        circles.play();
        return circles;
    }
}
