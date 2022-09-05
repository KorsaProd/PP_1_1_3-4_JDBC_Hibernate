package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }

    private static Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users" +
                    "(id integer PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(40) NOT NULL," +
                    "lastName VARCHAR (40) NOT NULL," +
                    "age int NOT NULL);").addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
            LOGGER.log(Level.INFO, "Создана таблица");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE if EXISTS users").executeUpdate();
            session.getTransaction().commit();
            LOGGER.log(Level.INFO, "Таблица удалена");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            session.getTransaction().commit();
            LOGGER.log(Level.INFO, "Сохранен пользователь с данными: " +
                    "\nИмя: {0}, Фамилия: {1}, Возраст: {2}", new Object[]{name, lastName, age});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(session.get(User.class, id));
            session.getTransaction().commit();
            LOGGER.log(Level.INFO, "Удален пользователь с id = {0}", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createSQLQuery("SELECT * FROM users")
                    .addEntity(User.class);
            List<User> userList = query.list();
            for (User u : userList) {
                User user = new User(u.getName(), u.getLastName(), u.getAge());
                user.setId(u.getId());
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        List<User> userList = getAllUsers();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (User u : userList) {
                session.delete(u);
            }
            session.getTransaction().commit();
            LOGGER.log(Level.INFO, "Таблица очищена");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
