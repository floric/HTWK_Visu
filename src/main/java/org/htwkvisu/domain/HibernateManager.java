package org.htwkvisu.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.function.Function;

/**
 * HibernateManager - manages the connection to hibernate
 * Instead of using the default entitymanager (with too much configutations)
 * , the hibernatemanager will be used for our query-calls.
 */
public class HibernateManager {

    private Session session;

    public <A, B> B transact(Function<A, B> function, A input) {
        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        B result = function.apply(input);
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
        return result;
    }

    public Session getSession() {
        return session;
    }
}
