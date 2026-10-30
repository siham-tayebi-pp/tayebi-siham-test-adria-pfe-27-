
package net.tayebi.transactionservice.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import net.tayebi.transactionservice.models.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "account-service")
public interface AccountRestClient {

    @GetMapping("/accounts")
    @CircuitBreaker(name = "account-service", fallbackMethod = "accountsFallback")
    List<Account> findAll();

    @GetMapping("/accounts/{id}")
    @CircuitBreaker(name = "account-service", fallbackMethod = "accountFallback")
    Account findById(@PathVariable UUID id);

    @PutMapping("/accounts/{id}/balance")
    @CircuitBreaker(name = "account-service", fallbackMethod = "balanceFallback")
    Account updateBalance(@PathVariable UUID id, @RequestBody Double balance);


    // Fallback pour findAll()
    default List<Account> accountsFallback(Throwable t) {
        System.out.println("Fallback findAll : " + t.getMessage());
        return List.of();
    }

    // Fallback pour findById(UUID id)
    default Account accountFallback(UUID id, Throwable t) {
        System.out.println("Fallback findById : " + t.getMessage());
        return null;
    }

    // Fallback pour updateBalance(UUID id, Double balance)
    default Account balanceFallback(UUID id, Double balance, Throwable t) {
        System.out.println("Fallback updateBalance : " + t.getMessage());
        return null;
    }
}
