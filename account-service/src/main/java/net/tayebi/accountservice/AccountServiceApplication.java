package net.tayebi.accountservice;

import net.tayebi.accountservice.entities.Account;
import net.tayebi.accountservice.enums.AccountStatus;
import net.tayebi.accountservice.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(AccountRepository accountRepository) {
        return args -> {
            if (accountRepository.count() > 0) return;   // évite les doublons

            accountRepository.saveAll(List.of(
                    Account.builder().customerReference("siham").currency("EUR").balance(5000.0).status(AccountStatus.ACTIVE).build(),
                    Account.builder().customerReference("imane").currency("EUR").balance(2500.0).status(AccountStatus.ACTIVE).build(),
                    Account.builder().customerReference("ali").currency("USD").balance(1800.0).status(AccountStatus.ACTIVE).build(),
                    Account.builder().customerReference("omar").currency("EUR").balance(300.0).status(AccountStatus.BLOCKED).build(),
                    Account.builder().customerReference("hassane").currency("MAD").balance(12000.0).status(AccountStatus.ACTIVE).build(),
                    Account.builder().customerReference("fouzia").currency("MAD").balance(12000.0).status(AccountStatus.ACTIVE).build(),
                    Account.builder().customerReference("hamid").currency("MAD").balance(12000.0).status(AccountStatus.ACTIVE).build()
            ));

            accountRepository.findAll().forEach(
                    a -> System.out.println("Compte created by succes : " + a.getId() + " | " + a.getCustomerReference()
                            + " | " + a.getCurrency() + " | " + a.getBalance() + " | " + a.getStatus()));
        };
    }
}

