package pl.edu.agh.gastronomiastosowana.dao;

import org.hibernate.Session;
import pl.edu.agh.gastronomiastosowana.model.Critic;
import pl.edu.agh.gastronomiastosowana.session.SessionService;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Objects;
import java.util.Optional;

public class CriticDao extends GenericDao<Critic> {

    public CriticDao() {
        super();
    }

    public Optional<Critic> findByFullName(String fullName) {
        Objects.requireNonNull(fullName);
        final Session session = SessionService.getSession();
        TypedQuery<Critic> getCriticByFullNameQuery = session.createQuery(
                "from Critic c where concat(c.name, ' ', c.surname) = :fname", Critic.class
        );
        getCriticByFullNameQuery.setParameter("fname", fullName);
        try {
            return Optional.of(getCriticByFullNameQuery.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Critic> findByNameAndSurname(String name, String surname) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(surname);
        return findByFullName(name + " " + surname);
    }

    public Optional<Critic> findByEmail(String email) {
        Objects.requireNonNull(email);
        final Session session = SessionService.getSession();
        TypedQuery<Critic> findCriticByEmailQuery = session.createQuery(
                "from Critic c where c.email = :email", Critic.class
        );
        findCriticByEmailQuery.setParameter("email", email);
        try {
            return Optional.of(findCriticByEmailQuery.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
