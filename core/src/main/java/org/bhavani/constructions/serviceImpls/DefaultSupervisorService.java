package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.SupervisorEntityDao;
import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;
import org.bhavani.constructions.services.SupervisorService;

import javax.inject.Inject;
import java.util.Optional;

import static org.bhavani.constructions.constants.ErrorConstants.USER_EXISTS;
import static org.bhavani.constructions.constants.ErrorConstants.USER_NOT_FOUND;
import static org.bhavani.constructions.helpers.EntityBuilder.createEmployeeEntity;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultSupervisorService implements SupervisorService {

    private final SupervisorEntityDao supervisorEntityDao;

    @Override
    public SupervisorEntity getEmployee(String employeeName) {
        Optional<SupervisorEntity> supervisor = supervisorEntityDao.getEmployee(employeeName);
        if(!supervisor.isPresent()){
            log.info("No user with name: {}", employeeName);
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        return supervisor.get();
    }

    @Override
    public SupervisorEntity createEmployee(CreateSupervisorRequestDTO createSupervisorRequestDTO, String userId) {
        SupervisorEntity supervisorEntity = createEmployeeEntity(createSupervisorRequestDTO, userId);
        supervisorEntityDao.getEmployee(createSupervisorRequestDTO.getName()).ifPresent(existingSupervisor -> {
            log.error("{} already present. User different name.", existingSupervisor.getName());
            throw new IllegalArgumentException(USER_EXISTS);
        });
        supervisorEntityDao.saveEmployee(supervisorEntity);
        return supervisorEntity;
    }

    @Override
    public SupervisorEntity updateEmployee(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                    String userId) {
        SupervisorEntity supervisorEntity = supervisorEntityDao.getEmployee(createSupervisorRequestDTO.getName()).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", createSupervisorRequestDTO.getName());
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
        updateSupervisorData(createSupervisorRequestDTO, supervisorEntity);
        supervisorEntityDao.updateEmployee(supervisorEntity);
        return supervisorEntity;
    }

    @Override
    public void deleteEmployee(String employeeName) {
        SupervisorEntity supervisorEntity = supervisorEntityDao.getEmployee(employeeName).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", employeeName);
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
        supervisorEntityDao.deleteEmployee(supervisorEntity);
    }

    private static void updateSupervisorData(CreateSupervisorRequestDTO createSupervisorRequestDTO, SupervisorEntity supervisorEntity) {
        supervisorEntity.setName(createSupervisorRequestDTO.getName());
        supervisorEntity.setPersonalMobileNumber(createSupervisorRequestDTO.getPersonalMobileNumber());
        supervisorEntity.setBankAccountNumber(createSupervisorRequestDTO.getBankAccountNumber());
        supervisorEntity.setSalary(createSupervisorRequestDTO.getSalary());
        supervisorEntity.setAdmin(createSupervisorRequestDTO.isAdmin());
        supervisorEntity.setCompanyMobileNumber(createSupervisorRequestDTO.getCompanyMobileNumber());
        supervisorEntity.setAtmCardNumber(createSupervisorRequestDTO.getAtmCardNumber());
        supervisorEntity.setVehicleNumber(createSupervisorRequestDTO.getVehicleNumber());
        supervisorEntity.setOtPay(createSupervisorRequestDTO.getOtPay());
    }
}
