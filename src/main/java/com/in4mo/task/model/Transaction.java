package com.in4mo.task.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "TRANSFER")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @Column(name = "SOURCE")
    private int source;
    @Column(name = "DESTINATION")
    private int destination;
    @Column(name = "AMOUNT")
    private double amount;
    @Column(name = "EXECUTION_DATE")
    private Timestamp executionDate;
}