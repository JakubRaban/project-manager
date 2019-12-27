package pl.edu.agh.gastronomiastosowana.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int taskID;

    @Transient
    private StringProperty title;

    @Transient
    private StringProperty details;

    @Transient
    private ObjectProperty<LocalDate> creationDate;

    @Transient
    private ObjectProperty<LocalDate> deadline;

    @ManyToOne
    private Project assessedProject;

    public Task(){
        title = new SimpleStringProperty(this, "title");
        details = new SimpleStringProperty(this, "details");
        creationDate = new SimpleObjectProperty<>(this, "creationDate");
        deadline = new SimpleObjectProperty<>(this, "deadline");
    }

    public Task(String title, String details, LocalDate creationDate, LocalDate deadline, Project assessedProject){
        this();

        setTitle(title);
        setDetails(details);
        setCreationDate(creationDate);
        setDeadline(deadline);
        setAssessedProject(assessedProject);
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String  getTitle(){
        return title.get();
    }
    public void setTitle(String title){
        this.title.setValue(title);
    }
    public StringProperty titleProperty(){
        return this.title;
    }

    @Access(AccessType.PROPERTY)
    public String  getDetails(){
        return details.get();
    }
    public void setDetails(String details){
        this.details.setValue(details);
    }
    public StringProperty detailsProperty(){
        return this.details;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public LocalDate getCreationDate(){
        return creationDate.get();
    }
    public void setCreationDate(LocalDate creationDate){
        this.creationDate.setValue(creationDate);
    }
    public ObjectProperty<LocalDate> creationDateProperty(){
        return this.creationDate;
    }

    @Access(AccessType.PROPERTY)
    public LocalDate getDeadline(){
        return deadline.get();
    }
    public void setDeadline(LocalDate deadline){
        this.deadline.setValue(deadline);
    }
    public ObjectProperty<LocalDate> deadlineProperty(){
        return this.deadline;
    }

    public void setAssessedProject(Project project) {
        this.assessedProject = project;
    }

}
