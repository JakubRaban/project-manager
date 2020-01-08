package pl.edu.agh.gastronomiastosowana.model.aggregations.model_wrapper;

import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.Rating;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class ParticipantProjectGroup {
    //Class used in purpose of saving in object state, which project group was selected to perform binding
    //Wraps Participant
    //selectedProjectGroup is changed every time user clicks "Selected project group only"
    private Participant participant;
    private Optional<ProjectGroup> selectedProjectGroup;

    public ParticipantProjectGroup(Participant participant){
        this.participant = participant;
        this.selectedProjectGroup = Optional.empty();
    }

    public void setSelectedProjectGroup(Optional<ProjectGroup> selectedProjectGroup){
        this.selectedProjectGroup = selectedProjectGroup;
    }

    public String getName(){
        return participant.getName();
    }
    public String getSurname(){
        return participant.getSurname();
    }
    public String getIndexNumber(){
        return participant.getIndexNumber();
    }
    public String getEmail(){
        return participant.getEmail();
    }
    public LocalDate getRegistrationDate(){
        return participant.getRegistrationDate();
    }
    public Set<ProjectGroup> getParticipatesIn(){
        return participant.getParticipatesIn();
    }
    public Set<ProjectGroup> getLeaderIn(){
        return participant.getLeaderIn();
    }
    public Set<Rating> getRating(){
        return participant.getRating();
    }
    public String getFullName(){
        return participant.getFullName();
    }
    public String getNameEmailLabel(){
        return participant.getNameEmailLabel();
    }

    public Double getAverageRating(){
        return participant.getAverageRating(selectedProjectGroup);
    }
    public String getAverageRatingTableCell(){
        Double averageRating = participant.getAverageRating(selectedProjectGroup);
        if (averageRating == Double.NEGATIVE_INFINITY){
            return "NO RATINGS";
        }
        return String.format("%.2f", averageRating * 100) + " %";
    }

    public long getRatingCount(){
        return participant.getRatingCount(selectedProjectGroup);
    }
    public String getRatingCountTableCell(){
        Long ratingCount = participant.getRatingCount(selectedProjectGroup);
        if (ratingCount == 0){
            return "NO RATING";
        }
        return String.valueOf(ratingCount);
    }

    public double getRatingSum(){
        return participant.getRatingSum(selectedProjectGroup);
    }
    public String getRatingSumTableCell(){
        Double ratingSum = participant.getRatingSum(selectedProjectGroup);
        if (ratingSum == Double.NEGATIVE_INFINITY){
            return "NO RATING";
        }
        return String.valueOf(ratingSum);
    }

    public double getMaxRatingSum(){
        return participant.getMaxRatingSum(selectedProjectGroup);
    }
    public String getMaxRatingSumTableCell(){
        Double maxRatingSum = participant.getMaxRatingSum(selectedProjectGroup);
        if (maxRatingSum == Double.NEGATIVE_INFINITY){
            return "NO RATING";
        }
        return String.valueOf(maxRatingSum);
    }
}
