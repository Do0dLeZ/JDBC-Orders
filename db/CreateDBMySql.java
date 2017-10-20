package db;

import db.ICreateDB;
import db.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDBMySql implements ICreateDB {
    @Override
    public void createDataBase() {
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS clients " +
                    "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(35) NOT NULL," +
                    "surname VARCHAR(35) NOT NULL," +
                    "email VARCHAR(50)," +
                    "phone VARCHAR(15))");

            statement.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(35) UNIQUE NOT NULL)");

            statement.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(35)" +
                    "deal_date TIMESTAMP NOT NULL," +
                    "client_id INT NOT NULL," +
                    "product_id INT NOT NULL," +
                    "product_count INT," +
                    "FOREIGN KEY (client_id) REFERENCES clients(id)," +
                    "FOREIGN KEY (product_id) REFERENCES products(id))");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
