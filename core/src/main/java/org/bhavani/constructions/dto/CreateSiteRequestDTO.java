package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Builder;
import lombok.Getter;
import org.bhavani.constructions.dao.entities.models.SiteStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CreateSiteRequestDTO {

    @NotNull
    @JsonProperty("site_name")
    private String siteName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("site_status")
    private SiteStatus siteStatus;

    @JsonProperty("vehicles")
    private List<String> vehicles;

    @JsonProperty("supervisors")
    private List<String> supervisors;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("work_start_date")
    private LocalDate workStartDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("work_end_date")
    private LocalDate workEndDate;
}
