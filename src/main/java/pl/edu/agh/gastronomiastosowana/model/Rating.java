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
    private StringProperty title;
    @Transient
    private ObjectProperty<RatingDetails> ratingValue;
    @Transient
    private ObjectProperty<LocalDate> submitDate;
    @Transient
    private StringProperty comment;

    @Transient
    private ObjectProperty<Participant> participant;
    @ManyToOne
    private ProjectGroup assessedGroup;

    public Rating(String title, ProjectGroup assessedGroup, Participant participant, RatingDetails ratingValue, String comment) throws InvalidRatingValueException {
        this();
        setTitle(title);
        setRatingValue(ratingValue);
        setSubmitDate(LocalDate.now());
        setComment(comment);
        setParticipant(participant);
        setAssessedGroup(assessedGroup);
    }

    public Rating() {
        title = new SimpleStringProperty(this, "title");
        ratingValue = new SimpleObjectProperty<>(this, "ratingValue");
        submitDate = new SimpleObjectProperty<>(this, "submitDate");
        comment = new SimpleStringProperty(this, "comment");
        participant = new SimpleObjectProperty<>(this, "participant");
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    private String getTitle() {
        return title.get();
    }
    private void setTitle(String title) {
        this.title.set(title);
    }
    private StringProperty titleProperty() {
        return this.title;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    @Embedded
    public RatingDetails getRatingValue() {
        return ratingValue.getValue();
    }
    public void setRatingValue(RatingDetails ratingValue) throws InvalidRatingValueException {
        double rating = ratingValue.getRatingValue();
        double maxRating = ratingValue.getMaxRatingValue();
        if(rating < 0.0 || rating > maxRating) throw new InvalidRatingValueException();
        this.ratingValue.setValue(ratingValue);
    }
    public ObjectProperty<RatingDetails> ratingProperty() {
        return this.ratingValue;
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
