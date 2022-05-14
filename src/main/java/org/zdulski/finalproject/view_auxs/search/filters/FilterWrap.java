package org.zdulski.finalproject.view_auxs.search.filters;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class FilterWrap implements Observable {

    private final BooleanProperty check = new SimpleBooleanProperty(false);
    private final String item;

    public FilterWrap(String item) {
        this.item = item;
    }

    public FilterWrap(String item, Boolean check) {
        this.item = item;
        this.check.set(check);
    }
    public BooleanProperty getCheck(){return check;}

    public Boolean getCheckValue() {
        return check.getValue();
    }

    public void setCheck(Boolean value) {
        check.set(value);
    }

    @Override
    public String toString() {
        return item;
    }

    public void reverseCheck() {
        boolean curr = this.check.getValue();
        this.check.set(!curr);
    }

    static public FilterWrap copyOf(FilterWrap o){
        return new FilterWrap(o.item, o.check.getValue());
    }

    @Override
    public void addListener(InvalidationListener e) {
        check.addListener(e);
    }

    @Override
    public void removeListener(InvalidationListener e) {
        check.addListener(e);
    }
}
