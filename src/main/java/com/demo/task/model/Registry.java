package com.demo.task.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "REGISTRY")
public class Registry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "BALANCE")
    private double balance;
    @Column(name = "CUSTOMER_ID")
    private String customerId;
}

