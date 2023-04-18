package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.DriverEntityDao;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dto.CreateDriverRequestDTO;
import org.bhavani.constructions.services.DriverService;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

import static org.bhavani.constructions.constants.ErrorConstants.DOC_PARSING_ERROR;
import static org.bhavani.constructions.constants.ErrorConstants.USER_EXISTS;
import static org.bhavani.constructions.helpers.EntityBuilder.createEmployeeEntity;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultDriverService implements DriverService {

    private final DriverEntityDao driverEntityDao;
    @Override
    public DriverEntity getEmployee(String employeeName) {
        return null;
    }

    @Override
    public DriverEntity createEmployee(CreateDriverRequestDTO createDriverRequestDTO,
                                       @FormDataParam("file") InputStream license, @FormDataParam("file") InputStream aadhar,
                                       String userId) {
        try{
            DriverEntity driverEntity = createEmployeeEntity(createDriverRequestDTO, license, aadhar, userId);
            driverEntityDao.getEmployee(createDriverRequestDTO.getName()).ifPresent(existingDriver -> {
                log.error("{} already present. User different name.", existingDriver.getName());
                throw new IllegalArgumentException(USER_EXISTS);
            });
            driverEntityDao.saveEmployee(driverEntity);
            return driverEntity;
        }catch (IOException ioException){
            log.error("Error while parsing license/aadhar");
            throw new RuntimeException(DOC_PARSING_ERROR);
        }
    }

    @Override
    public DriverEntity updateEmployee(CreateDriverRequestDTO createDriverRequestDTO, String userId) {
        return null;
    }

    @Override
    public void deleteEmployee(String employeeName) {

    }

    @Override
    public DriverEntity createDriverResponse(DriverEntity driver) {
        return DriverEntity.builder()
                .name(driver.getName())
                .personalMobileNumber(driver.getPersonalMobileNumber())
                .bankAccountNumber(driver.getBankAccountNumber())
                .salary(driver.getSalary())
                .admin(driver.isAdmin())
                .otPayDay(driver.getOtPayDay())
                .otPayDayNight(driver.getOtPayDayNight())
                .build();
    }
}
