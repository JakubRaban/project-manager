package pl.edu.agh.gastronomiastosowana.model;

import javafx.beans.property.*;
import net.bytebuddy.asm.Advice;
import pl.edu.agh.gastronomiastosowana.model.exceptions.InvalidRatingValueException;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ratingID;

    @Transient private DoubleProperty ratingValue = new SimpleDoubleProperty();

    @Transient private ObjectProperty<LocalDate> submitDate = new SimpleObjectProperty<>();

    @Transient private StringProperty comment = new SimpleStringProperty();

    @ManyToOne
    private Participant participant;

    @ManyToOne
    private Critic critic;

    @ManyToOne
    private ProjectGroup assessedGroup;

    public Rating(Participant participant, double ratingValue, String comment) throws InvalidRatingValueException {
        submitDate.setValue(LocalDate.now());

        if ((ratingValue < 0.0) || ( ratingValue > 5.0)){
            throw new InvalidRatingValueException();
        }
        this.ratingValue.setValue(ratingValue);

        this.comment.setValue(comment);

        this.participant = participant;
    }

    public Rating(Participant participant, Critic critic, double ratingValue, String comment) throws InvalidRatingValueException {
        this(participant, ratingValue, comment);
        this.critic = critic;
    }

    public Rating() {

    }


    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public double getRatingValue() {
        return ratingValue.getValue();
    }
    public void setRatingValue(Double ratingValue){this.ratingValue.setValue(ratingValue);}
    public DoubleProperty ratingProperty() { return this.ratingValue; }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getComment() {
        return comment.getValue();
    }
    public void setComment(String comment){this.comment.setValue(comment);}
    public StringProperty commentProperty() { return this.comment; }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public LocalDate getSubmitDate(){ return this.submitDate.getValue(); }
    public void setSubmitDate(LocalDate submitDate){ this.submitDate.setValue(submitDate);}
    public ObjectProperty<LocalDate> submitDateProperty(){ return this.submitDate; }

}
