package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.*;
import org.bhavani.constructions.dao.entities.models.VehicleTaxEnum;

import java.io.InputStream;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadVehicleTaxRequestDTO {

    @JsonProperty("vehicle_num")
    private String vehicleNumber;

    @JsonProperty("tax_type")
    private VehicleTaxEnum taxType;

    @JsonProperty("renewal_amount")
    private int amount;

    @JsonProperty("tax_doc")
    private InputStream taxReceipt;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("validity_start")
    private LocalDate validityStartDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("validity_end")
    private LocalDate validityEndDate;
}
