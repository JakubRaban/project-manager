package pl.edu.agh.gastronomiastosowana.model;

import javafx.beans.property.*;
import pl.edu.agh.gastronomiastosowana.model.exceptions.InvalidRatingValueException;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ratingID;

    @Transient
    private StringProperty ratingTitle;
    @Transient
    private ObjectProperty<RatingDetails> ratingDetails;
    @Transient
    private ObjectProperty<LocalDate> submitDate;
    @Transient
    private StringProperty comment;

    @Transient
    private ObjectProperty<Participant> participant;
    @ManyToOne
    private ProjectGroup assessedGroup;

    public Rating(String ratingTitle, ProjectGroup assessedGroup, Participant participant, RatingDetails ratingDetails, String comment) throws InvalidRatingValueException {
        this();
        setRatingTitle(ratingTitle);
        setRatingDetails(ratingDetails);
        setSubmitDate(LocalDate.now());
        setComment(comment);
        setParticipant(participant);
        setAssessedGroup(assessedGroup);
    }

    public Rating() {
        ratingTitle = new SimpleStringProperty(this, "ratingTitle");
        ratingDetails = new SimpleObjectProperty<>(this, "ratingDetails");
        submitDate = new SimpleObjectProperty<>(this, "submitDate");
        comment = new SimpleStringProperty(this, "comment");
        participant = new SimpleObjectProperty<>(this, "participant");
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getRatingTitle() {
        return ratingTitle.get();
    }
    public void setRatingTitle(String ratingTitle) {
        this.ratingTitle.set(ratingTitle);
    }
    public StringProperty ratingTitleProperty() {
        return ratingTitle;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    @Embedded
    public RatingDetails getRatingDetails() {
        return ratingDetails.getValue();
    }
    public void setRatingDetails(RatingDetails ratingDetails) throws InvalidRatingValueException {
        double rating = ratingDetails.getRatingValue();
        double maxRating = ratingDetails.getMaxRatingValue();
        if(rating < 0.0 || rating > maxRating) throw new InvalidRatingValueException();
        this.ratingDetails.setValue(ratingDetails);
    }
    public void setRatingValue(double value) {
        getRatingDetails().setRatingValue(value);
    }
    public void setMaxRatingValue(double value) {
        getRatingDetails().setMaxRatingValue(value);
    }
    public ObjectProperty<RatingDetails> ratingDetailsProperty() {
        return this.ratingDetails;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getComment() {
        return comment.getValue();
    }
    public void setComment(String comment) {
        this.comment.setValue(comment);
    }
    public StringProperty commentProperty() {
        return this.comment;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public LocalDate getSubmitDate() {
        return this.submitDate.getValue();
    }
    public void setSubmitDate(LocalDate submitDate) {
        this.submitDate.setValue(submitDate);
    }
    public ObjectProperty<LocalDate> submitDateProperty() {
        return this.submitDate;
    }

    @Access(AccessType.PROPERTY)
    @ManyToOne
    public Participant getParticipant() {
        return this.participant.get();
    }
    public void setParticipant(Participant participant) {
        this.participant.set(participant);
    }
    public ObjectProperty<Participant> participantProperty() {
        return this.participant;
    }

    public void setAssessedGroup(ProjectGroup group) {
        this.assessedGroup = group;
    }

}
