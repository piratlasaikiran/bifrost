package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bhavani.constructions.dao.api.DriverEntityDao;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dto.CreateDriverRequestDTO;
import org.bhavani.constructions.services.DriverService;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

import static org.bhavani.constructions.constants.ErrorConstants.*;
import static org.bhavani.constructions.utils.EntityBuilder.createDriverEntity;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultDriverService implements DriverService {

    private final DriverEntityDao driverEntityDao;
    @Override
    public DriverEntity getDriver(String driverName) {
        return driverEntityDao.getDriver(driverName).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", driverName);
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
    }

    @Override
    public DriverEntity createDriver(CreateDriverRequestDTO createDriverRequestDTO,
                                     InputStream license, InputStream aadhar,
                                     String userId) {
        try{
            DriverEntity driverEntity = createDriverEntity(createDriverRequestDTO, license, aadhar, userId);
            driverEntityDao.getDriver(createDriverRequestDTO.getName()).ifPresent(existingDriver -> {
                log.error("{} already present. User different name.", existingDriver.getName());
                throw new IllegalArgumentException(USER_EXISTS);
            });
            driverEntityDao.saveDriver(driverEntity);
            return driverEntity;
        }catch (IOException ioException){
            log.error("Error while parsing license/aadhar");
            throw new RuntimeException(DOC_PARSING_ERROR);
        }
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
                .admin(driverEntity.isAdmin())
                .otPayDay(driverEntity.getOtPayDay())
                .otPayDayNight(driverEntity.getOtPayDayNight())
                .build();
    }

    @Override
    public DriverEntity updateDriver(CreateDriverRequestDTO createDriverRequestDTO, InputStream license, InputStream aadhar, String userId) {
        DriverEntity driverEntity = driverEntityDao.getDriver(createDriverRequestDTO.getName()).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", createDriverRequestDTO.getName());
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
        updateDriverData(createDriverRequestDTO, license, aadhar, driverEntity);
        return driverEntity;
    }

    private void updateDriverData(CreateDriverRequestDTO createDriverRequestDTO, InputStream license, InputStream aadhar, DriverEntity driverEntity) {
        try {

            driverEntity.setName(createDriverRequestDTO.getName());
            driverEntity.setPersonalMobileNumber(createDriverRequestDTO.getPersonalMobileNumber());
            driverEntity.setBankAccountNumber(createDriverRequestDTO.getBankAccountNumber());
            driverEntity.setSalary(createDriverRequestDTO.getSalary());
            driverEntity.setAdmin(createDriverRequestDTO.isAdmin());
            driverEntity.setLicense(IOUtils.toByteArray(license));
            driverEntity.setAadhar(IOUtils.toByteArray(aadhar));
            driverEntity.setOtPayDay(createDriverRequestDTO.getOtPayDay());
            driverEntity.setOtPayDayNight(createDriverRequestDTO.getOtPayDayNight());
        }catch (IOException ioException){
            log.error("Error while updating driver data");
            throw new RuntimeException(CORRUPTED_DATA);
        }
    }
}
