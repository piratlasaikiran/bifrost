package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bhavani.constructions.dao.entities.models.AttendanceType;
import org.bhavani.constructions.dao.entities.models.EmployeeType;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeAttendanceRequestDTO {

    @JsonProperty("site")
    private String site;

    @JsonProperty("employee_name")
    private String employeeName;

    @JsonProperty("employee_type")
    private EmployeeType employeeType;

    @JsonProperty("entered_by")
    private String enteredBy;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("attendance_date")
    private LocalDate attendanceDate;

    @JsonProperty("attendance_type")
    private AttendanceType attendanceType;

    @JsonProperty("make_transaction")
    private boolean makeTransaction;

    @JsonProperty("bank_account")
    private String bankAccount;
}
