package org.bhavani.constructions.utils;

import org.apache.commons.io.IOUtils;
import org.bhavani.constructions.dao.entities.*;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dto.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.bhavani.constructions.constants.Constants.STRING_JOIN_DELIMITER;

public class EntityBuilder {
    public static SupervisorEntity createSupervisorEntity(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                                          String aadharLocationS3, String userId) {

        return SupervisorEntity.builder()
                .name(createSupervisorRequestDTO.getName())
                .personalMobileNumber(createSupervisorRequestDTO.getPersonalMobileNumber())
                .bankAccountNumber(createSupervisorRequestDTO.getBankAccountNumber())
                .salary(createSupervisorRequestDTO.getSalary())
                .aadhar(aadharLocationS3)
                .companyMobileNumber(createSupervisorRequestDTO.getCompanyMobileNumber())
                .atmCardNumber(createSupervisorRequestDTO.getAtmCardNumber())
                .otPay(createSupervisorRequestDTO.getOtPay())
                .createdBy(userId)
                .updatedBy(userId)
                .build();
    }

    public static DriverEntity createDriverEntity(CreateDriverRequestDTO createDriverRequestDTO, String licenseLocationS3,
                                                  String aadharLocationS3, String userId) {

        return DriverEntity.builder()
                .name(createDriverRequestDTO.getName())
                .personalMobileNumber(createDriverRequestDTO.getPersonalMobileNumber())
                .bankAccountNumber(createDriverRequestDTO.getBankAccountNumber())
                .salary(createDriverRequestDTO.getSalary())
                .otPayDay(createDriverRequestDTO.getOtPayDay())
                .otPayDayNight(createDriverRequestDTO.getOtPayDayNight())
                .license(licenseLocationS3)
                .aadhar(aadharLocationS3)
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

    public static SiteEntity createSiteEntity(CreateSiteRequestDTO createSiteRequestDTO){
        return SiteEntity.builder()
                .siteName(createSiteRequestDTO.getSiteName())
                .address(createSiteRequestDTO.getAddress())
                .currentStatus(createSiteRequestDTO.getSiteStatus())
                .supervisors(convertListToCommaSeparatedString(createSiteRequestDTO.getSupervisors()))
                .vehicles(convertListToCommaSeparatedString(createSiteRequestDTO.getVehicles()))
                .workStartDate(createSiteRequestDTO.getWorkStartDate())
                .workEndDate(createSiteRequestDTO.getWorkEndDate())
                .build();
    }

    public static String convertListToCommaSeparatedString(List<String> entities) {
        return String.join(STRING_JOIN_DELIMITER, entities);
    }

    public static AssetLocationEntity createAssetLocationEntity(CreateAssetLocationRequestDTO createAssetLocationRequestDTO){
        return AssetLocationEntity.builder()
                .assetName(createAssetLocationRequestDTO.getAssetName())
                .assetType(createAssetLocationRequestDTO.getAssetType())
                .location(createAssetLocationRequestDTO.getLocation())
                .startDate(createAssetLocationRequestDTO.getStartDate())
                .endDate(createAssetLocationRequestDTO.getEndDate())
                .build();
    }

    public static UserEntity createUserEntity(String userName, String hashedPassword, String userId){
        return UserEntity.builder()
                .userName(userName)
                .password(hashedPassword)
                .createdBy(userId)
                .updatedBy(userId)
                .build();
    }

    public static VendorEntity createVendorEntity(CreateVendorRequestDTO createVendorRequestDTO, String contractDocLocationS3, String userId){
        return VendorEntity.builder()
                .vendorId(createVendorRequestDTO.getVendorId())
                .name(createVendorRequestDTO.getName())
                .location(createVendorRequestDTO.getLocation())
                .mobileNumber(createVendorRequestDTO.getMobileNumber())
                .purposes(convertListToCommaSeparatedString(createVendorRequestDTO.getPurposes().stream().map(Enum::toString).collect(Collectors.toList())))
                .commodityCosts(createVendorRequestDTO.getCommodityCosts())
                .contractDocument(contractDocLocationS3)
                .createdBy(userId)
                .updatedBy(userId)
                .build();
    }

    public static BankAccountEntity createBankAccountEntity(CreateBankAccountRequestDTO createBankAccountRequestDTO, String userId){
        return BankAccountEntity.builder()
                .nickName(createBankAccountRequestDTO.getNickName())
                .accountNumber(createBankAccountRequestDTO.getAccountNumber())
                .bankName(createBankAccountRequestDTO.getBankName())
                .accountHolders(convertListToCommaSeparatedString(createBankAccountRequestDTO.getAccountHolders()))
                .atmCard(createBankAccountRequestDTO.getAtmCard())
                .currentBalance(createBankAccountRequestDTO.getCurrentBalance())
                .createdBy(userId)
                .updatedBy(userId)
                .build();
    }

    public static TransactionEntity createTransactionEntity(CreateTransactionRequestDTO createTransactionRequestDTO, String billLocationS3, String userId) {
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .source(createTransactionRequestDTO.getSource())
                .destination(createTransactionRequestDTO.getDestination())
                .amount(createTransactionRequestDTO.getAmount())
                .transactionDate(createTransactionRequestDTO.getTransactionDate())
                .purpose(createTransactionRequestDTO.getPurpose())
                .site(createTransactionRequestDTO.getSite())
                .vehicleNumber(createTransactionRequestDTO.getVehicleNumber())
                .status(createTransactionRequestDTO.getStatus())
                .mode(createTransactionRequestDTO.getMode())
                .status(TransactionStatus.SUBMITTED)
                .bankAccount(createTransactionRequestDTO.getBankAccount())
                .remarks(createTransactionRequestDTO.getRemarks())
                .createdBy(userId)
                .updatedBy(userId)
                .build();
        if(Objects.nonNull(billLocationS3))
            transactionEntity.setBill(billLocationS3);

        return transactionEntity;
    }

    public static VendorAttendanceEntity createVendorAttendanceEntity(CreateVendorAttendanceRequestDTO createVendorAttendanceRequestDTO, String userId){
        return VendorAttendanceEntity.builder()
                .vendorId(createVendorAttendanceRequestDTO.getVendorId())
                .site(createVendorAttendanceRequestDTO.getSite())
                .enteredBy(createVendorAttendanceRequestDTO.getEnteredBy())
                .attendanceDate(createVendorAttendanceRequestDTO.getAttendanceDate())
                .commodityAttendance(createVendorAttendanceRequestDTO.getCommodityAttendance())
                .makeTransaction(createVendorAttendanceRequestDTO.isMakeTransaction())
                .bankAccount(createVendorAttendanceRequestDTO.getBankAccount())
                .createdBy(userId)
                .updatedBy(userId)
                .build();

    }

    public static EmployeeAttendanceEntity createEmployeeAttendanceEntity(CreateEmployeeAttendanceRequestDTO createEmployeeAttendanceRequestDTO, String userId){
        return EmployeeAttendanceEntity.builder()
                .employeeName(createEmployeeAttendanceRequestDTO.getEmployeeName())
                .employeeType(createEmployeeAttendanceRequestDTO.getEmployeeType())
                .attendanceDate(createEmployeeAttendanceRequestDTO.getAttendanceDate())
                .site(createEmployeeAttendanceRequestDTO.getSite())
                .attendanceType(createEmployeeAttendanceRequestDTO.getAttendanceType())
                .enteredBy(createEmployeeAttendanceRequestDTO.getEnteredBy())
                .makeTransaction(createEmployeeAttendanceRequestDTO.isMakeTransaction())
                .bankAccount(createEmployeeAttendanceRequestDTO.getBankAccount())
                .createdBy(userId)
                .updatedBy(userId)
                .build();
    }

    public static TransactionEntity getTransactionEntityForAttendance(String destination, long amount, LocalDate attendanceDate, String userId) {
        return TransactionEntity.builder()
                .source("Bhavani Constructions")
                .destination(destination)
                .amount(amount)
                .purpose(TransactionPurpose.ATTENDANCE)
                .remarks("Making automated payment by attendance marking")
                .transactionDate(attendanceDate)
                .status(TransactionStatus.SUBMITTED)
                .mode(TransactionMode.CASH)
                .createdBy(userId)
                .updatedBy(userId)
                .build();
    }

    public static AssetOwnershipEntity createAssetOwnershipEntity(CreateAssetOwnershipRequestDTO createAssetOwnershipRequestDTO, String userId){
        return AssetOwnershipEntity.builder()
                .assetType(createAssetOwnershipRequestDTO.getAssetType())
                .assetName(createAssetOwnershipRequestDTO.getAssetName())
                .currentOwner(createAssetOwnershipRequestDTO.getCurrentOwner())
                .startDate(createAssetOwnershipRequestDTO.getStartDate())
                .endDate(createAssetOwnershipRequestDTO.getEndDate())
                .createdBy(userId)
                .updatedBy(userId)
                .build();
    }

    public static PendingBalanceEntity createPendingBalanceEntity(String accountName, TransactionEntity transactionEntity, Long pendingAmount, String userId){
        return PendingBalanceEntity.builder()
                .accountName(accountName)
                .transactionEntity(transactionEntity)
                .pendingBalance(pendingAmount)
                .createdBy(userId)
                .updatedBy(userId)
                .build();
    }
}

