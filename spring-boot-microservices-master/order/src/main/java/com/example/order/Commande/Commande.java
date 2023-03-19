package com.example.order.Commande;

import javax.persistence.*;
import java.util.List;

@Entity
public class Commande {
    @Id
    @GeneratedValue
    private Long id;

    private Long cartId;

    private Double totalPrice;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> products;

    public Commande(Long id, Long cartId, Double totalPrice, List<OrderItem> products) {
        this.id = id;
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public Commande(Long id, Double totalPrice) {
        this.id = id;
        this.totalPrice = totalPrice;
    }

    public Commande() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void addProduct(OrderItem product) {
        this.products.add(product);
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }
}
