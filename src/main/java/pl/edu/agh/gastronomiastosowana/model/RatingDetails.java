package pl.edu.agh.gastronomiastosowana.model;

import javax.persistence.Embeddable;

@Embeddable
public class RatingDetails {

    private double ratingValue;
    private double maxRatingValue;

    public RatingDetails() { }

    public RatingDetails(double ratingValue, double maxRatingValue) {
        this.ratingValue = ratingValue;
        this.maxRatingValue = maxRatingValue;
    }

    public double getRatingValue() {
        return ratingValue;
    }

    public double getMaxRatingValue() {
        return maxRatingValue;
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public void setMaxRatingValue(double maxRatingValue) {
        this.maxRatingValue = maxRatingValue;
    }
}
