package org.zdulski.finalproject.eventbus;

import org.zdulski.finalproject.view_controllers.View;

import java.util.Set;

public class ShowMealsByIdsEvent {

    private Set<String> ids;

    private View viewType;

    public ShowMealsByIdsEvent(Set<String> ids, View viewType) {
        this.ids = ids;
        this.viewType = viewType;
    }

    public Set<String> getIds() {
        return ids;
    }

    public View getViewType(){
        return viewType;
    }
}
