package org.zdulski.finalproject.mediator_controllers;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class ViewMediator {
    //TODO RETHINK should the communication between controllers be handled by mediator patter
    private ViewMediator(){}
    static private ViewMediator INSTANCE = new ViewMediator();
    static public ViewMediator getInstance(){return INSTANCE;}

    protected BorderPane mainPane;

    public void setMainPane(BorderPane mainPane){
        this.mainPane = mainPane;
    }

    public void setCenter(Pane pane){
        mainPane.setCenter(pane);
    }


}
