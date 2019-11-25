package pl.edu.agh.gastronomiastosowana.dao;

import org.hibernate.Session;
import pl.edu.agh.gastronomiastosowana.model.Critic;
import pl.edu.agh.gastronomiastosowana.model.Rating;
import pl.edu.agh.gastronomiastosowana.session.SessionService;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class RatingDao extends GenericDao<Rating> {

    public RatingDao() {
        super();
    }

    public List<Rating> findRatingsByProjectGroupName(String name) {
        final Session session = SessionService.getSession();
        TypedQuery<Rating> ratingQuery = session.createQuery(
                "from Rating r where r.projectGroup.groupName = :name", Rating.class
        );
        ratingQuery.setParameter("name", name);
        return ratingQuery.getResultList();
    }

    public Optional<List<Rating>> findRatingsByCritic(String criticFullName) {
        final Session session = SessionService.getSession();
        CriticDao critics = new CriticDao();
        Optional<Critic> foundCritic = critics.findByFullName(criticFullName);
        if (foundCritic.isPresent()) {
            TypedQuery<Rating> criticQuery = session.createQuery(
                    "from Rating r where r.critic = :crit", Rating.class
            );
            criticQuery.setParameter("crit", foundCritic.get());
            return Optional.of(criticQuery.getResultList());
        }
        return Optional.empty();
    }

}
