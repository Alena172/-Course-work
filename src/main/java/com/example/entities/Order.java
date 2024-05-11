package com.example.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int totalCost;
    private String status;
    private String date;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany
    private List<OrderItem> orderItems;
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }
}
