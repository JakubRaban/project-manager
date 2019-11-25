package pl.edu.agh.gastronomiastosowana.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.gastronomiastosowana.session.SessionService;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;

public class GenericDao<T> {

    private Class<T> entityType;

    @SuppressWarnings("unchecked")
    public GenericDao() {
        this.entityType = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public void create(T entity) {
        Objects.requireNonNull(entity);
        save(entity);
    }

    public List<T> findAll() {
        Session session = SessionService.getSession();
        TypedQuery<T> findAllQuery = session.createQuery(
                "from " + entityType.getName(), entityType
        );
        return findAllQuery.getResultList();
    }

    public T findById(int id) {
        Session session = SessionService.getSession();
        return session.find(entityType, id);
    }

    public void save(final T object) throws PersistenceException {
        final Session session = SessionService.getSession();
        final Transaction tx = session.beginTransaction();
        session.save(object);
        session.merge(object);
        tx.commit();
    }

    public void update(final T object) throws PersistenceException {
        final Session session = SessionService.getSession();
        final Transaction tx = session.beginTransaction();
        session.update(object);
        session.merge(object);
        tx.commit();
    }

    public Session currentSession() {
        return SessionService.getSession();
    }

}
