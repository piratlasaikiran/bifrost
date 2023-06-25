package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.DriverEntityDao;
import org.bhavani.constructions.dao.api.EmployeeAttendanceDao;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dao.entities.EmployeeAttendanceEntity;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.AttendanceType;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dto.CreateEmployeeAttendanceRequestDTO;
import org.bhavani.constructions.services.EmployeeAttendanceService;

import javax.inject.Inject;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Objects;

import static org.bhavani.constructions.constants.ErrorConstants.TRANSACTION_ERROR;
import static org.bhavani.constructions.dao.entities.models.EmployeeType.DRIVER;
import static org.bhavani.constructions.utils.EntityBuilder.createEmployeeAttendanceEntity;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultEmployeeAttendanceService implements EmployeeAttendanceService {

    private final EmployeeAttendanceDao employeeAttendanceDao;
    private final DriverEntityDao driverEntityDao;

    @Override
    public EmployeeAttendanceEntity enterAttendance(CreateEmployeeAttendanceRequestDTO createEmployeeAttendanceRequestDTO, String userId) {
        EmployeeAttendanceEntity employeeAttendanceEntity = createEmployeeAttendanceEntity(createEmployeeAttendanceRequestDTO, userId);
        employeeAttendanceDao.enterAttendance(employeeAttendanceEntity);
        log.info("Attendance for: {} entered successfully by: {}", createEmployeeAttendanceRequestDTO.getAttendanceDate(), createEmployeeAttendanceRequestDTO.getEnteredBy());
        if(createEmployeeAttendanceRequestDTO.isMakeTransaction()){
            TransactionEntity transactionEntity;
            if(Objects.equals(createEmployeeAttendanceRequestDTO.getEmployeeType(), DRIVER)) {
                DriverEntity driverEntity = driverEntityDao.getDriver(createEmployeeAttendanceRequestDTO.getEmployeeName()).orElseThrow(() -> {
                    log.error("Error while making transaction based on attendance");
                    return new RuntimeException(TRANSACTION_ERROR);
                });
                int dailyWage = createEmployeeAttendanceRequestDTO.getAttendanceType() == AttendanceType.DAY ? driverEntity.getOtPayDay() : driverEntity.getOtPayDayNight();
                transactionEntity = getTransactionEntity(driverEntity.getName(), createEmployeeAttendanceRequestDTO.getEmployeeName(), dailyWage, createEmployeeAttendanceRequestDTO.getAttendanceDate());
            }
        }
        return employeeAttendanceEntity;
    }

    @Override
    public EnumSet<AttendanceType> getAttendanceTypes() {
        return EnumSet.allOf(AttendanceType.class);
    }

    private static TransactionEntity getTransactionEntity(String source, String destination, long dailyWage, LocalDate attendanceDate) {
        return TransactionEntity.builder()
                .source(source)
                .destination(destination)
                .amount(dailyWage)
                .purpose(TransactionPurpose.ATTENDANCE)
                .remarks("Making automated payment by attendance marking")
                .transactionDate(attendanceDate)
                .status(TransactionStatus.SUBMITTED)
                .mode(TransactionMode.CASH)
                .build();
    }
}
