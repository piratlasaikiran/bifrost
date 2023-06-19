package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBankAccountRequestDTO {

    @JsonProperty("nick_name")
    private String nickName;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("account_holders")
    private List<String> accountHolders;

    @JsonProperty("bank_name")
    private String bankName;

    @JsonProperty("current_balance")
    private Long currentBalance;

    @JsonProperty("atm_card")
    private Long atmCard;
}
