package pl.edu.agh.gastronomiastosowana.dao;

import org.hibernate.Session;
import pl.edu.agh.gastronomiastosowana.model.Critic;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.Rating;
import pl.edu.agh.gastronomiastosowana.session.SessionService;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class RatingDao extends GenericDao<Rating> {

    public RatingDao() {
        super();
    }

    public List<Rating> findRatingsByProjectGroup(ProjectGroup projectGroup) {
        final Session session = SessionService.getSession();
        TypedQuery<Rating> ratingQuery = session.createQuery(
                "from Rating r where r.assessedGroup = :projectGroup", Rating.class
        );
        ratingQuery.setParameter("projectGroup", projectGroup);
        return ratingQuery.getResultList();
    }

}
