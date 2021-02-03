package com.in4mo.task.service;

import com.in4mo.task.model.Registry;
import com.in4mo.task.repository.RegistryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RegistryService {
    private static final Logger LOG = LoggerFactory.getLogger(RegistryService.class);
    private final RegistryRepository registryRepository;

    @Autowired
    public RegistryService(RegistryRepository registryRepository) {
        this.registryRepository = registryRepository;
    }

    public Registry getRegistry(int registryId) {
        Optional<Registry> registryOpt = registryRepository.findById(registryId);
        if(registryOpt.isPresent()) {
            return registryOpt.get();
        } else {
            LOG.error("Registry does not exist");
            throw new IllegalArgumentException();
        }
    }

    public void saveRegistry(Registry registry) {
        if(registry != null) {
            registryRepository.save(registry);
            LOG.info("Registry successfully saved");
        }
    }

    public void rechargeRegistry(int id, double amount) {
        Registry registry = getRegistry(id);
        double currentBalance = registry.getBalance();
        registry.setBalance(currentBalance + amount);
        registryRepository.save(registry);
    }

    public double getBalanceOfRegistry(int id) {
        return getRegistry(id).getBalance();
    }

    public String getRegistryDetails(int id) {
        return getRegistry(id).toString();
    }

    public double getCumulativeBalance() {
        return registryRepository.getCumulativeBalance();
    }

    public Map<String, Double> getAllBalances() {
        return StreamSupport.stream(registryRepository.findAll().spliterator(), false).collect(Collectors.toMap(Registry::getName, Registry::getBalance));
    }

}
