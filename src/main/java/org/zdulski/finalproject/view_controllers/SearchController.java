package org.zdulski.finalproject.view_controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import org.zdulski.finalproject.mealdb.MealdbApiAccess;
import org.zdulski.finalproject.view_auxs.SearchComboBoxCellFactory;
import org.zdulski.finalproject.view_auxs.SearchComboBoxItemWrap;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML
    private ComboBox<SearchComboBoxItemWrap<String>> areaComboBox;

    @FXML
    private ComboBox<SearchComboBoxItemWrap<String>> categoryComboBox;

    @FXML
    void searchBtnClicked() {
        System.out.println("search button clicked");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MealdbApiAccess dbAccess = new MealdbApiAccess();
        List<SearchComboBoxItemWrap<String>> areas
                = dbAccess.getAreas().stream().map(SearchComboBoxItemWrap::new).toList();
        List<SearchComboBoxItemWrap<String>> categories
                = dbAccess.getCategories().stream().map(SearchComboBoxItemWrap::new).toList();

        areaComboBox.setItems(FXCollections.observableList(areas));
        areaComboBox.setCellFactory(new SearchComboBoxCellFactory(areaComboBox));

        categoryComboBox.setItems(FXCollections.observableList(categories));
        categoryComboBox.setCellFactory(c -> {
            ListCell<SearchComboBoxItemWrap<String>> cell = new ListCell<>(){
                @Override
                protected void updateItem(SearchComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        final CheckBox cb = new CheckBox(item.toString());
                        cb.selectedProperty().bind(item.checkProperty());
                        setGraphic(cb);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder sb = new StringBuilder();
                categoryComboBox.getItems().filtered(SearchComboBoxItemWrap::getCheck)
                        .forEach(p -> sb.append("; ").append(p.getItem()));
                final String string = sb.toString();
                System.out.println(string);
                categoryComboBox.setPromptText(string);
            });

            return cell;
        });


    }



}
