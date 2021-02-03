package com.in4mo.task.repository;

import com.in4mo.task.model.Registry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistryRepository extends CrudRepository<Registry, Integer> {
    @Query("SELECT SUM(r.balance) FROM Registry r")
    double getCumulativeBalance();
}
