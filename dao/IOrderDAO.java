package dao;

import entity.Order;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface IOrderDAO {
    int add(Order order) throws SQLException;
    void update(Order order) throws SQLException;
    void delete(Order order);

    ArrayList<Order> getOrdersByClient(int clientId) throws SQLException;
    ArrayList<Order> getOrdersByProduct(int productId) throws SQLException;
    ArrayList<Order> getOrderBetweenDates(Timestamp beginDate, Timestamp endDate) throws SQLException;

}
