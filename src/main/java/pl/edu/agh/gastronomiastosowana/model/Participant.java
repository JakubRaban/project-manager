package pl.edu.agh.gastronomiastosowana.model;


import javafx.beans.property.*;
import javafx.collections.FXCollections;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int participantID;
    @Transient
    private StringProperty name;
    @Transient
    private StringProperty surname;
    @Transient
    private StringProperty indexNumber;
    @Transient
    private StringProperty email;
    @Transient
    private ObjectProperty<LocalDate> registrationDate;
    @Transient
    private SetProperty<ProjectGroup> participatesIn;
    @Transient
    private SetProperty<ProjectGroup> leaderIn;
    @Transient
    private SetProperty<Rating> ratings;

    public Participant(String name, String surname, String age, String email) {
        this();

        setName(name);
        setSurname(surname);
        setIndexNumber(age);
        setEmail(email);

    }

    public Participant() {
        participatesIn = new SimpleSetProperty<>(this, "worksFor");
        leaderIn = new SimpleSetProperty<>(this, "managedProjectGroup");

        name = new SimpleStringProperty(this, "name");
        surname = new SimpleStringProperty(this, "surname");
        indexNumber = new SimpleStringProperty(this, "indexNumber");
        email = new SimpleStringProperty(this, "email");
        registrationDate = new SimpleObjectProperty<LocalDate>(this, "registrationDate");
        ratings = new SimpleSetProperty<>(this, "ratings");

        setParticipatesIn(new HashSet<>());
        setLeaderIn(new HashSet<>());
        setRegistrationDate(LocalDate.now());
    }


    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public StringProperty nameProperty() {
        return name;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getSurname() {
        return surname.get();
    }
    public void setSurname(String surname) {
        this.surname.set(surname);
    }
    public StringProperty surnameProperty() {
        return surname;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getIndexNumber() {
        return indexNumber.get();
    }
    public void setIndexNumber(String age) {
        this.indexNumber.set(age);
    }
    public StringProperty indexNumberProperty() {
        return indexNumber;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false, unique = true)
    public String getEmail() {
        return email.get();
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public StringProperty emailProperty() {
        return email;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public LocalDate getRegistrationDate() {
        return registrationDate.get();
    }
    public void setRegistrationDate(LocalDate registrationDateProperty) {
        this.registrationDate.set(registrationDateProperty);
    }
    public ObjectProperty<LocalDate> registrationDateProperty() {
        return registrationDate;
    }

    @Access(AccessType.PROPERTY)
    @ManyToMany
    public Set<ProjectGroup> getParticipatesIn() {
        return participatesIn.get();
    }
    public void setParticipatesIn(Set<ProjectGroup> projectGroups) {
        this.participatesIn.set(FXCollections.observableSet(projectGroups));
    }
    public SetProperty<ProjectGroup> worksForProperty() {
        return participatesIn;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(mappedBy = "leader")
    public Set<ProjectGroup> getLeaderIn() {
        return leaderIn.get();
    }
    public void setLeaderIn(Set<ProjectGroup> projectGroups) {
        this.leaderIn.set(FXCollections.observableSet(projectGroups));
    }
    public SetProperty<ProjectGroup> managedProjectGroupsProperty() {
        return leaderIn;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(mappedBy = "participant")
    public Set<Rating> getRating() {
        return ratings.getValue();
    }
    public void setRating(Set<Rating> ratings) {
        if (ratings == null) this.ratings.setValue(FXCollections.emptyObservableSet());
        else this.ratings.setValue(FXCollections.observableSet(ratings));
    }
    public SetProperty<Rating> ratingProperty() {
        return ratings;
    }


    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }
}

