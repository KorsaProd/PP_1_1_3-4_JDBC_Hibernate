package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {

        UserService service = new UserServiceImpl();
        service.createUsersTable();
        service.saveUser("Иван", "Иванов", (byte)20);
        service.saveUser("Петя", "Петров", (byte) 40);
        service.saveUser("Вася", "Васечкин", (byte) 30);
        service.saveUser("Коля", "Неожиданно", (byte) 18);
        System.out.println(service.getAllUsers());
        service.cleanUsersTable();
        service.dropUsersTable();
        Util.closeSession();
    }
}
