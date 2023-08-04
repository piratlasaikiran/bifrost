package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.entities.*;
import org.bhavani.constructions.dto.CreateDriverRequestDTO;
import org.bhavani.constructions.services.DriverService;
import org.bhavani.constructions.utils.AWSS3Util;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.bhavani.constructions.constants.Constants.DRIVER_AADHAR_FOLDER;
import static org.bhavani.constructions.constants.Constants.DRIVER_LICENSE_FOLDER;
import static org.bhavani.constructions.constants.ErrorConstants.USER_EXISTS;
import static org.bhavani.constructions.constants.ErrorConstants.USER_NOT_FOUND;
import static org.bhavani.constructions.utils.EntityBuilder.createDriverEntity;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultDriverService implements DriverService {

    private final DriverEntityDao driverEntityDao;
    private final EmployeeAttendanceDao employeeAttendanceDao;
    private final AssetLocationEntityDao assetLocationEntityDao;
    private final AssetOwnershipEntityDao assetOwnershipEntityDao;
    private final PassBookEntityDao passBookEntityDao;
    private final TransactionEntityDao transactionEntityDao;
    @Override
    public DriverEntity getDriver(String driverName) {
        return driverEntityDao.getDriver(driverName).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", driverName);
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
    }

    @Override
    public DriverEntity createDriver(CreateDriverRequestDTO createDriverRequestDTO,
                                     InputStream license, FormDataContentDisposition licenseContent,
                                     InputStream aadhar, FormDataContentDisposition aadharContent,
                                     String userId) {
        String aadharLocationS3 = AWSS3Util.uploadToAWSS3(aadhar, aadharContent.getFileName(), DRIVER_AADHAR_FOLDER);
        String licenseLocationS3 = AWSS3Util.uploadToAWSS3(license, licenseContent.getFileName(), DRIVER_LICENSE_FOLDER);

        DriverEntity driverEntity = createDriverEntity(createDriverRequestDTO, licenseLocationS3, aadharLocationS3, userId);
        driverEntityDao.getDriver(createDriverRequestDTO.getName()).ifPresent(existingDriver -> {
            log.error("{} already present. User different name.", existingDriver.getName());
            throw new IllegalArgumentException(USER_EXISTS);
        });
        driverEntityDao.saveDriver(driverEntity);
        return driverEntity;
    }

    @Override
    public void deleteDriver(String driverName) {
        DriverEntity driverEntity = driverEntityDao.getDriver(driverName).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", driverName);
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
        driverEntityDao.deleteDriver(driverEntity);
    }

    @Override
    public DriverEntity createDriverResponse(DriverEntity driverEntity) {
        return DriverEntity.builder()
                .name(driverEntity.getName())
                .personalMobileNumber(driverEntity.getPersonalMobileNumber())
                .bankAccountNumber(driverEntity.getBankAccountNumber())
                .salary(driverEntity.getSalary())
                .otPayDay(driverEntity.getOtPayDay())
                .otPayDayNight(driverEntity.getOtPayDayNight())
                .build();
    }

    @Override
    public DriverEntity updateDriver(CreateDriverRequestDTO createDriverRequestDTO, InputStream license, FormDataContentDisposition licenseContent,
                                     InputStream aadhar, FormDataContentDisposition aadharContent,
                                     String userId, String driverName) {
        DriverEntity driverEntity = driverEntityDao.getDriver(driverName).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", createDriverRequestDTO.getName());
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
        if(!driverName.equals(createDriverRequestDTO.getName())) {
            driverEntityDao.getDriver(createDriverRequestDTO.getName()).ifPresent(newDriverEntity -> {
                log.error("User: {} already exists", createDriverRequestDTO.getName());
                throw new IllegalArgumentException(USER_EXISTS);
            });
            updateDependentEntities(driverName, createDriverRequestDTO.getName());
        }
        String aadharLocationS3 = AWSS3Util.uploadToAWSS3(aadhar, aadharContent.getFileName(), DRIVER_AADHAR_FOLDER);
        String licenseLocationS3 = AWSS3Util.uploadToAWSS3(license, licenseContent.getFileName(), DRIVER_LICENSE_FOLDER);

        updateDriverData(createDriverRequestDTO, licenseLocationS3, aadharLocationS3, driverEntity, userId);
        return driverEntity;
    }

    private void updateDependentEntities(String oldDriverName, String newDriverName) {
        List<EmployeeAttendanceEntity> employeeAttendanceEntities = employeeAttendanceDao.getEmployeeAttendancesForEmployee(oldDriverName);
        employeeAttendanceEntities.forEach(employeeAttendanceEntity -> employeeAttendanceEntity.setEmployeeName(newDriverName));

        List<AssetLocationEntity> assetLocationEntities = assetLocationEntityDao.getAssetLocationEntities(oldDriverName);
        assetLocationEntities.forEach(assetLocationEntity -> assetLocationEntity.setAssetName(newDriverName));

        List<AssetOwnershipEntity> assetOwnershipEntities = assetOwnershipEntityDao.getAssetOwnershipEntitiesByOwnerName(oldDriverName);
        assetOwnershipEntities.forEach(assetOwnershipEntity -> assetOwnershipEntity.setCurrentOwner(newDriverName));

        List<TransactionEntity> transactionEntitiesAsSource = transactionEntityDao.getTransactionsBySourceName(oldDriverName);
        List<TransactionEntity> transactionEntitiesAsDestination = transactionEntityDao.getTransactionsByDestinationName(oldDriverName);
        transactionEntitiesAsSource.forEach(transactionEntity -> transactionEntity.setSource(newDriverName));
        transactionEntitiesAsDestination.forEach(transactionEntity -> transactionEntity.setDestination(newDriverName));

        List<PassBookEntity> passBookEntities = passBookEntityDao.getAccountPasBook(oldDriverName);
        passBookEntities.forEach(passBookEntity -> passBookEntity.setAccountName(newDriverName));
    }

    @Override
    public List<CreateDriverRequestDTO> getDrivers() {
        List<DriverEntity> driverEntities = getDriverEntities();
        List<CreateDriverRequestDTO> driverRequestDTOS = new ArrayList<>();
        driverEntities.forEach(driverEntity -> {
            driverRequestDTOS.add(CreateDriverRequestDTO.builder()
                            .name(driverEntity.getName())
                            .personalMobileNumber(driverEntity.getPersonalMobileNumber())
                            .bankAccountNumber(driverEntity.getBankAccountNumber())
                            .salary(driverEntity.getSalary())
                            .otPayDay(driverEntity.getOtPayDay())
                            .otPayDayNight(driverEntity.getOtPayDayNight())
                            .build());
        });
        return driverRequestDTOS;
    }

    @Override
    public List<String> getDriverNames() {
        List<DriverEntity> driverEntities = getDriverEntities();
        return driverEntities.stream().map(DriverEntity::getName).collect(Collectors.toList());
    }

    private List<DriverEntity> getDriverEntities() {
        return driverEntityDao.getDrivers();
    }

    private void updateDriverData(CreateDriverRequestDTO createDriverRequestDTO, String licenseLocationS3,
                                  String aadharLocationS3, DriverEntity driverEntity, String userId) {

        driverEntity.setName(createDriverRequestDTO.getName());
        driverEntity.setPersonalMobileNumber(createDriverRequestDTO.getPersonalMobileNumber());
        driverEntity.setBankAccountNumber(createDriverRequestDTO.getBankAccountNumber());
        driverEntity.setSalary(createDriverRequestDTO.getSalary());
        driverEntity.setLicense(licenseLocationS3);
        driverEntity.setAadhar(aadharLocationS3);
        driverEntity.setOtPayDay(createDriverRequestDTO.getOtPayDay());
        driverEntity.setOtPayDayNight(createDriverRequestDTO.getOtPayDayNight());
        driverEntity.setUpdatedBy(userId);
    }
}
