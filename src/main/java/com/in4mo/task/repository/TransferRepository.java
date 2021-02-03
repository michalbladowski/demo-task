package com.in4mo.task.repository;

import com.in4mo.task.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends CrudRepository<Transaction, Long> {

}
