package org.zdulski.finalproject.mediators;

import org.zdulski.finalproject.view_controllers.SearchController;

public class SearchMediator {
    //TODO RETHINK should the communication between controllers be handled by mediator pattern
    private SearchMediator(){}
    static private SearchMediator INSTANCE = new SearchMediator();
    static public SearchMediator getInstance(){return INSTANCE;}

    protected SearchController controller;

    public void setController(SearchController controller){
        this.controller = controller;
    }



}
