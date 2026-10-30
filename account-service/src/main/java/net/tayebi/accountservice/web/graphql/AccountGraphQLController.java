package net.tayebi.accountservice.web.graphql;

import lombok.AllArgsConstructor;
import net.tayebi.accountservice.entities.Account;
import net.tayebi.accountservice.services.AccountService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class AccountGraphQLController {
    private  AccountService service;

    @QueryMapping
    public List<Account> accounts() { return service.findAll(); }
    @QueryMapping public Account accountById(@Argument UUID id) { return service.findById(id); }
    @MutationMapping
    public Account createAccount(@Argument String customerReference, @Argument String currency, @Argument double balance) {
        return service.create(customerReference, currency, balance);
    }
}
