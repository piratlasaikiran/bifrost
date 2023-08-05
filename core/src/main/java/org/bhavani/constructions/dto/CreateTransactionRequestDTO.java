package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequestDTO {

    @JsonProperty("transaction_id")
    private Long transactionId;

    @JsonProperty("source")
    private String source;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("purpose")
    private TransactionPurpose purpose;

    @JsonProperty("site")
    private String site;

    @JsonProperty("vehicle_number")
    private String vehicleNumber;

    @JsonProperty("remarks")
    private String remarks;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("transaction_date")
    private LocalDate transactionDate;

    @JsonProperty("status")
    private TransactionStatus status;

    @JsonProperty("mode")
    private TransactionMode mode;

    @JsonProperty("bank_account")
    private String bankAccount;
}
