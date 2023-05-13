package org.bhavani.constructions.utils;

import org.apache.commons.io.IOUtils;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dao.entities.VehicleEntity;
import org.bhavani.constructions.dao.entities.VehicleTaxEntity;
import org.bhavani.constructions.dto.CreateDriverRequestDTO;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;
import org.bhavani.constructions.dto.CreateVehicleRequestDTO;
import org.bhavani.constructions.dto.UploadVehicleTaxRequestDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static VehicleTaxEntity createVehicleTaxEntity(UploadVehicleTaxRequestDTO uploadVehicleTaxRequestDTO, String vehicleNumber)
            throws IOException {
        return VehicleTaxEntity.builder()
                .vehicleNumber(vehicleNumber)
                .taxType(uploadVehicleTaxRequestDTO.getTaxType())
                .amount(uploadVehicleTaxRequestDTO.getAmount())
                .taxReceipt(IOUtils.toByteArray(uploadVehicleTaxRequestDTO.getTaxReceipt()))
                .validityStartDate(uploadVehicleTaxRequestDTO.getValidityStartDate())
                .validityEndDate(uploadVehicleTaxRequestDTO.getValidityEndDate())
                .build();
    }

    public static List<VehicleTaxEntity> createVehicleTaxEntities(List<UploadVehicleTaxRequestDTO> vehicleTaxRequestDTOS, String vehicleNumber){
        if(vehicleTaxRequestDTOS == null || vehicleTaxRequestDTOS.isEmpty())
            return new ArrayList<>();
        return vehicleTaxRequestDTOS.stream().map(vehicleTaxRequestDTO -> {
                    try {
                        return createVehicleTaxEntity(vehicleTaxRequestDTO, vehicleNumber);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                                    .collect(Collectors.toList());
    }

    public static VehicleEntity createVehicle(CreateVehicleRequestDTO createVehicleRequestDTO){
        return VehicleEntity.builder()
                .vehicleNumber(createVehicleRequestDTO.getVehicleNumber())
                .owner(createVehicleRequestDTO.getOwner())
                .chassisNumber(createVehicleRequestDTO.getChassisNumber())
                .engineNumber(createVehicleRequestDTO.getEngineNumber())
                .vehicleClass(createVehicleRequestDTO.getVehicleClass())
                .insuranceProvider(createVehicleRequestDTO.getInsuranceProvider())
                .financeProvider(createVehicleRequestDTO.getFinanceProvider())
                .build();
    }
}

