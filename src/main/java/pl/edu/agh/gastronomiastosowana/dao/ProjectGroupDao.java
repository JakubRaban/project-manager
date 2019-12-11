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

    public List<ProjectGroup> findProjectGroupsByChief(Participant chief){
        final Session session = SessionService.getSession();
        Query PGByChiefQuery = session.createQuery("from ProjectGroup as pg " +
                "where pg.chief = :chief", ProjectGroup.class);
        PGByChiefQuery.setParameter("chief", chief);

        List<ProjectGroup> result;

        result = (List<ProjectGroup>) PGByChiefQuery.getResultList();

        return result;
    }

    public Optional<ProjectGroup> findProjectGroupByProject(Project project){
        final Session session = SessionService.getSession();

        Query PGByChiefQuery = session.createQuery("from ProjectGroup as pg " +
                "where pg.project = :project", ProjectGroup.class);
        PGByChiefQuery.setParameter("project", project);

        Optional result;

        result = PGByChiefQuery.getResultStream().findFirst();

        return result;
    }

}