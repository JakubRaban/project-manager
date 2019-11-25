package pl.edu.agh.gastronomiastosowana.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionService {
    private static final SessionFactory sessionFactory = new Configuration().configure() // configures settings from
            // hibernate.cfg.xml
            .buildSessionFactory();

    private static Session session;

    public static void openSession() {
        session = sessionFactory.openSession();
    }

    public static Session getSession() {
        if ( session == null ){
            openSession();
        }
        return session;
    }

    public static void closeSession() {
        session.close();
    }
}
