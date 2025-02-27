package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDaoImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(User user) {
        if (user.getCar() != null && user.getCar().getId() == null) {
            sessionFactory.getCurrentSession().save(user.getCar());
        }
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(
                "SELECT u FROM User u " +
                        "LEFT JOIN FETCH u.car", User.class);
        return query.getResultList();
    }

    @Override
    public User findUserByCar(String model, int series) {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(
                        "SELECT u FROM User u " +
                                "JOIN FETCH u.car " +
                                "WHERE u.car.model = :model AND u.car.series = :series", User.class)
                .setParameter("model", model)
                .setParameter("series", series);
        return query.getSingleResult();
    }
}