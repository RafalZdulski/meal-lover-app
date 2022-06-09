package org.zdulski.finalproject.view_controllers;

import javafx.fxml.Initializable;

public interface ViewController extends Initializable {
    void onEntering();
    void update();
    void onLeaving();
}
