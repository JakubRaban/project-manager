package pl.edu.agh.gastronomiastosowana.model;


import javafx.beans.property.*;
import javafx.collections.FXCollections;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int participantID;

    @Transient private StringProperty name;

    @Transient private StringProperty surname;

    @Transient private IntegerProperty age;

    @Transient private StringProperty email;

    @Transient private ObjectProperty<LocalDate> registrationDate;

    @Transient private SetProperty<ProjectGroup> worksFor;

    @Transient private SetProperty<ProjectGroup> managedProjectGroups;



    public Participant(String name, String surname, int age, String email){
        this();

        setName(name);
        setSurname(surname);
        setAge(age);
        setEmail(email);
        setRegistrationDate(LocalDate.now());

    }

    public Participant(){
        worksFor = new SimpleSetProperty<ProjectGroup>();
        managedProjectGroups = new SimpleSetProperty<ProjectGroup>();

        name = new SimpleStringProperty(this, "name");
        surname = new SimpleStringProperty(this, "surname");
        age = new SimpleIntegerProperty(this, "age");
        email = new SimpleStringProperty(this, "email");
        registrationDate = new SimpleObjectProperty<LocalDate>(this, "registrationDate");
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable=false)
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public StringProperty nameProperty(){
        return name;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable=false)
    public String getSurname() {
        return surname.get();
    }
    public void setSurname(String surname) {
        this.surname.set(surname);
    }
    public StringProperty surnameProperty(){
        return surname;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable=false)
    public int getAge() {
        return age.get();
    }
    public void setAge(int age) {
        this.age.set(age);
    }
    public IntegerProperty ageProperty() {
        return age;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable=false, unique = true)
    public String getEmail() {
        return email.get();
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public StringProperty emailProperty(){
        return email;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable=false)
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
    public Set<ProjectGroup> getWorksFor() {
        return worksFor.get();
    }
    public void setWorksFor(Set<ProjectGroup> projectGroups){
        this.worksFor.set(FXCollections.observableSet(projectGroups));
    }
    public SetProperty<ProjectGroup> worksForProperty(){
        return worksFor;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(mappedBy = "chief")
    public Set<ProjectGroup> getManagedProjectGroups() {
        return managedProjectGroups.get();
    }
    public void setManagedProjectGroups(Set<ProjectGroup> projectGroups){
        this.managedProjectGroups.set(FXCollections.observableSet(projectGroups));
    }
    public SetProperty<ProjectGroup> managedProjectGroupsProperty(){
        return managedProjectGroups;
    }
}

