package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bhavani.constructions.dao.api.SupervisorEntityDao;
import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;
import org.bhavani.constructions.utils.EntityBuilder;
import org.bhavani.constructions.services.SupervisorService;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.bhavani.constructions.constants.ErrorConstants.*;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultSupervisorService implements SupervisorService {

    private final SupervisorEntityDao supervisorEntityDao;

    @Override
    public SupervisorEntity getSupervisor(String supervisorName) {
        Optional<SupervisorEntity> supervisor = supervisorEntityDao.getSupervisor(supervisorName);
        if(!supervisor.isPresent()){
            log.info("No user with name: {}", supervisorName);
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        return supervisor.get();
    }

    @Override
    public SupervisorEntity createSupervisor(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                             InputStream aadhar, String userId) {
        try {
            SupervisorEntity supervisorEntity = EntityBuilder.createSupervisorEntity(createSupervisorRequestDTO, aadhar, userId);
            supervisorEntityDao.getSupervisor(createSupervisorRequestDTO.getName()).ifPresent(existingSupervisor -> {
                log.error("{} already present. User different name.", existingSupervisor.getName());
                throw new IllegalArgumentException(USER_EXISTS);
            });
            supervisorEntityDao.saveSupervisor(supervisorEntity);
            return supervisorEntity;
        }catch (IOException ioException){
            log.error("Error while parsing license/aadhar");
            throw new RuntimeException(DOC_PARSING_ERROR);
        }
    }

    @Override
    public SupervisorEntity updateSupervisor(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                             InputStream aadhar, String userId) {
        SupervisorEntity supervisorEntity = supervisorEntityDao.getSupervisor(createSupervisorRequestDTO.getName()).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", createSupervisorRequestDTO.getName());
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
        updateSupervisorData(createSupervisorRequestDTO, aadhar, supervisorEntity);
        supervisorEntityDao.updateSupervisor(supervisorEntity);
        return supervisorEntity;
    }

    @Override
    public void deleteSuperVisor(String supervisorName) {
        SupervisorEntity supervisorEntity = supervisorEntityDao.getSupervisor(supervisorName).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", supervisorName);
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
        supervisorEntityDao.deleteSupervisor(supervisorEntity);
    }

    @Override
    public SupervisorEntity createSupervisorResponse(SupervisorEntity supervisor) {
        return SupervisorEntity.builder()
                .name(supervisor.getName())
                .personalMobileNumber(supervisor.getPersonalMobileNumber())
                .bankAccountNumber(supervisor.getBankAccountNumber())
                .salary(supervisor.getSalary())
                .admin(supervisor.isAdmin())
                .companyMobileNumber(supervisor.getCompanyMobileNumber())
                .atmCardNumber(supervisor.getAtmCardNumber())
                .vehicleNumber(supervisor.getVehicleNumber())
                .build();
    }

    private static void updateSupervisorData(CreateSupervisorRequestDTO createSupervisorRequestDTO, InputStream aadhar, SupervisorEntity supervisorEntity) {
        try {
            supervisorEntity.setName(createSupervisorRequestDTO.getName());
            supervisorEntity.setPersonalMobileNumber(createSupervisorRequestDTO.getPersonalMobileNumber());
            supervisorEntity.setBankAccountNumber(createSupervisorRequestDTO.getBankAccountNumber());
            supervisorEntity.setSalary(createSupervisorRequestDTO.getSalary());
            supervisorEntity.setAdmin(createSupervisorRequestDTO.isAdmin());
            supervisorEntity.setAadhar(IOUtils.toByteArray(aadhar));
            supervisorEntity.setCompanyMobileNumber(createSupervisorRequestDTO.getCompanyMobileNumber());
            supervisorEntity.setAtmCardNumber(createSupervisorRequestDTO.getAtmCardNumber());
            supervisorEntity.setVehicleNumber(createSupervisorRequestDTO.getVehicleNumber());
            supervisorEntity.setOtPay(createSupervisorRequestDTO.getOtPay());
        }catch(IOException ioException){
            log.error("Error while updating driver data");
            throw new RuntimeException(CORRUPTED_DATA);
        }
    }
}
