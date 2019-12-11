package pl.edu.agh.gastronomiastosowana.model;

import pl.edu.agh.gastronomiastosowana.model.exceptions.InvalidRatingValueException;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ratingID;

    @Column(nullable = false)
    private double ratingValue;

    @Column(nullable = false)
    private LocalDate submitDate;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    private ProjectGroup projectGroup;

    @ManyToOne
    private Critic critic;

    public Rating(ProjectGroup projectGroup, Critic critic, double ratingValue, String comment) throws InvalidRatingValueException {
        submitDate = LocalDate.now();

        if ((ratingValue < 0.0) || ( ratingValue > 5.0)){
            throw new InvalidRatingValueException();
        }
        this.ratingValue = ratingValue;

        this.comment = comment;

        this.projectGroup = projectGroup;
        this.projectGroup.getRatings().add(this);

        this.critic = critic;

    }
    public Rating(){

    }

    public double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
