package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {


    private PreparedStatement preparedStatement;
    private Statement statement;
    private final Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    //language=SQL
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users" +
            "(id integer PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(40) NOT NULL," +
            "lastName VARCHAR (40) NOT NULL," +
            "age int NOT NULL);";

    //language=SQL
    private final String DROP_TABLE = "DROP TABLE IF EXISTS users;";

    //language=SQL
    private final String INSERT_USER = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?);";

    //language=SQL
    private final String REMOVE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";

    //language=SQL
    private final String SELECT_ALL_USERS = "SELECT * FROM users;";

    //language=SQL
    private final String CLEAN_TABLE = "TRUNCATE TABLE users";

    public UserDaoJDBCImpl() {}

    public void createUsersTable() {

        try (Connection connection = getConnection()){
          statement = connection.createStatement();
          statement.execute(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = getConnection()) {
            statement = connection.createStatement();
            statement.execute(DROP_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = getConnection()) {
            preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, String.valueOf(age));
            preparedStatement.execute();
            LOGGER.log(Level.INFO, "Пользователь с данными:" +
                            "\nИмя: {0}, Фамилия: {1}, Возраст: {2} \nдобавлен в базу данных",
                    new Object[] {name, lastName, age});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = getConnection()) {
            preparedStatement = connection.prepareStatement(REMOVE_USER_BY_ID);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = getConnection()) {
            preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userList.add(new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Connection connection = getConnection()) {
            statement = connection.createStatement();
            statement.execute(CLEAN_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return Util.openConnection();
    }
}
