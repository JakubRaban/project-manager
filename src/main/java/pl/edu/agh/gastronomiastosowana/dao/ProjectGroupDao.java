package pl.edu.agh.gastronomiastosowana.dao;

import org.hibernate.Session;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.session.SessionService;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class ProjectGroupDao extends GenericDao<ProjectGroup>{

    public ProjectGroupDao() {
        super();
    }

    public List<ProjectGroup> findProjectGroupsByLeader(Participant leader){
        final Session session = SessionService.getSession();
        Query PGByLeaderQuery = session.createQuery("from ProjectGroup as pg " +
                "where pg.leader = :leader", ProjectGroup.class);
        PGByLeaderQuery.setParameter("leader", leader);

        List<ProjectGroup> result;

        result = (List<ProjectGroup>) PGByLeaderQuery.getResultList();

        return result;
    }

    public Optional<ProjectGroup> findProjectGroupByProject(Project project){
        final Session session = SessionService.getSession();

        Query PGByLeaderQuery = session.createQuery("from ProjectGroup as pg " +
                "where pg.project = :project", ProjectGroup.class);
        PGByLeaderQuery.setParameter("project", project);

        Optional result;

        result = PGByLeaderQuery.getResultStream().findFirst();

        return result;
    }

}