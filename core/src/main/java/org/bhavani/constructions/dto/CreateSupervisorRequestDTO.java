package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
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

    @NotNull
    @JsonProperty("admin")
    private boolean admin;

    @JsonProperty("company_mob_num")
    private Long companyMobileNumber;

    @JsonProperty("atm_card")
    private Long atmCardNumber;

    @JsonProperty("vehicle_num")
    private String vehicleNumber;

    @NotNull
    @JsonProperty("ot_pay")
    private int otPay;
}
