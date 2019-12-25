package pl.edu.agh.gastronomiastosowana.dao;

import org.hibernate.Session;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.session.SessionService;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectGroupDao extends GenericDao<ProjectGroup>{

    private ProjectDao projects = new ProjectDao();

    public ProjectGroupDao() {
        super();
    }

    public List<ProjectGroup> findProjectGroupsByLeader(Participant leader){
        final Session session = SessionService.getSession();
        TypedQuery<ProjectGroup> PGByLeaderQuery = session.createQuery("from ProjectGroup as pg " +
                "where pg.leader = :leader", ProjectGroup.class);
        PGByLeaderQuery.setParameter("leader", leader);

        return PGByLeaderQuery.getResultList();
    }

    public Optional<ProjectGroup> findProjectGroupByProject(Project project){
        final Session session = SessionService.getSession();

        TypedQuery<ProjectGroup> PGByLeaderQuery = session.createQuery("from ProjectGroup as pg " +
                "where pg.project = :project", ProjectGroup.class);
        PGByLeaderQuery.setParameter("project", project);

        return PGByLeaderQuery.getResultStream().findFirst();
    }

    public List<ProjectGroup> findActiveProjectGroups() {
        return mapProjectsToProjectGroups(projects.findActiveProjects());
    }

    public List<ProjectGroup> findArchivalProjectGroups() {
        return mapProjectsToProjectGroups(projects.findArchivalProjects());
    }

    public List<ProjectGroup> findFutureProjectGroups() {
        return mapProjectsToProjectGroups(projects.findFutureProjects());
    }

    private List<ProjectGroup> mapProjectsToProjectGroups(List<Project> projects) {
        return projects.stream()
                .map(Project::getProjectGroup)
                .collect(Collectors.toList());
    }

}