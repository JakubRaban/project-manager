package pl.edu.agh.gastronomiastosowana.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int participantID;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String surname;

    @Column(nullable=false)
    private int age;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable=false)
    private LocalDate registrationDate;

    @ManyToMany
    private Set<ProjectGroup> worksFor;

    @OneToMany(mappedBy = "chief")
    private Set<ProjectGroup> managedProjectGroups;



    public Participant(String name, String surname, int age, String email){
        worksFor = new HashSet<ProjectGroup>();
        managedProjectGroups = new HashSet<ProjectGroup>();

        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.registrationDate = LocalDate.now();
    }

    public Participant(){
        //hib
    }

    public Set<ProjectGroup> getWorksFor() {
        return worksFor;
    }

    public Set<ProjectGroup> getManagedProjectGroups() {
        return managedProjectGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getParticipantID() {
        return participantID;
    }
}
