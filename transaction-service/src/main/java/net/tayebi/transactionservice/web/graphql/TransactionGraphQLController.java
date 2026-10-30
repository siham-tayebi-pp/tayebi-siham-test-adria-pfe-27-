package net.tayebi.transactionservice.web.graphql;

import net.tayebi.transactionservice.entities.Transaction;
import net.tayebi.transactionservice.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
public class TransactionGraphQLController {

    @Autowired
    private TransactionService transactionService;

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.findAll();
    }

    @QueryMapping
    public List<Transaction> transactionsByAccount(@Argument UUID accountId) {
        return transactionService.findByAccount(accountId);
    }

    @MutationMapping
    public Transaction createTransaction(@Argument UUID accountId,
                                         @Argument Double amount,
                                         @Argument String currency,
                                         @Argument String merchantCategory,
                                         @Argument String country) {
        Transaction t = new Transaction();
        t.setAccountId(accountId);
        t.setAmount(BigDecimal.valueOf(amount));
        t.setCurrency(currency);
        t.setMerchantCategory(merchantCategory);
        t.setCountry(country);
        return transactionService.create(t);
    }

    @MutationMapping
    public Transaction transfer(@Argument UUID sourceId,
                                @Argument UUID destinationId,
                                @Argument Double amount) {
        return transactionService.transfer(sourceId, destinationId, BigDecimal.valueOf(amount));
    }
}