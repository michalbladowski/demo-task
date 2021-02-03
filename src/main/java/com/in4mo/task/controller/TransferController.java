package com.in4mo.task.controller;

import com.in4mo.task.model.Transaction;
import com.in4mo.task.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransferController {
    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/transfers")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getAllTransfers() {
        return transferService.getAllTransfers();
    }

    @PostMapping("/transfer/from/{fromId}/to/{toId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String transfer(@PathVariable(value = "fromId") int fromId, @PathVariable(value = "toId") int toId, @RequestParam(value = "amount") double amount) {
        transferService.transfer(fromId, toId, amount);
        return "Transferred: " + amount + " from registry of id: " + fromId + " to registry of id: " + toId;
    }

}
