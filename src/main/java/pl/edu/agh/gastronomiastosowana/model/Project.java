package pl.edu.agh.gastronomiastosowana.model;

import pl.edu.agh.gastronomiastosowana.model.exceptions.ChiefIsSetException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.GroupAlreadyAssignedException;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int projectID;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private LocalDate startDate;

    //optional
    //if present, means that project has been finished
    private LocalDate endDate = null;

    //if project is removed, projectgroup is also removed
    @OneToOne(cascade = CascadeType.REMOVE)
    private ProjectGroup projectGroup;


    public void createProjectGroup(String groupName, Participant creator) throws GroupAlreadyAssignedException, ChiefIsSetException {
        if (projectGroup != null){
            throw new GroupAlreadyAssignedException();
        }
        projectGroup = new ProjectGroup(groupName, creator);
        projectGroup.setProject(this);
    }



    //prototype:
    //Maybe it won't be needed
    public void setProjectGroup(String groupName, Participant creator) throws ChiefIsSetException {
        projectGroup.setProject(null);
        this.projectGroup = null;

        projectGroup = new ProjectGroup(groupName, creator);
        projectGroup.setProject(this);
    }
    //prototype:
    //Maybe it won't be needed
    public void setProjectGroup(ProjectGroup newProjectGroup) throws GroupAlreadyAssignedException {
        if ( newProjectGroup.getProject() != null ){
            throw new GroupAlreadyAssignedException();
        }

        if ( projectGroup != null) {
            projectGroup.setProject(null);
        }

        this.projectGroup = newProjectGroup;
        projectGroup.setProject(this);
    }



    public ProjectGroup getProjectGroup() {
        return projectGroup;
    }

    public Project(String projectName){
        this.name = projectName;
        this.startDate = LocalDate.now();
    }
    public Project(){
        //hib
    }
}
