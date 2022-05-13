package org.zdulski.finalproject.view_auxs;


import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class SearchComboBoxCellFactory implements Callback<ListView<SearchComboBoxItemWrap<String>>, ListCell<SearchComboBoxItemWrap<String>>> {
    ComboBox<SearchComboBoxItemWrap<String>> comboBox;

    public SearchComboBoxCellFactory(ComboBox<SearchComboBoxItemWrap<String>> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public ListCell<SearchComboBoxItemWrap<String>> call(ListView<SearchComboBoxItemWrap<String>> listView) {
        ListCell<SearchComboBoxItemWrap<String>> cell = new ListCell<>(){
            @Override
            public void updateItem(SearchComboBoxItemWrap<String> item, boolean empty){
                super.updateItem(item, empty);
                if (!empty) {
                    final CheckBox checkBox = new CheckBox(item.getItem());
                    checkBox.selectedProperty().bind(item.checkProperty());
                    setGraphic(checkBox);
                }
            }
        };

        cell.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
            StringBuilder sb = new StringBuilder();
            comboBox.getItems().filtered(SearchComboBoxItemWrap::getCheck).forEach(p -> {
                sb.append(", ").append(p.getItem());
            });
            final String string = sb.toString();
            System.out.println(string);
            comboBox.setPromptText(string.substring(Integer.min(2, string.length())));
        });

        return cell;
    }
}
