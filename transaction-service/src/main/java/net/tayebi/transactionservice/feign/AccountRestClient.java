package net.tayebi.transactionservice.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import net.tayebi.transactionservice.models.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import net.tayebi.*;
import java.util.UUID;




@FeignClient(name = "account-service")
public interface AccountRestClient {
    @GetMapping("/accounts/{id}")
    @CircuitBreaker(name = "account-service", fallbackMethod = "accountFallback")
    Account findById(@PathVariable UUID id);

    @PutMapping("/accounts/{id}/balance")
    @CircuitBreaker(name = "account-service", fallbackMethod = "balanceFallback")
    Account updateBalance(@PathVariable UUID id, @RequestBody Double balance);

    default Account accountFallback(UUID id, Throwable t) {
        return null;
    }
    default Account balanceFallback(UUID id, Double r, Throwable t) {
        return null;
    }
}

