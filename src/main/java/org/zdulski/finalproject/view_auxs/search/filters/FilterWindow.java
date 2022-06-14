package org.zdulski.finalproject.view_auxs.search.filters;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

//TODO does this count as a custom graphic component?
public class FilterWindow extends Stage {
    private final int WIDTH = 270;
    private final int HEIGHT = 400;

    private final List<FilterWrap> newFilterList;
    private final List<FilterWrap> oldFilterList;

    public FilterWindow(List<FilterWrap> items){
        this.oldFilterList = items;
        this.newFilterList = items.stream().map(FilterWrap::copyOf).collect(Collectors.toList());
        this.initModality(Modality.APPLICATION_MODAL);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(getList());
        borderPane.setBottom(getButtons());
        Scene dialogScene = new Scene(borderPane, WIDTH, HEIGHT);
        String css = this.getClass().getResource("/org/zdulski/finalproject/css/filter-window.css").toExternalForm();
        dialogScene.getStylesheets().add(css);

        this.setScene(dialogScene);
        this.setAlwaysOnTop(true);
        this.setResizable(false);
        this.show();
    }

    private HBox getButtons() {
        int buttonWidth = WIDTH/3;

        HBox hBox = new HBox();
        Button addBtn = new Button("add");
        addBtn.setPrefWidth(buttonWidth);
        addBtn.setOnAction( e -> {
            for (int i=0; i< oldFilterList.size(); i++){
                oldFilterList.get(i).setCheck(newFilterList.get(i).getCheckValue());
            }
            this.close();
        });

        Button retBtn = new Button("cancel");
        retBtn.setPrefWidth(buttonWidth);
        retBtn.setOnAction( e -> this.close());

        Button clearBtn = new Button("clear");
        clearBtn.setPrefWidth(buttonWidth);
        clearBtn.setOnAction(e -> newFilterList.forEach(filter -> filter.setCheck(false)));

        hBox.getChildren().addAll(new Button[]{clearBtn,retBtn,addBtn});
        return hBox;
    }

    private ListView<FilterWrap> getList(){
        ListView<FilterWrap> listView = new ListView<>();
        listView.setItems(FXCollections.observableList(newFilterList));

        listView.setCellFactory(c -> {
            ListCell<FilterWrap> cell = new ListCell<>(){
                protected void updateItem(FilterWrap item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        final CheckBox cb = new CheckBox(item.toString());
                        cb.selectedProperty().bind(item.getCheck());
                        //disabling so it EventHandlers would not overlap with cell event defined below
                        cb.setDisable(true);
                        cb.setOpacity(1);

                        setGraphic(cb);
                    }
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                if (cell.getItem() != null)
                    cell.getItem().reverseCheck();
            });
            return cell;
        });

        return listView;
    }
}
