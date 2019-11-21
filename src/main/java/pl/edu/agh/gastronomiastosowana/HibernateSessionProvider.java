package pl.edu.agh.gastronomiastosowana;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionProvider {
    private static SessionFactory sessionFactory;

    private HibernateSessionProvider() { }

    public static Session getSession() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            sessionFactory = configuration.configure().buildSessionFactory();
        }
        return sessionFactory.openSession();
    }

}
