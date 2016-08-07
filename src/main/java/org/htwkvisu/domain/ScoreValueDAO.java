package org.htwkvisu.domain;

import org.hibernate.Query;

import java.util.List;

public class ScoreValueDAO {

    private static final HibernateManager HIBERNATE = new HibernateManager();

    public static List<ScoreValue> query(String query) {
        return HIBERNATE.transact((q) -> (List<ScoreValue>) HIBERNATE.getSession().createQuery(q).list(), query);
    }

    public static int update(String query) {
        return HIBERNATE.transact((q) -> {
            Query updateQuery = HIBERNATE.getSession().createQuery(q);
            int count = updateQuery.executeUpdate();
            return count;
        }, query);
    }

    public static List<ScoreValue> exeNamedQuery(String name) {
        return HIBERNATE.transact((n) -> (List<ScoreValue>) HIBERNATE.getSession().getNamedQuery(n).list(), name);
    }
}
