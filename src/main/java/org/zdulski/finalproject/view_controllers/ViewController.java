package org.zdulski.finalproject.view_controllers;

import javafx.fxml.Initializable;

public interface ViewController extends Initializable {
    /*TODO ADD make onEntering and onLeaving useful also update is barely usable -
     * I should unify and describe situations where/when those functions are called
     */
    void onEntering();
    void update();
    void onLeaving();
}
