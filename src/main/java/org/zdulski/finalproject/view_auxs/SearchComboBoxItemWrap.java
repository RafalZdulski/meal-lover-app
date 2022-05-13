package org.zdulski.finalproject.view_auxs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SearchComboBoxItemWrap<T> {

    private BooleanProperty check = new SimpleBooleanProperty(false);
    private ObjectProperty<T> item = new SimpleObjectProperty<>();

    public SearchComboBoxItemWrap() {
    }

    public SearchComboBoxItemWrap(T item) {
        this.item.set(item);
    }

    public SearchComboBoxItemWrap(T item, Boolean check) {
        this.item.set(item);
        this.check.set(check);
    }

    public BooleanProperty checkProperty() {
        return check;
    }

    public Boolean getCheck() {
        return check.getValue();
    }

    public void setCheck(Boolean value) {
        check.set(value);
    }

    public ObjectProperty<T> itemProperty() {
        return item;
    }

    public T getItem() {
        return item.getValue();
    }

    public void setItem(T value) {
        item.setValue(value);
    }

    @Override
    public String toString() {
        return item.getValue().toString();
    }
}