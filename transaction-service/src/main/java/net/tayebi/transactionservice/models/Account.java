package net.tayebi.transactionservice.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.tayebi.transactionservice.enums.AccountStatus;

import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private UUID id;
    private String customerReference;
    private AccountStatus status;
    private String currency;
    private double balance;
}