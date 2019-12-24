package pl.edu.agh.gastronomiastosowana.dao;

import org.hibernate.Session;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.session.SessionService;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.*;

public class ProjectDao extends GenericDao<Project> {

    public ProjectDao() {
        super();
    }

    public Optional<Project> findProjectByName(String name) {
        Objects.requireNonNull(name);
        Session session = SessionService.getSession();
        TypedQuery<Project> projectQuery = session.createQuery(
                "from Project p where p.name = :name", Project.class
        );
        projectQuery.setParameter("name", name);
        try {
            return Optional.of(projectQuery.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Project> findProjectByProjectGroupName(String name) {
        Objects.requireNonNull(name);
        Session session = SessionService.getSession();
        TypedQuery<Project> projectQuery = session.createQuery(
                "from Project p where p.projectGroup.groupName = :name", Project.class
        );
        projectQuery.setParameter("name", name);
        try {
            return Optional.of(projectQuery.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Project> findActiveProjects() {
        Session session = SessionService.getSession();
        TypedQuery<Project> projectQuery = session.createQuery(
                "from Project p where p.startDate <= :today and p.endDate >= :today", Project.class
        );
        projectQuery.setParameter("today", LocalDate.now());
        return projectQuery.getResultList();
    }

    public List<Project> findArchivalProjects() {
        Session session = SessionService.getSession();
        TypedQuery<Project> projectQuery = session.createQuery(
                "from Project p where p.endDate < :today", Project.class
        );
        projectQuery.setParameter("today", LocalDate.now());
        return projectQuery.getResultList();
    }

    public List<Project> findFutureProjects() {
        Session session = SessionService.getSession();
        TypedQuery<Project> projectQuery = session.createQuery(
                "from Project p where p.startDate > :today", Project.class
        );
        projectQuery.setParameter("today", LocalDate.now());
        return projectQuery.getResultList();
    }

}
