package org.example.authorization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Authenticator {
    public static Connection connection;
    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Authenticator.connection =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotekaonline", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("jest lipa");
        }
    }

    public static void disconnect() {
        try {
            Authenticator.connection.close();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

}
