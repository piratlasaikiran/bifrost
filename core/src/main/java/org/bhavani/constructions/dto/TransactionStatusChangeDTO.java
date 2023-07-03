package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatusChangeDTO {

    @JsonProperty("transaction_id")
    private Long transactionId;

    @JsonProperty("desired_status")
    private TransactionStatus desiredStatus;

    @JsonProperty("message")
    private String message;
}
