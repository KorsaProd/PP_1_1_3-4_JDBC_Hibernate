package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private static final String USER_NAME = "root";
    private static final String PASSWORD = "1q2w3e4r";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/pre_project";
    private static Connection connection;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.url", DATABASE_URL)
                .setProperty("hibernate.connection.username", USER_NAME)
                .setProperty("hibernate.connection.password", PASSWORD)
                .setProperty("hibernate.connection.driver_class", DRIVER)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "none")
                .addAnnotatedClass(User.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeSession() {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }


    public static Connection openConnection() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
