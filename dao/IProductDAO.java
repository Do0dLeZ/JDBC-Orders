package dao;

import entity.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IProductDAO {
    int add(Product product);
    void update(Product product);
    void delete(Product product);

    ArrayList<Product> getProductByName(String name);
    Product getProductById(int id);
}
