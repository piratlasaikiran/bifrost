package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.entities.*;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;
import org.bhavani.constructions.services.SupervisorService;
import org.bhavani.constructions.utils.AWSS3Util;
import org.bhavani.constructions.utils.EntityBuilder;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.SUPERVISOR_AADHAR_FOLDER;
import static org.bhavani.constructions.constants.ErrorConstants.*;
import static org.bhavani.constructions.utils.EntityBuilder.convertListToCommaSeparatedString;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultSupervisorService implements SupervisorService {

    private final SupervisorEntityDao supervisorEntityDao;
    private final EmployeeAttendanceDao employeeAttendanceDao;
    private final AssetLocationEntityDao assetLocationEntityDao;
    private final AssetOwnershipEntityDao assetOwnershipEntityDao;
    private final PassBookEntityDao passBookEntityDao;
    private final TransactionEntityDao transactionEntityDao;
    private final SiteEntityDao siteEntityDao;


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
                                             InputStream aadhar, FormDataContentDisposition aadharContent,
                                             String userId) {
        supervisorEntityDao.getSupervisorByATMCard(createSupervisorRequestDTO.getAtmCardNumber())
                .ifPresent(supervisorHoldingATMCard -> {
                log.error("{} already in use. Use different card.", supervisorHoldingATMCard.getAtmCardNumber());
                throw new IllegalArgumentException(ATM_CARD_IN_USE);
        });
        String aadharLocationS3 = AWSS3Util.uploadToAWSS3(aadhar, aadharContent.getFileName(), SUPERVISOR_AADHAR_FOLDER);
        SupervisorEntity supervisorEntity = EntityBuilder.createSupervisorEntity(createSupervisorRequestDTO, aadharLocationS3, userId);
        supervisorEntityDao.getSupervisor(createSupervisorRequestDTO.getName()).ifPresent(existingSupervisor -> {
            log.error("{} already present. Use different name.", existingSupervisor.getName());
            throw new IllegalArgumentException(USER_EXISTS);
        });
        supervisorEntityDao.saveSupervisor(supervisorEntity);
        return supervisorEntity;
    }

    @Override
    public SupervisorEntity updateSupervisor(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                             InputStream aadhar, FormDataContentDisposition aadharContent,
                                             String userId, String supervisorName) {

        Optional<SupervisorEntity> supervisorHoldingATMCard = supervisorEntityDao.getSupervisorByATMCard(createSupervisorRequestDTO.getAtmCardNumber());
        if(supervisorHoldingATMCard.isPresent() && !Objects.equals(supervisorHoldingATMCard.get().getName(), supervisorName)){
                    log.error("{} already in use. Use different card.", supervisorHoldingATMCard.get().getAtmCardNumber());
                    throw new IllegalArgumentException(ATM_CARD_IN_USE);
        }
        SupervisorEntity supervisorEntity = supervisorEntityDao.getSupervisor(supervisorName).orElseThrow(() -> {
            log.error("{} doesn't exist. Create user first", supervisorName);
            return new IllegalArgumentException(USER_NOT_FOUND);
        });
        if(!supervisorName.equals(createSupervisorRequestDTO.getName())) {
            supervisorEntityDao.getSupervisor(createSupervisorRequestDTO.getName()).ifPresent(newSupervisorEntity -> {
                log.error("User: {} already exists", createSupervisorRequestDTO.getName());
                throw new IllegalArgumentException(USER_EXISTS);
            });
            updateDependentEntities(supervisorName, createSupervisorRequestDTO.getName());
        }
        String updatedAadharS3Location = AWSS3Util.updateDocInAWS(supervisorEntity.getAadhar(), aadhar, aadharContent.getFileName(), SUPERVISOR_AADHAR_FOLDER);
        updateSupervisorData(createSupervisorRequestDTO, updatedAadharS3Location, userId, supervisorEntity);
        supervisorEntityDao.updateSupervisor(supervisorEntity);
        return supervisorEntity;
    }

    private void updateDependentEntities(String oldSupervisorName, String newSupervisorName) {
        List<EmployeeAttendanceEntity> employeeAttendanceEntities = employeeAttendanceDao.getEmployeeAttendancesForEmployee(oldSupervisorName);
        employeeAttendanceEntities.forEach(employeeAttendanceEntity -> employeeAttendanceEntity.setEmployeeName(newSupervisorName));

        List<AssetLocationEntity> assetLocationEntities = assetLocationEntityDao.getAssetLocationEntities(oldSupervisorName);
        assetLocationEntities.forEach(assetLocationEntity -> assetLocationEntity.setAssetName(newSupervisorName));

        List<AssetOwnershipEntity> assetOwnershipEntities = assetOwnershipEntityDao.getAssetOwnershipEntitiesByOwnerName(oldSupervisorName);
        assetOwnershipEntities.forEach(assetOwnershipEntity -> assetOwnershipEntity.setCurrentOwner(newSupervisorName));

        List<TransactionEntity> transactionEntitiesAsSource = transactionEntityDao.getTransactionsBySourceName(oldSupervisorName);
        List<TransactionEntity> transactionEntitiesAsDestination = transactionEntityDao.getTransactionsByDestinationName(oldSupervisorName);
        transactionEntitiesAsSource.forEach(transactionEntity -> transactionEntity.setSource(newSupervisorName));
        transactionEntitiesAsDestination.forEach(transactionEntity -> transactionEntity.setDestination(newSupervisorName));

        List<PassBookEntity> passBookEntities = passBookEntityDao.getAccountPasBook(oldSupervisorName);
        passBookEntities.forEach(passBookEntity -> passBookEntity.setAccountName(newSupervisorName));

        List<SiteEntity> siteEntities = siteEntityDao.getSitesUnderSupervisor(oldSupervisorName);
        siteEntities.forEach(siteEntity -> {
            List<String> supervisors = siteEntity.getSupervisors();
            supervisors.replaceAll(s -> s.equals(oldSupervisorName) ? newSupervisorName : s);
            siteEntity.setSupervisors(convertListToCommaSeparatedString(supervisors));
        });
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
                .companyMobileNumber(supervisor.getCompanyMobileNumber())
                .atmCardNumber(supervisor.getAtmCardNumber())
                .build();
    }

    @Override
    public List<CreateSupervisorRequestDTO> getSupervisors() {
        List<SupervisorEntity> supervisorEntities = supervisorEntityDao.getAllSupervisors();
        List<CreateSupervisorRequestDTO> supervisorDTOs = new ArrayList<>();
        supervisorEntities.forEach(supervisor -> {
            supervisorDTOs.add(CreateSupervisorRequestDTO.builder()
                            .name(supervisor.getName())
                            .personalMobileNumber(supervisor.getPersonalMobileNumber())
                            .bankAccountNumber(supervisor.getBankAccountNumber())
                            .salary(supervisor.getSalary())
                            .companyMobileNumber(supervisor.getCompanyMobileNumber())
                            .atmCardNumber(supervisor.getAtmCardNumber())
                            .otPay(supervisor.getOtPay())
                            .build());
        });
        return supervisorDTOs;
    }

    private static void updateSupervisorData(CreateSupervisorRequestDTO createSupervisorRequestDTO, String aadharLocationS3,
                                             String userId, SupervisorEntity supervisorEntity) {
        supervisorEntity.setName(createSupervisorRequestDTO.getName());
        supervisorEntity.setPersonalMobileNumber(createSupervisorRequestDTO.getPersonalMobileNumber());
        supervisorEntity.setBankAccountNumber(createSupervisorRequestDTO.getBankAccountNumber());
        supervisorEntity.setSalary(createSupervisorRequestDTO.getSalary());
        supervisorEntity.setAadhar(aadharLocationS3);
        supervisorEntity.setCompanyMobileNumber(createSupervisorRequestDTO.getCompanyMobileNumber());
        supervisorEntity.setAtmCardNumber(createSupervisorRequestDTO.getAtmCardNumber());
        supervisorEntity.setOtPay(createSupervisorRequestDTO.getOtPay());
        supervisorEntity.setUpdatedBy(userId);
    }
}
