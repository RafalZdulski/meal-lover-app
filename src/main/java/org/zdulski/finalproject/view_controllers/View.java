package org.zdulski.finalproject.view_controllers;

public enum View {
    BROWSE("/org/zdulski/finalproject/views/browse-view.fxml"),
    //RANDOM("/org/zdulski/finalproject/views/random-meal-view.fxml"),
    MEAL("/org/zdulski/finalproject/views/meal-view.fxml"),
    LATEST("/org/zdulski/finalproject/views/latest-view.fxml"),
    DASHBOARD(""),
    FAVOURITE("/org/zdulski/finalproject/views/favourites-view.fxml");


    String url;
    View(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
