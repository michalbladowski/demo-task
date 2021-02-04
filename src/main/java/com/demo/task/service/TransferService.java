package com.demo.task.service;

import com.demo.task.model.Registry;
import com.demo.task.model.Transaction;
import com.demo.task.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransferService {
    private static final Logger LOG = LoggerFactory.getLogger(TransferService.class);
    private final TransferRepository transferRepository;
    private final RegistryService registryService;

    @Autowired
    public TransferService(TransferRepository transferRepository, RegistryService registryService) {
        this.transferRepository = transferRepository;
        this.registryService = registryService;
    }

    public List<Transaction> getAllTransfers() {
        return (List<Transaction>) transferRepository.findAll();
    }

    @Transactional
    public void transfer(int fromId, int toId, double amount) {
        if(fromId <= 0 || toId <= 0) {
            throw new IllegalArgumentException("Incorrect source/destination registry id to execute transfer");
        } else {
            Registry sourceRegistry = registryService.getRegistry(fromId);
            Registry destinationRegistry = registryService.getRegistry(toId);

            // update source registry
            sourceRegistry.setBalance(sourceRegistry.getBalance() - amount);
            registryService.saveRegistry(sourceRegistry);
            LOG.info("Updated source registry of id " + fromId);

            // update destination registry
            destinationRegistry.setBalance(destinationRegistry.getBalance() + amount);
            registryService.saveRegistry(destinationRegistry);
            LOG.info("Updated destination registry of id " + toId);

            // log transaction
            Transaction transaction = new Transaction();
            transaction.setSource(fromId);
            transaction.setDestination(toId);
            transaction.setAmount(amount);
            transaction.setExecutionDate(new Timestamp(System.currentTimeMillis()));
            transferRepository.save(transaction);
            LOG.info("Logged new transaction");
        }
    }
}
