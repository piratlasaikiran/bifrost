package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateAssetLocationRequestDTO {

    @JsonProperty("asset_type")
    private String assetType;

    @JsonProperty("asset_name")
    private String assetName;

    @JsonProperty("location")
    private String location;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("end_date")
    private LocalDate endDate;
}
