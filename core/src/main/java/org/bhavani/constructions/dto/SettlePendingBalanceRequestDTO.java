package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bhavani.constructions.dao.entities.models.TransactionMode;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlePendingBalanceRequestDTO {

    @JsonProperty("payer")
    private String payer;

    @JsonProperty("payee")
    private String payee;

    @JsonProperty("bank_account")
    private String bankAccount;

    @JsonProperty("mode")
    private TransactionMode mode;

    @JsonProperty("remarks")
    private String remarks;
}
