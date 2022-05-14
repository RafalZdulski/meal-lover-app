package org.zdulski.finalproject.view_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.zdulski.finalproject.mealdb.MealdbApiAccess;
import org.zdulski.finalproject.view_auxs.search.filters.FilterPillsDisplay;
import org.zdulski.finalproject.view_auxs.search.filters.FilterWindow;
import org.zdulski.finalproject.view_auxs.search.filters.FilterWrap;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
        //TODO implement searching
        System.out.println("\nname: " + (nameField.getText().length() != 0) + "\n" + nameField.getText());

        System.out.println("\nareas: " + areas.stream().anyMatch(FilterWrap::getCheckValue));
        areas.stream().filter(FilterWrap::getCheckValue).forEach(System.out::println);

        System.out.println("\ncategories: " + categories.stream().anyMatch(FilterWrap::getCheckValue));
        categories.stream().filter(FilterWrap::getCheckValue).forEach(System.out::println);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MealdbApiAccess dbAccess = new MealdbApiAccess();
        areas = dbAccess.getAreas().stream().map(FilterWrap::new).toList();;
        categories = dbAccess.getCategories().stream().map(FilterWrap::new).toList();

        filterPillsVBox.getChildren().add( new FilterPillsDisplay(areas));
        filterPillsVBox.getChildren().add(new FilterPillsDisplay(categories));
    }
}
