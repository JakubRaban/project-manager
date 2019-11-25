package pl.edu.agh.gastronomiastosowana.model;


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

    @Column(unique = true, nullable=false)
    private String groupName;

    @Column(nullable=false)
    private boolean active;

    @Column(nullable=false)
    private LocalDate creationDate;

    @ManyToOne
    private Participant chief = null;

    @ManyToMany(mappedBy = "worksFor")
    private Set<Participant> participants = new HashSet<Participant>();

    @OneToOne(mappedBy = "projectGroup")
    private Project project;

    @OneToMany(mappedBy = "projectGroup")
    private Set<Rating> ratings = new HashSet<Rating>();

    public ProjectGroup(String groupName, Participant creator) throws ChiefIsSetException {
        //creator initially becomes chief, he can then change chief to another person, but losses ability to menage group
        this.groupName = groupName;
        setChief(creator);
        this.creationDate = LocalDate.now();
    }
    public ProjectGroup(){
        //hib
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public void changeChief(Participant new_chief) throws ChiefNotSetException{
        //Previous chief no longer manages the group
        this.chief.getManagedProjectGroups().remove(this);
        //New chief manages this project now
        new_chief.getManagedProjectGroups().add(this);
        //set new_chief as chief to this working group
        this.chief = new_chief;

        addParticipant(chief);
    }

    public void addParticipant(Participant participant){
        participants.add(participant);
        participant.getWorksFor().add(this);
    }

    public void removeParticipant(Participant participant) throws NonPresentParticipantRemovalException, ChiefRemovalException {
        if ( ! participants.contains(participant)){
            throw new NonPresentParticipantRemovalException();
        }
        if ( participant == this.chief){
            throw new ChiefRemovalException();
        }
        participants.remove(participant);
        participant.getWorksFor().remove(this);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }
}
