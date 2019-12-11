package pl.edu.agh.gastronomiastosowana.model;


import javafx.beans.property.*;
import pl.edu.agh.gastronomiastosowana.model.exceptions.ChiefIsSetException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.ChiefNotSetException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.ChiefRemovalException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.NonPresentParticipantRemovalException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ProjectGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int projectGroupID;
    @Transient private StringProperty groupName;
    @Transient private BooleanProperty active;
    @Transient private ObjectProperty<LocalDate> creationDate;
    @Transient private ObjectProperty<Project> project;

    @ManyToOne
    private Participant chief;
    @ManyToMany(mappedBy = "worksFor")
    private Set<Participant> participants = new HashSet<>();

    public ProjectGroup(String groupName) {
        this();
        //creator initially becomes chief, he can then change chief to another person, but losses ability to menage group
        setGroupName(groupName);
        setCreationDate(LocalDate.now());
    }

    public ProjectGroup() {
        groupName = new SimpleStringProperty(this, "groupName");
        active = new SimpleBooleanProperty(this, "active");
        creationDate = new SimpleObjectProperty<>(this, "creationDate");
        project = new SimpleObjectProperty<>(this, "project");
    }

    @Access(AccessType.PROPERTY)
    @Column(unique = true, nullable=false)
    public String getGroupName() {
        return groupName.get();
    }

    public StringProperty groupNameProperty() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName.set(groupName);
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable=false)
    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable=false)
    public LocalDate getCreationDate() {
        return creationDate.get();
    }

    public ObjectProperty<LocalDate> creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate.set(creationDate);
    }

    @Access(AccessType.PROPERTY)
    @OneToOne(mappedBy = "projectGroup")
    public Project getProject() {
        return project.get();
    }

    public ObjectProperty<Project> projectProperty() {
        return project;
    }

    public void setProject(Project project) {
        this.project.setValue(project);
    }

    public Participant getChief() {
        return chief;
    }

    private void setChief(Participant chief) throws ChiefIsSetException {
        if ( this.chief != null ){
            throw new ChiefIsSetException();
        }
        this.chief = chief;
        addParticipant(chief);
        chief.getManagedProjectGroups().add(this);
    }

    public void changeChief(Participant new_chief) throws ChiefNotSetException {

        if ( this.chief != null) {
            //Previous chief no longer manages the group
            this.chief.getManagedProjectGroups().remove(this);
        }
        //set new_chief as chief to this working group
        this.chief = new_chief;
        //New chief manages this project now
        new_chief.getManagedProjectGroups().add(this);
        addParticipant(chief);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.getWorksFor().add(this);
    }

    public void removeParticipant(Participant participant) throws NonPresentParticipantRemovalException, ChiefRemovalException {
        if (!participants.contains(participant)){
            throw new NonPresentParticipantRemovalException();
        }
        if ( participant == this.chief){
            throw new ChiefRemovalException();
        }
        participants.remove(participant);
        participant.getWorksFor().remove(this);
    }


    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }
}
