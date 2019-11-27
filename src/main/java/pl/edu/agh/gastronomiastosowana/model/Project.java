package pl.edu.agh.gastronomiastosowana.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import pl.edu.agh.gastronomiastosowana.model.exceptions.ChiefIsSetException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.GroupAlreadyAssignedException;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int projectID;
    @Transient private StringProperty name;
    @Transient private ObjectProperty<LocalDate> startDate;
    @Transient private ObjectProperty<LocalDate> endDate;
    @Transient private ObjectProperty<ProjectGroup> projectGroup;

    public Project(String projectName){
        this();
        setName(projectName);
        setStartDate(LocalDate.now());
    }

    public Project(){
        name = new SimpleStringProperty(this, "name");
        startDate = new SimpleObjectProperty<>(this, "startDate");
        endDate = new SimpleObjectProperty<>(this, "endDate");
        projectGroup = new SimpleObjectProperty<>(this, "projectGroup");
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable=false)
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable=false)
    public LocalDate getStartDate() {
        return startDate.get();
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDate startDateProperty) {
        this.startDate.set(startDateProperty);
    }

    @Access(AccessType.PROPERTY)
    @Column
    public LocalDate getEndDate() {
        return endDate.get();
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDate endDateProperty) {
        this.endDate.set(endDateProperty);
    }

    @Access(AccessType.PROPERTY)
    @OneToOne(cascade = CascadeType.REMOVE)
    public ProjectGroup getProjectGroup() {
        return projectGroup.get();
    }

    public ObjectProperty<ProjectGroup> projectGroupProperty() {
        return projectGroup;
    }

    //prototype:
    //Maybe it won't be needed
    public void setProjectGroup(ProjectGroup projectGroup) throws GroupAlreadyAssignedException {
        if (getProjectGroup() != null) {
            getProjectGroup().setProject(null);
        }

        this.projectGroup.set(projectGroup);
        if (getProjectGroup() != null)
            getProjectGroup().setProject(this);
    }

    //prototype:
    //Maybe it won't be needed
    public void setProjectGroup(String groupName, Participant creator) throws ChiefIsSetException {
        getProjectGroup().setProject(null);
        this.projectGroup.set(null);

        projectGroup.set(new ProjectGroup(groupName, creator));
        getProjectGroup().setProject(this);
    }

    public void createProjectGroup(String groupName, Participant creator) throws GroupAlreadyAssignedException, ChiefIsSetException {
        if (getProjectGroup() != null){
            throw new GroupAlreadyAssignedException();
        }
        projectGroup.set(new ProjectGroup(groupName, creator));
        getProjectGroup().setProject(this);
    }
}
