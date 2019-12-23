package pl.edu.agh.gastronomiastosowana.model.aggregations;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

public abstract class AbstractAggregatedList<T> {
    protected ListProperty<T> elements;

    public AbstractAggregatedList() {
        elements = new SimpleListProperty<>();
    }

    public ObservableList<T> getElements() {
        return elements.get();
    }

    public void setElements(ObservableList<T> elements) {
        this.elements.set(elements);
    }

    public ListProperty<T> getProperty() {
        return elements;
    }
}
