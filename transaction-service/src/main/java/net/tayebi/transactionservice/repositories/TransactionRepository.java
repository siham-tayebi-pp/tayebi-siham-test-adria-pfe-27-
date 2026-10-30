package net.tayebi.transactionservice.repositories;

import net.tayebi.transactionservice.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    public List<Transaction> findByAccountId(UUID accountId);
}
