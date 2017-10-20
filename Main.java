import dao.IClientDAO;
import dao.IOrderDAO;
import dao.IProductDAO;
import daoSqlImpl.ClientDAOMySQL;
import daoSqlImpl.OrderDAOMySql;
import daoSqlImpl.ProductDAOMySql;
import entity.Client;
import entity.Order;
import entity.Product;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

//Методы можна было бы вынести в сервисы, было бы два сервиса тогда, один добавляет другой возвращает данные.
//Но как-то .... потом доделать можно будет =)
public class Main {
    private static IOrderDAO orderDAO = new OrderDAOMySql();
    private static IClientDAO clientDAO = new ClientDAOMySQL();
    private static IProductDAO productDAO = new ProductDAOMySql();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            String s = scanner.nextLine();
            switch (s) {
                case "1":
                    addClient(scanner);
                    break;
                case "2":
                    addProduct(scanner);
                    break;
                case "3":
                    addOrder(scanner);
                    break;
                case "4":
                    getOrders(scanner);
            }
        }
    }

    private static void getOrders(Scanner scanner) {
        System.out.println("1. Get by client id.\n" +
                "2. Get by product id.\n" +
                "3. Get between dates.");
        String s = scanner.nextLine();
        switch (s) {
            case "1":
                getByClient(scanner);
                break;
            case "2":
                getByProduct(scanner);
                break;
            case "3":
                getByDates(scanner);
                break;
        }
    }

    private static void getByDates(Scanner scanner) {
        System.out.println("Enter begin date like 01.01.2017 'day.month.year'");
        String beginDate = scanner.nextLine();
        System.out.println("Enter end date like 01.01.2017 'day.month.year'");
        String endDate = scanner.nextLine();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
        try {
            Date begin = format.parse(beginDate);
            Date end = format.parse(endDate);
            for (Order order : orderDAO.getOrderBetweenDates(new Timestamp(begin.getTime()), new Timestamp(end.getTime()))) {
                System.out.println(order);
            }
        } catch (ParseException | SQLException e) {
            System.out.println("O M G no data here =) cause ERRRRRROOOOORRRR");
        }
    }

    private static void getByProduct(Scanner scanner) {
        System.out.println("Enter product id:");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            for (Order order : orderDAO.getOrdersByProduct(id)) {
                System.out.println(order.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getByClient(Scanner scanner) {
        System.out.println("Enter client id:");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            for (Order order : orderDAO.getOrdersByClient(id)) {
                System.out.println(order.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addOrder(Scanner scanner) {
        Order order = new Order();

        System.out.println("Enter client email or 'Name Surname':");
        String input = scanner.nextLine();
        order.setClient(getClientForNewOrder(scanner, input));

        System.out.println("Enter product name:");
        input = scanner.nextLine();
        order.setProduct(getProductForNewOrder(scanner, input));

        System.out.println("Enter count of product:");
        order.setProductCount(Integer.parseInt(scanner.nextLine()));

        System.out.println("Enter name of order");
        order.setName(scanner.nextLine());

        order.setDealDate(new Date());

        try {
            orderDAO.add(order);
        } catch (SQLException e) {
            System.out.println("Can not add order... had no time to say WHYYYYYYY =)");
        }
    }

    private static Product getProductForNewOrder(Scanner scanner, String input) {
        ArrayList<Product> products;
        products = productDAO.getProductByName(input);
        if (products.size() == 0) {
            System.out.println("There are no product in data base with input name. Please check your input data (put 1). Or create new product (put 2).");
            switch (scanner.nextLine()) {
                case "1":
                    addOrder(scanner);
                    break;
                case "2":
                    return addProduct(scanner);
            }
        } else if (products.size() == 1) {
            return products.get(0);
        } else {
            System.out.println("Select from list your product by number.");
            for (Product product : products) {
                System.out.println(products.indexOf(product) + ". " + product.toString());
            }
            int numberOfProduct = Integer.parseInt(scanner.nextLine());
            return products.get(numberOfProduct);
        }
        System.out.println("Something went wrong... smile =)");
        return null;
    }

    private static Client getClientForNewOrder(Scanner scanner, String input) {
        ArrayList<Client> clients;
        if (input.contains("@")) {
            clients = clientDAO.getByEmail(input);
        } else {
            String name = input.substring(0, input.indexOf(" "));
            String surname = input.substring(input.indexOf(" ") + 1);
            clients = clientDAO.getByFullName(name, surname);
        }
        return getClient(scanner, clients);
    }

    private static Client getClient(Scanner scanner, ArrayList<Client> clients) {
        if (clients.size() == 0) {
            System.out.println("There are no client in data base. Please check your input data (put 1). Or create new client (put 2).");
            switch (scanner.nextLine()) {
                case "1":
                    addOrder(scanner);
                    break;
                case "2":
                    return addClient(scanner);
            }
        } else if (clients.size() == 1) {
            return clients.get(0);
        } else {
            System.out.println("Choose your client from list, by putting his number.");
            for (Client client : clients) {
                System.out.println(clients.indexOf(client) + ". " + client.toString());
            }
            int numberOfClient = Integer.parseInt(scanner.nextLine());
            return clients.get(numberOfClient);
        }
        return null;
    }

    private static Product addProduct(Scanner scanner) {
        Product product = new Product();
        System.out.println("Enter product name");
        product.setName(scanner.nextLine());

        int id = productDAO.add(product);
        System.out.println("Product was added with id '" + id + "'.");
        return productDAO.getProductById(id);
    }

    private static Client addClient(Scanner scanner) {
        Client client = new Client();
        System.out.println("Enter first name:");
        client.setName(scanner.nextLine());
        System.out.println("Enter last name:");
        client.setSurname(scanner.nextLine());
        System.out.println("Enter email:");
        client.setEmail(scanner.nextLine());
        System.out.println("Enter phone number:");
        client.setPhoneNumber(scanner.nextLine());

        int id = clientDAO.add(client);
        System.out.println("Client with id '" + id + "' was added.");
        return clientDAO.getClientById(id);
    }

    private static void showMenu() {
        System.out.println("1. Add client.\n" +
                "2. Add product.\n" +
                "3. Add order.\n" +
                "4. Get orders");
    }
}
