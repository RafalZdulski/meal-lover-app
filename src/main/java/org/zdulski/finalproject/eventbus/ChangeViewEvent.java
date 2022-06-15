package org.zdulski.finalproject.eventbus;

import org.zdulski.finalproject.view_controllers.View;

public class ChangeViewEvent {

    private final View view;

    public ChangeViewEvent(View view){
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
