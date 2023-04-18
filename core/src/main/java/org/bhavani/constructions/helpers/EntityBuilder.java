package org.bhavani.constructions.helpers;

import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;

public class EntityBuilder {
    public static SupervisorEntity createUserEntity(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                                    String userId) {

        return SupervisorEntity.builder()
                .name(createSupervisorRequestDTO.getName())
                .personalMobileNumber(createSupervisorRequestDTO.getPersonalMobileNumber())
                .bankAccountNumber(createSupervisorRequestDTO.getBankAccountNumber())
                .salary(createSupervisorRequestDTO.getSalary())
                .admin(createSupervisorRequestDTO.isAdmin())
                .companyMobileNumber(createSupervisorRequestDTO.getCompanyMobileNumber())
                .atmCardNumber(createSupervisorRequestDTO.getAtmCardNumber())
                .vehicleNumber(createSupervisorRequestDTO.getVehicleNumber())
                .otPay(createSupervisorRequestDTO.getOtPay())
                .createdBy(userId)
                .updatedBy(userId)
                .build();

    }
}

