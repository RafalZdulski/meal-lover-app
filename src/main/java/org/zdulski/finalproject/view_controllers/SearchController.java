package org.zdulski.finalproject.view_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.zdulski.finalproject.config.PropertyManager;
import org.zdulski.finalproject.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ReturnEvent;
import org.zdulski.finalproject.eventbus.ShowMealsEvent;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;
import org.zdulski.finalproject.mealdbAPI.SearchEngineImpl;
import org.zdulski.finalproject.view_auxs.search.filters.FilterPillsDisplay;
import org.zdulski.finalproject.view_auxs.search.filters.FilterWindow;
import org.zdulski.finalproject.view_auxs.search.filters.FilterWrap;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class SearchController implements Initializable {

    private List<FilterWrap> areas;

    private List<FilterWrap> categories;

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
        String[] wordsFilters = new String[0];
        if (!nameField.getText().isBlank())
            wordsFilters = nameField.getText().split("\s+");
        String[] areasFilter = areas.stream().filter(FilterWrap::getCheckValue).map(FilterWrap::toString).toArray(String[]::new);
        String[] categoryFilters = categories.stream().filter(FilterWrap::getCheckValue).map(FilterWrap::toString).toArray(String[]::new);

        //get all meals when filtered by empty filters
        if (wordsFilters.length + areasFilter.length + categoryFilters.length < 1) {
            List<Meal> meals = new MealGetterImpl().getAllMeals();
            EventBusFactory.getEventBus().post(new ShowMealsEvent(meals, View.BROWSE));
        }

        Set<String> ids = new SearchEngineImpl().getIDs(wordsFilters, areasFilter, categoryFilters);
        if (!ids.isEmpty()) {
            List<Meal> meals = new MealGetterImpl().getMealsByIds(ids);
            System.out.println(meals.size());
            EventBusFactory.getEventBus().post(new ShowMealsEvent(meals, View.BROWSE));
            EventBusFactory.getEventBus().post(new ReturnEvent()); //hiding
        } else {
            //TODO ADD popup message saying there is nothing to show;
            System.err.println("nothing found");
        }
    }

    @FXML
    public void returnBtnClicked(){
        EventBusFactory.getEventBus().post(new ReturnEvent());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MealGetterImpl dbAccess = new MealGetterImpl();
        areas = dbAccess.getAreas().stream().map(FilterWrap::new).toList();
        categories = dbAccess.getCategories().stream().map(FilterWrap::new).toList();

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
}
