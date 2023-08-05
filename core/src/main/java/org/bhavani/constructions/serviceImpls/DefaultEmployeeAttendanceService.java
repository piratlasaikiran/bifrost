package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dao.entities.EmployeeAttendanceEntity;
import org.bhavani.constructions.dao.entities.PendingBalanceEntity;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.AttendanceType;
import org.bhavani.constructions.dao.entities.models.EmployeeType;
import org.bhavani.constructions.dto.CreateEmployeeAttendanceRequestDTO;
import org.bhavani.constructions.services.EmployeeAttendanceService;

import javax.inject.Inject;
import java.util.*;

import static org.bhavani.constructions.constants.ErrorConstants.ATTENDANCE_ENTRY_NOT_FOUND;
import static org.bhavani.constructions.constants.ErrorConstants.TRANSACTION_ERROR;
import static org.bhavani.constructions.dao.entities.models.EmployeeType.DRIVER;
import static org.bhavani.constructions.utils.EntityBuilder.*;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultEmployeeAttendanceService implements EmployeeAttendanceService {

    private final EmployeeAttendanceDao employeeAttendanceDao;
    private final DriverEntityDao driverEntityDao;
    private final TransactionEntityDao transactionEntityDao;
    private final PendingBalanceEntityDao pendingBalanceEntityDao;

    @Override
    public EmployeeAttendanceEntity enterAttendance(CreateEmployeeAttendanceRequestDTO createEmployeeAttendanceRequestDTO, String userId) {
        EmployeeAttendanceEntity employeeAttendanceEntity = createEmployeeAttendanceEntity(createEmployeeAttendanceRequestDTO, userId);
        employeeAttendanceDao.enterAttendance(employeeAttendanceEntity);
        log.info("Attendance for: {} entered successfully by: {}", createEmployeeAttendanceRequestDTO.getAttendanceDate(), createEmployeeAttendanceRequestDTO.getEnteredBy());
        if(createEmployeeAttendanceRequestDTO.isMakeTransaction()){
            if(Objects.equals(createEmployeeAttendanceRequestDTO.getEmployeeType(), DRIVER)) {
                DriverEntity driverEntity = driverEntityDao.getDriver(createEmployeeAttendanceRequestDTO.getEmployeeName()).orElseThrow(() -> {
                    log.error("Error while making transaction based on attendance");
                    return new RuntimeException(TRANSACTION_ERROR);
                });
                long dailyWage = createEmployeeAttendanceRequestDTO.getAttendanceType() == AttendanceType.DAY ? driverEntity.getOtPayDay() : driverEntity.getOtPayDayNight();
                TransactionEntity transactionEntity = getTransactionEntityForAttendance(createEmployeeAttendanceRequestDTO.getEmployeeName(),
                        dailyWage, createEmployeeAttendanceRequestDTO.getAttendanceDate(), userId);
                checkAndUpdatePendingBalanceEntity(transactionEntity, userId, dailyWage);
                transactionEntityDao.saveTransaction(transactionEntity);
            }
        }
        return employeeAttendanceEntity;
    }

    @Override
    public EnumSet<AttendanceType> getAttendanceTypes() {
        return EnumSet.allOf(AttendanceType.class);
    }

    @Override
    public EnumSet<EmployeeType> getEmployeeTypes() {
        return EnumSet.allOf(EmployeeType.class);
    }

    @Override
    public List<CreateEmployeeAttendanceRequestDTO> getAllEmployeeAttendances() {
        List<EmployeeAttendanceEntity> employeeAttendanceEntities = employeeAttendanceDao.getAllEmployeeAttendance();
        List<CreateEmployeeAttendanceRequestDTO> employeeAttendanceRequestDTOS = new ArrayList<>();
        employeeAttendanceEntities.forEach(employeeAttendanceEntity -> {
            employeeAttendanceRequestDTOS.add(CreateEmployeeAttendanceRequestDTO.builder()
                    .id(employeeAttendanceEntity.getId())
                    .employeeName(employeeAttendanceEntity.getEmployeeName())
                    .site(employeeAttendanceEntity.getSite())
                    .attendanceDate(employeeAttendanceEntity.getAttendanceDate())
                    .employeeType(employeeAttendanceEntity.getEmployeeType())
                    .attendanceType(employeeAttendanceEntity.getAttendanceType())
                    .enteredBy(employeeAttendanceEntity.getEnteredBy())
                    .makeTransaction(employeeAttendanceEntity.isMakeTransaction())
                    .bankAccount(employeeAttendanceEntity.getBankAccount())
                    .build()
            );
        });
        return employeeAttendanceRequestDTOS;
    }

    @Override
    public EmployeeAttendanceEntity updateAttendance(CreateEmployeeAttendanceRequestDTO createEmployeeAttendanceRequestDTO, String userId, Long existingEmployeeAttendanceId) {
        EmployeeAttendanceEntity employeeAttendanceEntity = employeeAttendanceDao.getEmployeeAttendance(existingEmployeeAttendanceId).orElseThrow(() -> {
            log.error("Error while updating employee attendance");
            return new RuntimeException(ATTENDANCE_ENTRY_NOT_FOUND);
        });
        employeeAttendanceEntity.setEmployeeType(createEmployeeAttendanceRequestDTO.getEmployeeType());
        employeeAttendanceEntity.setEmployeeName(createEmployeeAttendanceRequestDTO.getEmployeeName());
        employeeAttendanceEntity.setAttendanceDate(createEmployeeAttendanceRequestDTO.getAttendanceDate());
        employeeAttendanceEntity.setSite(createEmployeeAttendanceRequestDTO.getSite());
        employeeAttendanceEntity.setAttendanceType(createEmployeeAttendanceRequestDTO.getAttendanceType());
        employeeAttendanceEntity.setEnteredBy(createEmployeeAttendanceRequestDTO.getEnteredBy());
        employeeAttendanceEntity.setMakeTransaction(createEmployeeAttendanceRequestDTO.isMakeTransaction());
        employeeAttendanceEntity.setBankAccount(createEmployeeAttendanceRequestDTO.getBankAccount());

        //ToDo: make changes in transaction entity and respective balances

        return employeeAttendanceEntity;
    }

    private void checkAndUpdatePendingBalanceEntity(TransactionEntity transactionEntity, String userId, long wage) {
        Optional<PendingBalanceEntity> pendingBalanceEntity = pendingBalanceEntityDao.getLatestPendingBalanceEntity(transactionEntity.getDestination());
        Long pendingAmount = wage;
        if(pendingBalanceEntity.isPresent()){
            log.info("Fetching previous pending balance for user: {}", transactionEntity.getDestination());
            pendingAmount += pendingBalanceEntity.get().getPendingBalance();

        }
        PendingBalanceEntity latestPendingBalanceEntity = createPendingBalanceEntity(transactionEntity.getDestination(), transactionEntity, pendingAmount, userId);
        pendingBalanceEntityDao.savePendingBalanceEntity(latestPendingBalanceEntity);
    }
}
