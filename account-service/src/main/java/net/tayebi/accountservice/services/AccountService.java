package net.tayebi.accountservice.services;

import lombok.AllArgsConstructor;
import net.tayebi.accountservice.entities.Account;
import net.tayebi.accountservice.enums.AccountStatus;
import net.tayebi.accountservice.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
//@AllArgsConstructor
public class AccountService {
    @Autowired
    private  AccountRepository accountRepository;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
    public void delete(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable : " + id));

        accountRepository.delete(account);
    }

    public Account findById(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Compte introuvable : " + id));
    }

    public Account create(String customerReference, String currency, double balance) {
        return accountRepository.save(Account.builder().customerReference(customerReference)
                .currency(currency).balance(balance).status(AccountStatus.ACTIVE).build());
    }
    public Account update(UUID id, Account account) {
        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable : " + id));

        if (account.getCustomerReference() != null) {
            existing.setCustomerReference(account.getCustomerReference());
        }

        if (account.getStatus() != null) {
            existing.setStatus(account.getStatus());
        }

        if (account.getCurrency() != null) {
            existing.setCurrency(account.getCurrency());
        }

        if (account.getBalance() != null) {
            existing.setBalance(account.getBalance());
        }

        return accountRepository.save(existing);
    }

    public Account updateBalance(UUID id, double newBalance) {
        Account a = findById(id);
        a.setBalance(newBalance);
        return accountRepository.save(a);
    }

    public Account updateStatus(UUID id, AccountStatus status) {
        Account a = findById(id);
        a.setStatus(status);
        return accountRepository.save(a);
    }
}
