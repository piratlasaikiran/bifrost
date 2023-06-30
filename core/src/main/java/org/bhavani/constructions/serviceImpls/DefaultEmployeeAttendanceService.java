package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.entities.*;
import org.bhavani.constructions.dao.entities.models.AttendanceType;
import org.bhavani.constructions.dao.entities.models.EmployeeType;
import org.bhavani.constructions.dto.CreateEmployeeAttendanceRequestDTO;
import org.bhavani.constructions.services.EmployeeAttendanceService;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static org.bhavani.constructions.constants.ErrorConstants.TRANSACTION_ERROR;
import static org.bhavani.constructions.dao.entities.models.EmployeeType.DRIVER;
import static org.bhavani.constructions.utils.EntityBuilder.createEmployeeAttendanceEntity;
import static org.bhavani.constructions.utils.EntityBuilder.getTransactionEntityForAttendance;
import static org.bhavani.constructions.utils.PassBookHelper.createPassBookEntities;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultEmployeeAttendanceService implements EmployeeAttendanceService {

    private final EmployeeAttendanceDao employeeAttendanceDao;
    private final DriverEntityDao driverEntityDao;
    private final TransactionEntityDao transactionEntityDao;
    private final PassBookEntityDao passBookEntityDao;
    private final SupervisorEntityDao supervisorEntityDao;
    private final VendorEntityDao vendorEntityDao;

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
                int dailyWage = createEmployeeAttendanceRequestDTO.getAttendanceType() == AttendanceType.DAY ? driverEntity.getOtPayDay() : driverEntity.getOtPayDayNight();
                TransactionEntity transactionEntity = getTransactionEntityForAttendance(createEmployeeAttendanceRequestDTO.getEnteredBy(), createEmployeeAttendanceRequestDTO.getEmployeeName(),
                        dailyWage, createEmployeeAttendanceRequestDTO.getAttendanceDate(), createEmployeeAttendanceRequestDTO.getBankAccount(), userId);
                saveTransactionAndPassBookEntries(transactionEntity);
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
                    .employeeName(employeeAttendanceEntity.getEmployeeAttendancePK().getEmployeeName())
                    .site(employeeAttendanceEntity.getEmployeeAttendancePK().getSite())
                    .attendanceDate(employeeAttendanceEntity.getEmployeeAttendancePK().getAttendanceDate())
                    .employeeType(employeeAttendanceEntity.getEmployeeAttendancePK().getEmployeeType())
                    .attendanceType(employeeAttendanceEntity.getAttendanceType())
                    .enteredBy(employeeAttendanceEntity.getEnteredBy())
                    .makeTransaction(employeeAttendanceEntity.isMakeTransaction())
                    .bankAccount(employeeAttendanceEntity.getBankAccount())
                    .build()
            );
        });
        return employeeAttendanceRequestDTOS;
    }

    private void saveTransactionAndPassBookEntries(TransactionEntity transactionEntity) {
        transactionEntityDao.saveTransaction(transactionEntity);
        Set<String> employees = getAllEmployeeNames();
        List<PassBookEntity> passBookEntities = createPassBookEntities(transactionEntity, employees, getPreviousPassBookBalance(transactionEntity.getSource()), getPreviousPassBookBalance(transactionEntity.getDestination()));
        passBookEntityDao.savePassBookEntities(passBookEntities);
    }

    private Long getPreviousPassBookBalance(String accountName){
        Optional<PassBookEntity> previousPassBookEntity = passBookEntityDao.getLatestPassBookEntity(accountName);
        return previousPassBookEntity.isPresent() ? previousPassBookEntity.get().getCurrentBalance() : 0L;
    }

    private Set<String> getAllEmployeeNames() {
        Set<String> employeeNames = new HashSet<>();
        employeeNames.addAll(driverEntityDao.getDrivers().stream().map(DriverEntity::getName).collect(Collectors.toSet()));
        employeeNames.addAll(supervisorEntityDao.getAllSupervisors().stream().map(SupervisorEntity::getName).collect(Collectors.toSet()));
        employeeNames.addAll(vendorEntityDao.getAllVendors().stream().map(VendorEntity::getVendorId).collect(Collectors.toSet()));
        return employeeNames;
    }
}
