package com.demo.task.controller;

import com.demo.task.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registry")
public class RegistryController {
    private final RegistryService registryService;

    @Autowired
    public RegistryController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @GetMapping("/{id}/details")
    @ResponseStatus(HttpStatus.OK)
    public String getDetails(@PathVariable(value = "id") int id) {
        return registryService.getRegistryDetails(id);
    }

    @GetMapping("/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public double getBalance(@PathVariable(value = "id") int id) {
        return registryService.getBalanceOfRegistry(id);
    }

    @PutMapping("/{id}/recharge")
    @ResponseStatus(HttpStatus.OK)
    public String recharge(@PathVariable(value = "id") int id, @RequestParam(value = "amount") double amount) {
        registryService.rechargeRegistry(id, amount);
        return "Recharge of: " + amount + " has been applied";
    }

}
