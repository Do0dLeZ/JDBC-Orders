package db;

import java.sql.*;

public class DBConnection {
    private static DBConnection instance;
    private static Connection connection;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "orders";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SHOW DATABASES LIKE 'orders'");
        if (!result.next()) {
            statement.execute("CREATE DATABASE orders");

            statement.execute("USE orders");

            ICreateDB createDB = new CreateDBMySql(); //Not so good
            createDB.createDataBase();
        }

        statement.close();
        if (connection != null)
            connection.close();
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
            connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
            return connection;
        } else {
            connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
            return connection;
        }
    }
}
