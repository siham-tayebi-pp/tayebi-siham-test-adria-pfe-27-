package net.tayebi.accountservice.web.rest;

import net.tayebi.accountservice.entities.Account;
import net.tayebi.accountservice.enums.AccountStatus;
import net.tayebi.accountservice.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountRestController {
    @Autowired
    private AccountService service;

    @GetMapping
    public List<Account> all() { return service.findAll(); }
    @GetMapping("/{id}") public Account one(@PathVariable UUID id) { return service.findById(id); }
    @PostMapping
    public Account create(@RequestBody CreateAccountRequest r) {
        return service.create(r.customerReference(), r.currency(), r.balance());
    }
    @PutMapping("/{id}/balance") public Account balance(@PathVariable UUID id, @RequestBody BalanceRequest r) {
        return service.updateBalance(id, r.balance());
    }
    @PutMapping("/{id}/status") public Account status(@PathVariable UUID id, @RequestParam AccountStatus status) {
        return service.updateStatus(id, status);
    }
    @PutMapping("/{id}")
    public Account update(@PathVariable UUID id, @RequestBody Account account) {
        return service.update(id, account);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
    public record CreateAccountRequest(String customerReference, String currency, double balance) {}
    public record BalanceRequest(double balance) {}
}
