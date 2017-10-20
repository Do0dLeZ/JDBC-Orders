package dao;

import entity.Client;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IClientDAO {
    int add(Client client);
    void update(Client client) throws SQLException;
    void delete(int id) throws SQLException;

    Client getClientById(int id);
    ArrayList<Client> getByName(String name) throws SQLException;
    ArrayList<Client> getByFullName(String name, String surname);
    ArrayList<Client> getByEmail(String email);
    ArrayList<Client> getByPhone(String phone) throws SQLException;
}
