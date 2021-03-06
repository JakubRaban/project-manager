package pl.edu.agh.gastronomiastosowana.model;

import javafx.beans.property.*;
import pl.edu.agh.gastronomiastosowana.model.exceptions.LeaderRemovalException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.NonPresentParticipantRemovalException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class ProjectGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int projectGroupID;
    @Transient
    private StringProperty groupName;
    @Transient
    private BooleanProperty active;
    @Transient
    private ObjectProperty<LocalDate> creationDate;
    @Transient
    private ObjectProperty<Project> project;

    @ManyToOne
    private Participant leader;
    @ManyToMany(mappedBy = "participatesIn")
    private Set<Participant> participants;
    @OneToMany(mappedBy = "assessedGroup")
    private Set<Rating> ratings;

    public ProjectGroup(String groupName) {
        this();
        setGroupName(groupName);
        setCreationDate(LocalDate.now());
        setActive(true);
    }

    public ProjectGroup() {
        groupName = new SimpleStringProperty(this, "groupName");
        active = new SimpleBooleanProperty(this, "active");
        creationDate = new SimpleObjectProperty<>(this, "creationDate");
        project = new SimpleObjectProperty<>(this, "project");
        participants = new HashSet<>();
    }

    @Access(AccessType.PROPERTY)
    @Column(unique = true, nullable = false)
    public String getGroupName() {
        return groupName.get();
    }
    public void setGroupName(String groupName) {
        this.groupName.set(groupName);
    }
    public StringProperty groupNameProperty() {
        return groupName;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public boolean isActive() {
        return active.get();
    }
    public void setActive(boolean active) {
        this.active.set(active);
    }
    public BooleanProperty activeProperty() {
        return active;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public LocalDate getCreationDate() {
        return creationDate.get();
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate.set(creationDate);
    }
    public ObjectProperty<LocalDate> creationDateProperty() {
        return creationDate;
    }

    @Access(AccessType.PROPERTY)
    @OneToOne(mappedBy = "projectGroup")
    public Project getProject() {
        return project.get();
    }
    public void setProject(Project project) {
        this.project.setValue(project);
    }
    public ObjectProperty<Project> projectProperty() {
        return project;
    }

    public Participant getLeader() {
        return leader;
    }
    public void setLeader(Participant newLeader) {
        if (this.leader != null) {
            this.leader.getLeaderIn().remove(this);
        }
        this.leader = newLeader;
        newLeader.getLeaderIn().add(this);
        addParticipant(leader);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.getParticipatesIn().add(this);
    }

    public void removeParticipant(Participant participant) throws NonPresentParticipantRemovalException, LeaderRemovalException {
        if (!participants.contains(participant)) {
            throw new NonPresentParticipantRemovalException();
        }
        if (participant == this.leader) {
            throw new LeaderRemovalException();
        }
        participants.remove(participant);
        participant.getParticipatesIn().remove(this);
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public int getParticipantCount() {
        return this.participants.size();
    }

    public Participant getParticipantByFullName(String fullName) {
        return participants.stream()
                .filter(p -> p.getFullName().equals(fullName))
                .collect(Collectors.toList())
                .get(0);
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }
}
