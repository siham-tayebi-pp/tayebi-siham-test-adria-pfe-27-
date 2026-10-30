package net.tayebi.accountservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.tayebi.accountservice.enums.AccountStatus;

import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String customerReference;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String currency;
    private double balance;
}
