package pl.edu.agh.gastronomiastosowana.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.edu.agh.gastronomiastosowana.model.exceptions.GroupAlreadyAssignedException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.LeaderIsSetException;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int projectID;
    @Transient
    private StringProperty name;
    @Transient
    private ObjectProperty<LocalDate> startDate;
    @Transient
    private ObjectProperty<LocalDate> endDate;
    @Transient
    private ObjectProperty<ProjectGroup> projectGroup;

    public Project(String projectName) {
        this();
        setName(projectName);
        setStartDate(LocalDate.now());
    }

    public Project() {
        name = new SimpleStringProperty(this, "name");
        startDate = new SimpleObjectProperty<>(this, "startDate");
        endDate = new SimpleObjectProperty<>(this, "endDate");
        projectGroup = new SimpleObjectProperty<>(this, "projectGroup");
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
    public LocalDate getStartDate() {
        return startDate.get();
    }
    public void setStartDate(LocalDate startDateProperty) {
        this.startDate.set(startDateProperty);
    }
    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    @Access(AccessType.PROPERTY)
    @Column
    public LocalDate getEndDate() {
        return endDate.get();
    }
    public void setEndDate(LocalDate endDateProperty) {
        this.endDate.set(endDateProperty);
    }
    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    @Access(AccessType.PROPERTY)
    @OneToOne(cascade = CascadeType.REMOVE)
    public ProjectGroup getProjectGroup() {
        return projectGroup.get();
    }
    public void setProjectGroup(ProjectGroup projectGroup) throws GroupAlreadyAssignedException {
        if (getProjectGroup() != null) {
            getProjectGroup().setProject(null);
        }

        this.projectGroup.set(projectGroup);
        if (getProjectGroup() != null)
            getProjectGroup().setProject(this);
    }
    public void setProjectGroup(String groupName) {
        System.out.println(projectGroup);
        if (this.projectGroup.getValue() != null) {
            getProjectGroup().setProject(null);
            this.projectGroup.set(null);
        }

        projectGroup.set(new ProjectGroup(groupName));
        getProjectGroup().setProject(this);
    }
    public ObjectProperty<ProjectGroup> projectGroupProperty() {
        return projectGroup;

    }

    public void cancelProjectGroupAssignment() {
        if (this.projectGroup != null) {
            this.projectGroup.getValue().setProject(null);
            this.projectGroup.setValue(null);
        }

    }

    public void createProjectGroup(String groupName) throws GroupAlreadyAssignedException, LeaderIsSetException {
        if (getProjectGroup() != null) {
            throw new GroupAlreadyAssignedException();
        }
        projectGroup.set(new ProjectGroup(groupName));
        getProjectGroup().setProject(this);
    }

    public String toString() {
        return this.name.getValue();
    }
}
