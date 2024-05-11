package com.example.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "user_name")
    private String username;
    private String name;
    private String surname;
    private String phoneNumber;
    private String password;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Order> orders;
}
