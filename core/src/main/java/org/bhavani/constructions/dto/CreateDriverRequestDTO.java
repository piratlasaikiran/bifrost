package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CreateDriverRequestDTO {
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

    @NotNull
    @JsonProperty("ot_pay_day")
    private int otPayDay;

    @NotNull
    @JsonProperty("ot_pay_day_night")
    private int otPayDayNight;

    @JsonProperty("license")
    private byte[] license;

    @JsonProperty("aadhar")
    private byte[] aadhar;
}