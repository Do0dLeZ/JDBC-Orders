package daoSqlImpl;

import dao.IProductDAO;
import db.DBConnection;
import entity.Product;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAOMySql implements IProductDAO {
    private Connection connection;
    private PreparedStatement statement;

    @Override
    public int add(Product product) {
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement("INSERT INTO products (name) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            int id = -1;
            if (resultSet.next())
                id = resultSet.getInt(1);

            statement.close();
            connection.commit();

            return id;
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return -1;
    }

    @Override
    public void update(Product product) {
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement("UPDATE AT products SET name = ? WHERE id = ?");
            statement.setInt(1, product.getId());
            statement.executeUpdate();

            statement.close();
            connection.commit();
        }catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void delete(Product product) {
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement("DELETE FROM products WHERE id = ?");
            statement.setInt(1, product.getId());
            statement.executeUpdate();

            statement.close();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public ArrayList<Product> getProductByName(String name) {
        try {
            connection = DBConnection.getConnection();

            statement = connection.prepareStatement("SELECT id, name " +
                    "FROM products " +
                    "WHERE name LIKE ?");
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                products.add(product);
            }

            statement.close();
            return products;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }

        return null;
    }

    @Override
    public Product getProductById(int id) {
        try {
            connection = DBConnection.getConnection();

            statement = connection.prepareStatement("SELECT name " +
                    "FROM products " +
                    "WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Product product = null;
            if (resultSet.next()) {
                 product = new Product(
                        id,
                        resultSet.getString("name")
                );
            }

            statement.close();

            return product;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
}
