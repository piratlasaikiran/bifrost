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
public class CreateVehicleRequestDTO {

    @NotNull
    @JsonProperty("vehicle_num")
    private String vehicleNumber;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("chassis_num")
    private String chassisNumber;

    @JsonProperty("engine_num")
    private String engineNumber;

    @NotNull
    @JsonProperty("vehicle_class")
    private String vehicleClass;

    @JsonProperty("insurance_provider")
    private String insuranceProvider;

    @JsonProperty("finance_provider")
    private String financeProvider;
}
