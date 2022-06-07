package org.zdulski.finalproject.view_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.zdulski.finalproject.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
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
    private TextField nameField;

    @FXML
    private VBox filterPillsVBox;

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
        if (wordsFilters.length + areasFilter.length + categoryFilters.length < 1)
            EventBusFactory.getEventBus().post(new ShowMealsEvent(new MealGetterImpl().getAllMeals()));

        Set<String> ids = new SearchEngineImpl().getIDs(wordsFilters, areasFilter, categoryFilters);
        if (!ids.isEmpty()) {
            List<Meal> meals = new MealGetterImpl().getMealsByIds(ids);
            System.out.println(meals.size());
            EventBusFactory.getEventBus().post(new ShowMealsEvent(meals));
        } else {
            //TODO ADD popup message saying there is nothing to show;
            System.err.println("nothing found");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MealGetterImpl dbAccess = new MealGetterImpl();
        areas = dbAccess.getAreas().stream().map(FilterWrap::new).toList();
        categories = dbAccess.getCategories().stream().map(FilterWrap::new).toList();

        filterPillsVBox.getChildren().add(new FilterPillsDisplay(areas));
        filterPillsVBox.getChildren().add(new FilterPillsDisplay(categories));
    }
}
