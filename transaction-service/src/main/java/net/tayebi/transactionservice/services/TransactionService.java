package net.tayebi.transactionservice.services;

import lombok.RequiredArgsConstructor;
import net.tayebi.transactionservice.entities.Transaction;
import net.tayebi.transactionservice.feign.AccountRestClient;
import net.tayebi.transactionservice.models.Account;
import net.tayebi.transactionservice.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRestClient accountClient;
    @Autowired
    private ApplicationEventPublisher events;

    @Value("${bank.transaction.max-amount}")
    private BigDecimal maxAmount;

    @Transactional
    public Transaction create(Transaction transaction) {
        // 1. Montant
        if (transaction.getAmount() == null || transaction.getAmount().signum() <= 0)
            throw new RuntimeException("Le montant doit être strictement positif");
        if (transaction.getAmount().compareTo(maxAmount) > 0)
            throw new RuntimeException("Montant supérieur au plafond : " + maxAmount);

        // 2. Compte (consultation du solde via Feign)
        Account account = accountClient.findById(transaction.getAccountId());
        if (account == null)
            throw new RuntimeException("Compte inexistant ou service indisponible");
        if (!"ACTIVE".equals(account.getStatus()))
            throw new RuntimeException("Compte bloqué");
        if (!account.getCurrency().equals(transaction.getCurrency()))
            throw new RuntimeException("Devise différente de celle du compte");
        if (account.getBalance() < transaction.getAmount().doubleValue())
            throw new RuntimeException("Solde insuffisant");

        // 3. Enregistrement
        transaction.setId(null);
        transaction.setCreatedAt(Instant.now());
        Transaction saved = transactionRepository.save(transaction);

        // 4. Mise à jour du solde
        accountClient.updateBalance(account.getId(),
                account.getBalance() - transaction.getAmount().doubleValue());

        // 5. Événement Kafka (publié après le commit)
//        events.publishEvent(new TransactionSavedEvent(saved));
        return saved;
    }



    /** Virement : débit du compte source, crédit du compte destination. */
    @Transactional
    public Transaction transfer(UUID sourceId, UUID destId, BigDecimal amount) {
        if (sourceId.equals(destId))
            throw new RuntimeException("Les comptes source et destination sont identiques");

        Account source = accountClient.findById(sourceId);
        Account dest = accountClient.findById(destId);
        if (source == null || dest == null)
            throw new RuntimeException("Compte source ou destination introuvable");
        if (!"ACTIVE".equals(dest.getStatus()))
            throw new RuntimeException("Compte destination bloqué");
        if (!source.getCurrency().equals(dest.getCurrency()))
            throw new RuntimeException("Devises différentes");

        // Débit de la source (toutes les règles sont vérifiées dans create)
        Transaction t = new Transaction();
        t.setAccountId(sourceId);
        t.setAmount(amount);
        t.setCurrency(source.getCurrency());
        t.setMerchantCategory("TRANSFER");
        Transaction saved = create(t);

        // Crédit de la destination
        accountClient.updateBalance(destId, dest.getBalance() + amount.doubleValue());
        return saved;
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> findByAccount(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
