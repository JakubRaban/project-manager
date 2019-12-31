package pl.edu.agh.gastronomiastosowana.model.ratings;

import pl.edu.agh.gastronomiastosowana.model.Rating;
import pl.edu.agh.gastronomiastosowana.model.RatingDetails;

import java.util.List;
import java.util.function.ToDoubleFunction;

public class RatingAggregator {

    private List<Rating> ratings;

    public RatingAggregator(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public double sumOfValues() {
        return this.sumRatingDetailsWithMappingFunction(RatingDetails::getRatingValue);
    }

    public double sumOfMaxValues() {
        return this.sumRatingDetailsWithMappingFunction(RatingDetails::getMaxRatingValue);
    }

    public double averageValueToMaxRatio() {
        return sumOfValues() / sumOfMaxValues();
    }

    public double averageValueToMaxRatioPercent() {
        return averageValueToMaxRatio() * 100;
    }

    private double sumRatingDetailsWithMappingFunction(ToDoubleFunction<RatingDetails> function) {
        return this.ratings.stream()
                .map(Rating::getRatingDetails)
                .mapToDouble(function)
                .sum();
    }

}
