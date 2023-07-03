package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bhavani.constructions.dao.entities.models.AccountType;
import org.bhavani.constructions.dao.entities.models.TransactionType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassBookResponseDTO {

    @JsonProperty("account_name")
    private String accountName;

    @JsonProperty("account_type")
    private AccountType accountType;

    @JsonProperty("transaction")
    private CreateTransactionRequestDTO transactionDetails;

    @JsonProperty("current_balance")
    private Long currentBalance;

    @JsonProperty("transaction_amount")
    private Long transactionAmount;

    @JsonProperty("transaction_type")
    private TransactionType transactionType;
}
