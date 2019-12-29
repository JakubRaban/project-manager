package pl.edu.agh.gastronomiastosowana.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javassist.Loader;

import javax.persistence.Embeddable;

@Embeddable
public class RatingDetails {

    private DoubleProperty ratingValue;
    private DoubleProperty maxRatingValue;

    public RatingDetails() {
        this.ratingValue = new SimpleDoubleProperty(this, "ratingValue");
        this.maxRatingValue = new SimpleDoubleProperty(this, "maxRatingValue");
    }

    public RatingDetails(double ratingValue, double maxRatingValue) {
        this();
        setRatingValue(ratingValue);
        setMaxRatingValue(maxRatingValue);
    }

    public double getRatingValue() {
        return ratingValue.get();
    }

    public double getMaxRatingValue() {
        return maxRatingValue.get();
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue.set(ratingValue);
    }

    public void setMaxRatingValue(double maxRatingValue) {
        this.maxRatingValue.set(maxRatingValue);
    }

    public DoubleProperty ratingValueProperty() {
        return ratingValue;
    }

    public DoubleProperty maxRatingValueProperty() {
        return maxRatingValue;
    }
}
