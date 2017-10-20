package daoSqlImpl;

import dao.IClientDAO;
import db.DBConnection;
import entity.Client;

import java.sql.*;
import java.util.ArrayList;

public class ClientDAOMySQL implements IClientDAO {
    private Connection connection;
    private PreparedStatement statement;

    @Override
    public int add(Client client) {
        try {
            connection = DBConnection.getConnection();

            statement = connection.prepareStatement("INSERT " +
                    "INTO clients (name, surname, email, phone) " +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, client.getName());
            statement.setString(2, client.getSurname());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPhoneNumber());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            int id = -1;
            if (resultSet.next())
                id = resultSet.getInt(1);

            statement.close();
            resultSet.close();

            return id;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public void update(Client client) throws SQLException {
        if (client.getId() != -1) {
            connection = DBConnection.getConnection();

            statement = connection.prepareStatement("UPDATE clients SET name = ?, surname = ?, email = ?, phone = ? " +
                    "WHERE id = ?");

            statement.setString(1, client.getName());
            statement.setString(2, client.getSurname());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPhoneNumber());
            //we can find client by email and phone number if we stand it like UNIQUE, but not this time =)
            statement.setInt(5, client.getId());

            statement.executeUpdate();

            statement.close();
            connection.close();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        connection = DBConnection.getConnection();

        statement = connection.prepareStatement("DELETE FROM clients WHERE id = ?");
        statement.setInt(1, id);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    @Override
    public Client getClientById(int id) {
        try {
            connection = DBConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT id, name, surname, email, phone " +
                    "FROM clients " +
                    "WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Client client = null;
            if (resultSet.next()) {
                 client = new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
            }

            resultSet.close();
            statement.close();

            return client;
        } catch (SQLException ex) {
            ex.printStackTrace();
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
    public ArrayList<Client> getByName(String name) throws SQLException {
        connection = DBConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT id, name, surname, email, phone " +
                "FROM clients " +
                "WHERE name = ?");
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();

        statement.close();
        if (connection != null)
            connection.close();

        return initClients(resultSet);
    }

    @Override
    public ArrayList<Client> getByFullName(String name, String surname) {
        try {
            connection = DBConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT id, name, surname, email, phone " +
                    "FROM clients " +
                    "WHERE name = ? AND surname = ?");
            statement.setString(1, name);
            statement.setString(2, surname);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Client> clients = initClients(resultSet);

            resultSet.close();
            statement.close();

            return clients;
        } catch (SQLException ex){
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
    public ArrayList<Client> getByEmail(String email) {
        try {
            connection = DBConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT id, name, surname, email, phone " +
                    "FROM clients " +
                    "WHERE email = ?");

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            statement.close();

            return initClients(resultSet);
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
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Client> getByPhone(String phone) throws SQLException {
        connection = DBConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT id, name, surname, email, phone " +
                "FROM clients " +
                "WHERE phone = ?");

        statement.setString(1, phone);
        ResultSet resultSet = statement.executeQuery();

        statement.close();
        if (connection != null)
            connection.close();

        return initClients(resultSet);
    }

    private ArrayList<Client> initClients(ResultSet resultSet) throws SQLException {
        ArrayList<Client> clients = new ArrayList<>();

        while (resultSet.next()) {
            Client client = new Client(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getString("email"),
                    resultSet.getString("phone")
            );
            clients.add(client);
        }
        return clients;
    }
}
