package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingBalanceResponseDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("account_name")
    private String accountName;

    @JsonProperty("transaction")
    private CreateTransactionRequestDTO transactionDetails;

    @JsonProperty("pending_balance")
    private Long pendingBalance;
}
