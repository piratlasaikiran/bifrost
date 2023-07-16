package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSupervisorRequestDTO {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("personal_mobile_num")
    private Long personalMobileNumber;

    @NotNull
    @JsonProperty( "bank_ac")
    private String bankAccountNumber;

    @NotNull
    @JsonProperty("salary")
    private int salary;

    @JsonProperty("company_mob_num")
    private Long companyMobileNumber;

    @JsonProperty("atm_card")
    private Long atmCardNumber;

    @NotNull
    @JsonProperty("ot_pay")
    private int otPay;
}
