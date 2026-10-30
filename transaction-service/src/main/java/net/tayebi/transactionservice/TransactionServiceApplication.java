package net.tayebi.transactionservice;

import net.tayebi.transactionservice.entities.Transaction;
import net.tayebi.transactionservice.feign.AccountRestClient;
import net.tayebi.transactionservice.models.Account;
import net.tayebi.transactionservice.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class TransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(TransactionRepository transactionRepository, AccountRestClient accountClient) {
        return args -> {
            System.out.println(">>> Runner transactions démarré");
            if (transactionRepository.count() > 0) {
                System.out.println(">>> Déjà des transactions, rien à faire");
                return;
            }

            List<Account> accounts = null;
            for (int i = 1; i <= 10; i++) {                       // 10 essais, 3 s d'écart
                try {
                    accounts = accountClient.findAll();
                    if (accounts != null && !accounts.isEmpty()) break;
                    System.out.println(">>> Essai " + i + " : liste vide ou null (fallback ?)");
                } catch (Exception e) {
                    System.out.println(">>> Essai " + i + " : " + e.getMessage());
                }
                Thread.sleep(3000);
            }
            if (accounts == null || accounts.isEmpty()) {
                System.out.println(">>> Aucun compte récupéré : transactions non créées");
                return;
            }

            String[] categories = {"FOOD", "TRAVEL", "TECH", "HEALTH", "GIFT"};
            String[] countries  = {"MA", "FR", "US", "ES", "DE"};
            Random rnd = new Random(42);

            for (Account a : accounts) {
                for (int i = 0; i < 4; i++) {
                    transactionRepository.save(Transaction.builder()
                            .accountId(a.getId())
                            .amount(BigDecimal.valueOf(10 + rnd.nextInt(900)))
                            .currency(a.getCurrency())
                            .merchantCategory(categories[rnd.nextInt(categories.length)])
                            .country(countries[rnd.nextInt(countries.length)])
                            .createdAt(Instant.now().minus(rnd.nextInt(30), ChronoUnit.DAYS))
                            .build());
                }
            }
            System.out.println(">>> " + transactionRepository.count() + " transactions créées");
        };
    }
}
