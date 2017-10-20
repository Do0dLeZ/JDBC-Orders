package entity;

import java.util.Date;

public class Order {
    private int id;
    private String name;
    private Date dealDate;
    private Client client;
    private Product product;
    private Integer productCount;

    public Order() {
    }

    public Order(int id, String name, Date dealDate, Client client, Product product, Integer productCount) {
        this.id = id;
        this.name = name;
        this.dealDate = dealDate;
        this.client = client;
        this.product = product;
        this.productCount = productCount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "name='" + name + '\'' +
                ", dealDate=" + dealDate +
                ", client=" + client +
                ", product=" + product +
                ", productCount=" + productCount +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
}
