package org.zdulski.finalproject.view_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.zdulski.finalproject.mealdb.MealdbApiAccess;
import org.zdulski.finalproject.view_auxs.customcomponents.filters.CustomDialogWindow;
import org.zdulski.finalproject.view_auxs.customcomponents.filters.ItemWrap;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    private List<ItemWrap<String>> areas;
    private List<ItemWrap<String>> categories;


    @FXML
    void addArea(ActionEvent event) {
        final CustomDialogWindow dialog = new CustomDialogWindow(areas);
        dialog.show();
    }

    @FXML
    void addCategory(ActionEvent event) {

    }

    @FXML
    void clearAll() {
        clearName();
        clearAreas();
        clearCategories();
    }

    @FXML
    void clearAreas() {

    }

    @FXML
    void clearCategories() {

    }

    @FXML
    void clearName() {

    }

    @FXML
    void searchBtnClicked(ActionEvent event) {
        areas.stream().filter(ItemWrap::getCheck).forEach(System.out::println);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MealdbApiAccess dbAccess = new MealdbApiAccess();

        areas = dbAccess.getAreas().stream().map(ItemWrap::new).toList();
        categories = dbAccess.getCategories().stream().map(ItemWrap::new).toList();


    }



}
