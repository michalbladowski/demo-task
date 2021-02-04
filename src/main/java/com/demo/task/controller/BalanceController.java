package com.demo.task.controller;

import com.demo.task.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/balance")
public class BalanceController {
    private final RegistryService registryService;

    @Autowired
    public BalanceController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @GetMapping("/cumulative")
    @ResponseStatus(HttpStatus.OK)
    public double getCumulativeBalance() {
        return registryService.getCumulativeBalance();
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Double> getAllBalances() {
        return registryService.getAllBalances();
    }

}
