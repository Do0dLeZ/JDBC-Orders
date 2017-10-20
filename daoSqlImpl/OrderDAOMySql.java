package daoSqlImpl;

import dao.IClientDAO;
import dao.IOrderDAO;
import dao.IProductDAO;
import db.DBConnection;
import entity.Client;
import entity.Order;
import entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class OrderDAOMySql implements IOrderDAO {
    private Connection connection;
    private PreparedStatement statement;

    @Override
    public int add(Order order) {
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);
            int id = -1;

            statement = connection.prepareStatement("INSERT INTO orders (name, deal_date, client_id, product_id, product_count)" +
                                                        "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, order.getName());
            statement.setTimestamp(2, new Timestamp(order.getDealDate().getTime()));
            statement.setInt(3, order.getClient().getId());
            statement.setInt(4, order.getProduct().getId());
            statement.setInt(5, order.getProductCount());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next())
                id = resultSet.getInt(1);

            statement.close();
            connection.commit();

            return id;
        } catch (Exception ex){
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("some thing went wrong...");
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
    public void update(Order order) throws SQLException {
        //TODO update order
    }

    @Override
    public void delete(Order order) {
        //TODO delete order
    }

    @Override
    public ArrayList<Order> getOrdersByClient(int clientId) throws SQLException {
        connection = DBConnection.getConnection();
        statement = connection.prepareStatement("SELECT o.id, o.name, o.product_count, o.deal_date," +
                                                          " p.id, p.name " +
                                                    "FROM orders as o " +
                                                    "LEFT JOIN products AS p ON o.product_id = p.id " +
                                                    "WHERE o.client_id = ?");
        statement.setInt(1, clientId);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Order> orders = new ArrayList<>();
        IClientDAO clientDAO = new ClientDAOMySQL();
        Client client = clientDAO.getClientById(clientId);

        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getInt("p.id"),
                    resultSet.getString("p.name")
            );
            Order order = new Order(
                    resultSet.getInt("o.id"),
                    resultSet.getString("o.name"),
                    resultSet.getTimestamp("o.deal_date"),
                    client,
                    product,
                    resultSet.getInt("o.product_count")
            );
            orders.add(order);
        }

        statement.close();
        resultSet.close();
        if (connection != null)
            connection.close();

        return orders;
    }

    @Override
    public ArrayList<Order> getOrdersByProduct(int productId) throws SQLException {
        connection = DBConnection.getConnection();
        statement = connection.prepareStatement("SELECT o.id, o.name, o.product_count, o.deal_date," +
                                                          " c.id, c.name, c.surname, c.email, c.phone " +
                                                    "FROM orders as o " +
                                                    "LEFT JOIN clients AS c ON o.client_id = c.id " +
                                                    "WHERE o.product_id = ?");
        statement.setInt(1, productId);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Order> orders = new ArrayList<>();
        IProductDAO clientDAO = new ProductDAOMySql();
        Product product = clientDAO.getProductById(productId);

        while (resultSet.next()) {
            Client client = new Client(
                    resultSet.getInt("c.id"),
                    resultSet.getString("c.name"),
                    resultSet.getString("c.surname"),
                    resultSet.getString("c.email"),
                    resultSet.getString("c.phone")
            );
            Order order = new Order(
                    resultSet.getInt("o.id"),
                    resultSet.getString("o.name"),
                    resultSet.getTimestamp("o.deal_date"),
                    client,
                    product,
                    resultSet.getInt("o.product_count")
            );
            orders.add(order);
        }

        statement.close();
        resultSet.close();
        if (connection != null)
            connection.close();

        return orders;
    }

    @Override
    public ArrayList<Order> getOrderBetweenDates(Timestamp beginDate, Timestamp endDate) throws SQLException {
        connection = DBConnection.getConnection();
        statement = connection.prepareStatement("SELECT o.id, o.name, o.product_count, o.deal_date," +
                                                            "p.id, p.name," +
                                                            "c.id, c.name, c.surname, c.email, c.phone " +
                                                    "FROM orders as o " +
                                                    "LEFT JOIN clients AS p ON o.client_id = c.id " +
                                                    "LEFT JOIN products AS p ON o.product_id = p.id " +
                                                    "WHERE o.deal_date BETWEEN ? AND ?");

        statement.setTimestamp(1, beginDate);
        statement.setTimestamp(2, endDate);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getInt("p.id"),
                    resultSet.getString("p.name")
            );
            Client client = new Client(
                    resultSet.getInt("c.id"),
                    resultSet.getString("c.name"),
                    resultSet.getString("c.surname"),
                    resultSet.getString("c.email"),
                    resultSet.getString("c.phone")
            );
            Order order = new Order(
                    resultSet.getInt("o.id"),
                    resultSet.getString("o.name"),
                    resultSet.getTimestamp("o.deal_date"),
                    client,
                    product,
                    resultSet.getInt("o.product_count")
            );
            orders.add(order);
        }

        resultSet.close();
        statement.close();
        if (connection != null)
            connection.close();

        return orders;
    }
}
