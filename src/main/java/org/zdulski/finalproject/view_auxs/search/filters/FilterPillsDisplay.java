package org.zdulski.finalproject.view_auxs.search.filters;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class FilterPillsDisplay extends FlowPane {

    public FilterPillsDisplay(List<FilterWrap> filters){
        this.setHgap(2);
        this.setVgap(5);
        filters.forEach(this::filterPill);
    }

    private HBox filterPill(FilterWrap filter){
        HBox pill = new HBox();
        pill.setAlignment(Pos.CENTER);
        pill.setBackground(new Background(new BackgroundFill(
                Color.LIGHTGREY,new CornerRadii(30),null)));;
        pill.setSpacing(3);
        pill.setPadding(new Insets(0, 0, 0, 7));

        Text text = new Text(filter.toString());
        text.setFont(Font.font(16));
        pill.getChildren().add(text);

        Button btn = new Button();
        ImageView imageView = new ImageView(new Image(System.getProperty("user.dir") + "/"
                + "src/main/resources/org/zdulski/finalproject/icons/clear-button.png"));
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        btn.setGraphic(imageView);
        btn.setMaxSize(32,32);
        btn.setOnAction(e -> filter.setCheck(false));
        pill.getChildren().add(btn);

        filter.addListener(e -> {
            if (filter.getCheckValue()){
                this.getChildren().add(pill);
            } else {
                this.getChildren().remove(pill);
            }
        });

        return pill;
    }
}
