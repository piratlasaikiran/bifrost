package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bhavani.constructions.dao.entities.models.CommodityType;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateVendorAttendanceRequestDTO {

    @JsonProperty("site")
    private String site;

    @JsonProperty("vendor_id")
    private String vendorId;

    @JsonProperty("entered_by")
    private String enteredBy;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("attendance_date")
    private LocalDate attendanceDate;

    @JsonProperty("commodity_attendance")
    private Map<CommodityType, Integer> commodityAttendance;

    @JsonProperty("make_transaction")
    private boolean makeTransaction;

    @JsonProperty("bank_account")
    private String bankAccount;
}
