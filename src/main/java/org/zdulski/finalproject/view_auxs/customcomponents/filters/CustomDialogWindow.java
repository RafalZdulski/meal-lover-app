package org.zdulski.finalproject.view_auxs.customcomponents.filters;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

//TODO is this a custom graphic component
public class CustomDialogWindow extends Stage {
    private final int WIDTH = 200;
    private final int HEIGHT = 400;

    private List<ItemWrap<String>> items;
    private Scene dialogScene;


    public CustomDialogWindow(List<ItemWrap<String>> items){
        this.items = List.copyOf(items);
        this.initModality(Modality.WINDOW_MODAL);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(getList());
        borderPane.setBottom(getButtons());
        dialogScene = new Scene(borderPane, WIDTH, HEIGHT);

        this.setScene(dialogScene);
    }

    private HBox getButtons() {
        HBox hBox = new HBox();
        Button add = new Button("add");
        add.setPrefWidth(100);
        add.setOnAction( e ->{
            items.stream().filter(ItemWrap::getCheck).forEach(System.out::println);
        });

        Button ret = new Button("cancel");
        ret.setPrefWidth(100);
        ret.setOnAction( e ->{
            items.stream().filter(ItemWrap::getCheck).forEach(System.out::println);
        });

        hBox.getChildren().add(ret);
        hBox.getChildren().add(add);
        return hBox;
    }

    private ListView<ItemWrap<String>> getList(){
        ListView<ItemWrap<String>> listView = new ListView<>();
        listView.setItems(FXCollections.observableList(items));

        listView.setCellFactory(c -> {
            ListCell<ItemWrap<String>> cell = new ListCell<>(){
                @Override
                protected void updateItem(ItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        final CheckBox cb = new CheckBox(item.toString());
                        cb.setBackground(Background.fill(Color.LIGHTGREEN));

                        //cb.setPrefWidth(WIDTH)
                        cb.selectedProperty().bind(item.checkProperty());
                        setGraphic(cb);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                try {
                    cell.getItem().setCheck(!cell.getItem().getCheck());
                }catch (RuntimeException e){
                    System.err.println(e.getMessage());
                }
                StringBuilder sb = new StringBuilder();
                items.forEach(p -> {
                    if (p.getCheck()) sb.append("; ").append(p.getItem());
                });
                final String string = sb.toString();
                System.out.println("cellEvent: " + string);
            });
            return cell;
        });

        return listView;
    }
}
