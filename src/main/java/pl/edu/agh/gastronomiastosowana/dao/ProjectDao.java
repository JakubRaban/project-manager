package pl.edu.agh.gastronomiastosowana.dao;

import org.hibernate.Session;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.session.SessionService;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    /*
     * If endDate is null, find all starting on or after startDate
     * If startDate is null, find all starting on or before endDate
     */
    public List<Project> findProjectsStartingBetweenDates(LocalDate periodStart, LocalDate periodEnd) {
        Session session = SessionService.getSession();
        TypedQuery<Project> projectQuery = session.createQuery(
                "from Project p where p.startDate >= :pstart and p.startDate <= :pend", Project.class
        );
        projectQuery.setParameter("pstart", periodStart);
        projectQuery.setParameter("pend", periodEnd);
        return projectQuery.getResultList();
    }

    public List<Project> findProjectsEndingBetweenDates(LocalDate periodStart, LocalDate periodEnd) {
        Session session = SessionService.getSession();
        TypedQuery<Project> projectQuery = session.createQuery(
                "from Project p where p.endDate >= :pstart and p.endDate <= :pend", Project.class
        );
        projectQuery.setParameter("pstart", periodStart);
        projectQuery.setParameter("pend", periodEnd);
        return projectQuery.getResultList();
    }

}
