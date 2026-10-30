package net.tayebi.transactionservice.web.rest;

import net.tayebi.transactionservice.entities.Transaction;
import net.tayebi.transactionservice.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/transactions")
public class TransactionRestController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> findByAccount(@PathVariable UUID accountId) {
        return transactionService.findByAccount(accountId);
    }

    @PostMapping
    public Transaction create(@RequestBody Transaction transaction) {
        return transactionService.create(transaction);
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestParam UUID source,
                                @RequestParam UUID destination,
                                @RequestParam BigDecimal amount) {
        return transactionService.transfer(source, destination, amount);
    }
}