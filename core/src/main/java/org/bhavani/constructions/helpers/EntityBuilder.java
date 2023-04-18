package org.bhavani.constructions.helpers;

import org.apache.commons.io.IOUtils;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dto.CreateDriverRequestDTO;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;

import java.io.IOException;
import java.io.InputStream;

public class EntityBuilder {
    public static SupervisorEntity createSupervisorEntity(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                                          InputStream aadhar, String userId) throws IOException {

        return SupervisorEntity.builder()
                .name(createSupervisorRequestDTO.getName())
                .personalMobileNumber(createSupervisorRequestDTO.getPersonalMobileNumber())
                .bankAccountNumber(createSupervisorRequestDTO.getBankAccountNumber())
                .salary(createSupervisorRequestDTO.getSalary())
                .admin(createSupervisorRequestDTO.isAdmin())
                .aadhar(IOUtils.toByteArray(aadhar))
                .companyMobileNumber(createSupervisorRequestDTO.getCompanyMobileNumber())
                .atmCardNumber(createSupervisorRequestDTO.getAtmCardNumber())
                .vehicleNumber(createSupervisorRequestDTO.getVehicleNumber())
                .otPay(createSupervisorRequestDTO.getOtPay())
                .createdBy(userId)
                .updatedBy(userId)
                .build();

    }

    public static DriverEntity createDriverEntity(CreateDriverRequestDTO createDriverRequestDTO, InputStream license,
                                                  InputStream aadhar, String userId) throws IOException {

        return DriverEntity.builder()
                .name(createDriverRequestDTO.getName())
                .personalMobileNumber(createDriverRequestDTO.getPersonalMobileNumber())
                .bankAccountNumber(createDriverRequestDTO.getBankAccountNumber())
                .salary(createDriverRequestDTO.getSalary())
                .admin(createDriverRequestDTO.isAdmin())
                .otPayDay(createDriverRequestDTO.getOtPayDay())
                .otPayDayNight(createDriverRequestDTO.getOtPayDayNight())
                .license(IOUtils.toByteArray(license))
                .aadhar(IOUtils.toByteArray(aadhar))
                .createdBy(userId)
                .updatedBy(userId)
                .build();
    }
}

