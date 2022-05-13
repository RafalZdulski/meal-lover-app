package org.zdulski.finalproject.mediators;

import javafx.scene.layout.Pane;
import org.zdulski.finalproject.view_controllers.MainController;

public class MainMediator {
    //TODO RETHINK should the communication between controllers be handled by mediator pattern
    private MainMediator(){}
    static private MainMediator INSTANCE = new MainMediator();
    static public MainMediator getInstance(){return INSTANCE;}

    protected MainController controller;

    public void setController(MainController Controller){
        this.controller = Controller;
    }

    public void setCenter(Pane pane){
        controller.setCenterView(pane);
    }

    public void returnToLatestPane(){
        controller.setLatestPaneAsMain();
    }

}
