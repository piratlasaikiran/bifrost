package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.entities.*;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dto.*;
import org.bhavani.constructions.services.TransactionService;
import org.bhavani.constructions.utils.AWSS3Util;
import org.bhavani.constructions.utils.EntityBuilder;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.inject.Inject;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.bhavani.constructions.constants.Constants.TRANSACTION_RECEIPT_FOLDER;
import static org.bhavani.constructions.constants.Constants.TRANSACTION_STATE_CHANGE_ALLOWANCE;
import static org.bhavani.constructions.constants.ErrorConstants.NO_PENDING_BALANCE;
import static org.bhavani.constructions.constants.ErrorConstants.TRANSACTION_NOT_FOUND;
import static org.bhavani.constructions.dao.entities.models.TransactionPurpose.SETTLEMENT;
import static org.bhavani.constructions.utils.EntityBuilder.createPendingBalanceEntity;
import static org.bhavani.constructions.utils.PassBookHelper.createPassBookEntities;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultTransactionService implements TransactionService {

    private final TransactionEntityDao transactionEntityDao;
    private final PassBookEntityDao passBookEntityDao;
    private final DriverEntityDao driverEntityDao;
    private final SupervisorEntityDao supervisorEntityDao;
    private final VendorEntityDao vendorEntityDao;
    private final PendingBalanceEntityDao pendingBalanceEntityDao;

    @Override
    public TransactionEntity createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO,
                                               InputStream bill, FormDataContentDisposition billContent,
                                               String userId) {
        String billLocationS3 = null;
        if(Objects.nonNull(bill)) {
            billLocationS3 = AWSS3Util.uploadToAWSS3(bill, billContent.getFileName(), TRANSACTION_RECEIPT_FOLDER);
        }
        TransactionEntity transactionEntity = EntityBuilder.createTransactionEntity(createTransactionRequestDTO, billLocationS3, userId);
        transactionEntityDao.saveTransaction(transactionEntity);
        checkAndUpdatePendingBalances(transactionEntity, userId);
        return transactionEntity;
    }

    @Override
    public EnumSet<TransactionMode> getTransactionModes() {
        return EnumSet.allOf(TransactionMode.class);
    }

    @Override
    public EnumSet<TransactionPurpose> getTransactionPurposes() {
        return EnumSet.allOf(TransactionPurpose.class);
    }

    @Override
    public List<CreateTransactionRequestDTO> getAllTransactions() {
        List<TransactionEntity> transactionEntities = transactionEntityDao.getTransactions();
        List<CreateTransactionRequestDTO> transactionRequestDTOS = new ArrayList<>();
        transactionEntities.forEach(transactionEntity -> transactionRequestDTOS.add(getTransactionRequestDTO(transactionEntity)));
        return transactionRequestDTOS;
    }

    @Override
    public List<PassBookResponseDTO> getAllPassBookMainPages() {
        List<PassBookEntity> passBookEntities = passBookEntityDao.getLatestPassBookForAll();
        List<PassBookResponseDTO> passBookResponseDTOS = new ArrayList<>();
        passBookEntities.forEach(passBookEntity -> {
            passBookResponseDTOS.add(PassBookResponseDTO.builder()
                            .accountName(passBookEntity.getAccountName())
                            .accountType(passBookEntity.getAccountType())
                            .transactionDetails(getTransactionRequestDTO(passBookEntity.getTransactionEntity()))
                            .currentBalance(passBookEntity.getCurrentBalance())
                            .transactionAmount(passBookEntity.getTransactionAmount())
                            .transactionType(passBookEntity.getTransactionType())
                            .build());
        });
        return passBookResponseDTOS;
    }

    @Override
    public List<PassBookResponseDTO> getAccountPassBook(String accountName) {
        List<PassBookEntity> passBookEntities = passBookEntityDao.getAccountPasBook(accountName);
        List<PassBookResponseDTO> passBookResponseDTOS = new ArrayList<>();
        passBookEntities.forEach(passBookEntity -> {
            passBookResponseDTOS.add(PassBookResponseDTO.builder()
                    .accountName(passBookEntity.getAccountName())
                    .accountType(passBookEntity.getAccountType())
                    .transactionDetails(getTransactionRequestDTO(passBookEntity.getTransactionEntity()))
                    .currentBalance(passBookEntity.getCurrentBalance())
                    .transactionAmount(passBookEntity.getTransactionAmount())
                    .transactionType(passBookEntity.getTransactionType())
                    .build());
        });
        return passBookResponseDTOS;
    }

    @Override
    public EnumSet<TransactionStatus> getTransactionStatuses() {
        return EnumSet.allOf(TransactionStatus.class);
    }

    @Override
    public TransactionEntity updateTransaction(CreateTransactionRequestDTO createTransactionRequestDTO,
                                               InputStream bill, FormDataContentDisposition billContent,
                                               String userId, Long transactionId) {
        TransactionEntity transactionEntity = transactionEntityDao.getTransaction(transactionId).orElseThrow(() -> {
            log.error("Transaction not found");
             return new RuntimeException(TRANSACTION_NOT_FOUND);
        });
        transactionEntity.setSource(createTransactionRequestDTO.getSource());
        transactionEntity.setDestination(createTransactionRequestDTO.getDestination());
        transactionEntity.setAmount(createTransactionRequestDTO.getAmount());
        transactionEntity.setTransactionDate(createTransactionRequestDTO.getTransactionDate());
        transactionEntity.setPurpose(createTransactionRequestDTO.getPurpose());
        transactionEntity.setSite(createTransactionRequestDTO.getSite());
        transactionEntity.setVehicleNumber(createTransactionRequestDTO.getVehicleNumber());
        transactionEntity.setMode(createTransactionRequestDTO.getMode());
        transactionEntity.setBankAccount(createTransactionRequestDTO.getBankAccount());
        transactionEntity.setStatus(createTransactionRequestDTO.getStatus());
        transactionEntity.setRemarks(createTransactionRequestDTO.getRemarks());
        if(Objects.nonNull(bill)) {
            String billLocationS3;
            if(Objects.nonNull(transactionEntity.getBill())) {
                billLocationS3 = AWSS3Util.updateDocInAWS(transactionEntity.getBill(), bill, billContent.getFileName(), TRANSACTION_RECEIPT_FOLDER);
            }
            else{
                billLocationS3 = AWSS3Util.uploadToAWSS3(bill, billContent.getFileName(), TRANSACTION_RECEIPT_FOLDER);
            }
            transactionEntity.setBill(billLocationS3);
        }
        return transactionEntity;
    }

    @Override
    public TransactionEntity getTransaction(Long transactionId) {
        return transactionEntityDao.getTransaction(transactionId).orElseThrow(() -> {
            log.error("Transaction not found");
            return new RuntimeException(TRANSACTION_NOT_FOUND);
        });
    }

    @Override
    public Map<TransactionStatus, List<TransactionStatus>> getTransactionStatusChangeMapping() {
        return TRANSACTION_STATE_CHANGE_ALLOWANCE;
    }

    @Override
    public void changeTransactionStatus(TransactionStatusChangeDTO transactionStatusChangeDTO, String userId) {
        TransactionEntity transactionEntity = transactionEntityDao.getTransaction(transactionStatusChangeDTO.getTransactionId()).orElseThrow(() -> {
            log.error("Transaction not found");
            return new RuntimeException(TRANSACTION_NOT_FOUND);
        });
        transactionEntity.setStatus(transactionStatusChangeDTO.getDesiredStatus());
        if((transactionEntity.getStatus().equals(TransactionStatus.SUBMITTED) || transactionEntity.getStatus().equals(TransactionStatus.ON_HOLD)) &&
          transactionStatusChangeDTO.getDesiredStatus().equals(TransactionStatus.CHECKED)){
            savePassBookEntries(transactionEntity);
            checkAndUpdatePendingBalances(transactionEntity, userId);
        }
    }

    @Override
    public void settlePendingBalance(String accountName, SettlePendingBalanceRequestDTO settlePendingBalanceRequestDTO, String userId) {
        PendingBalanceEntity latestPendingBalanceEntity = pendingBalanceEntityDao.getLatestPendingBalanceEntity(accountName).orElseThrow(() -> {
            log.error("No pending balance for user: {}", accountName);
            return new IllegalArgumentException(NO_PENDING_BALANCE);
        });
        if(latestPendingBalanceEntity.getPendingBalance() != 0){
            TransactionEntity transactionEntity = TransactionEntity.builder()
                    .source(settlePendingBalanceRequestDTO.getPayer())
                    .destination(settlePendingBalanceRequestDTO.getPayee())
                    .amount(latestPendingBalanceEntity.getPendingBalance())
                    .purpose(SETTLEMENT)
                    .remarks(settlePendingBalanceRequestDTO.getRemarks())
                    .transactionDate(LocalDate.now())
                    .status(TransactionStatus.CHECKED)
                    .mode(settlePendingBalanceRequestDTO.getMode())
                    .bankAccount(settlePendingBalanceRequestDTO.getBankAccount())
                    .build();
            transactionEntityDao.saveTransaction(transactionEntity);
            savePassBookEntries(transactionEntity);
        }
        List<PendingBalanceEntity> pendingBalanceEntities = pendingBalanceEntityDao.getAllPendingBalancesForAccount(accountName);
        pendingBalanceEntityDao.deleteEntities(pendingBalanceEntities);
        log.info("Settled and deleted pending balances entities for user: {}", accountName);
    }

    @Override
    public List<PendingBalanceResponseDTO> getAllPendingBalancesForAllAccounts() {
        List<PendingBalanceEntity> pendingBalanceEntities = pendingBalanceEntityDao.getAllPendingBalancesForAllAccounts();
        List<PendingBalanceResponseDTO> pendingBalanceResponseDTOS = new ArrayList<>();
        pendingBalanceEntities.forEach(pendingBalanceEntity -> pendingBalanceResponseDTOS.add(
                PendingBalanceResponseDTO.builder()
                        .accountName(pendingBalanceEntity.getAccountName())
                        .transactionDetails(getTransactionRequestDTO(pendingBalanceEntity.getTransactionEntity()))
                        .pendingBalance(pendingBalanceEntity.getPendingBalance())
                        .build()
        ));
        return pendingBalanceResponseDTOS;
    }

    @Override
    public List<PendingBalanceResponseDTO> getPendingBalancesForAccount(String accountName) {
        List<PendingBalanceEntity> pendingBalanceEntities = pendingBalanceEntityDao.getAllPendingBalancesForAccount(accountName);
        List<PendingBalanceResponseDTO> pendingBalanceResponseDTOS = new ArrayList<>();
        pendingBalanceEntities.forEach(pendingBalanceEntity -> pendingBalanceResponseDTOS.add(
                PendingBalanceResponseDTO.builder()
                        .accountName(pendingBalanceEntity.getAccountName())
                        .transactionDetails(getTransactionRequestDTO(pendingBalanceEntity.getTransactionEntity()))
                        .pendingBalance(pendingBalanceEntity.getPendingBalance())
                        .build()
        ));
        return pendingBalanceResponseDTOS;
    }

    private void checkAndUpdatePendingBalances(TransactionEntity transactionEntity, String userId) {
        Optional<PendingBalanceEntity> sourcePendingBalanceEntity = pendingBalanceEntityDao.getLatestPendingBalanceEntity(transactionEntity.getSource());
        Optional<PendingBalanceEntity> destinationPendingBalanceEntity = pendingBalanceEntityDao.getLatestPendingBalanceEntity(transactionEntity.getDestination());
        long pendingAmount;
        Set<String> employeesList = getAllEmployeeNames();
        if(employeesList.contains(transactionEntity.getSource())){
            log.info("Fetching previous pending balance for user: {}", transactionEntity.getSource());
            pendingAmount = sourcePendingBalanceEntity.isPresent() ? sourcePendingBalanceEntity.get().getPendingBalance() : 0;
            PendingBalanceEntity latestPendingBalanceEntityForSource = createPendingBalanceEntity(transactionEntity.getSource(), transactionEntity, pendingAmount - transactionEntity.getAmount(), userId);
            pendingBalanceEntityDao.savePendingBalanceEntity(latestPendingBalanceEntityForSource);
        }
        if(employeesList.contains(transactionEntity.getDestination())){
            log.info("Fetching previous pending balance for user: {}", transactionEntity.getDestination());
            pendingAmount = destinationPendingBalanceEntity.isPresent() ? destinationPendingBalanceEntity.get().getPendingBalance() : 0;
            PendingBalanceEntity latestPendingBalanceEntityForDestination = createPendingBalanceEntity(transactionEntity.getDestination(), transactionEntity, pendingAmount + transactionEntity.getAmount(), userId);
            pendingBalanceEntityDao.savePendingBalanceEntity(latestPendingBalanceEntityForDestination);
        }
    }

    private CreateTransactionRequestDTO getTransactionRequestDTO(TransactionEntity transactionEntity) {
        return CreateTransactionRequestDTO.builder()
                .transactionId(transactionEntity.getId())
                .source(transactionEntity.getSource())
                .destination(transactionEntity.getDestination())
                .amount(transactionEntity.getAmount())
                .mode(transactionEntity.getMode())
                .purpose(transactionEntity.getPurpose())
                .site(transactionEntity.getSite())
                .vehicleNumber(transactionEntity.getVehicleNumber())
                .status(transactionEntity.getStatus())
                .transactionDate(transactionEntity.getTransactionDate())
                .bankAccount(transactionEntity.getBankAccount())
                .remarks(transactionEntity.getRemarks())
                .build();
    }

    public void savePassBookEntries(TransactionEntity transactionEntity) {
        Set<String> employees = getAllEmployeeNames();
        List<PassBookEntity> passBookEntities = createPassBookEntities(transactionEntity, employees, getPreviousPassBookBalance(transactionEntity.getSource()), getPreviousPassBookBalance(transactionEntity.getDestination()));
        passBookEntityDao.savePassBookEntities(passBookEntities);
    }

    private Long getPreviousPassBookBalance(String accountName){
        Optional<PassBookEntity> previousPassBookEntity = passBookEntityDao.getLatestPassBookEntity(accountName);
        return previousPassBookEntity.isPresent() ? previousPassBookEntity.get().getCurrentBalance() : 0L;
    }

    private Set<String> getAllEmployeeNames() {
        Set<String> employeeNames = new HashSet<>();
        employeeNames.addAll(driverEntityDao.getDrivers().stream().map(DriverEntity::getName).collect(Collectors.toSet()));
        employeeNames.addAll(supervisorEntityDao.getAllSupervisors().stream().map(SupervisorEntity::getName).collect(Collectors.toSet()));
        employeeNames.addAll(vendorEntityDao.getAllVendors().stream().map(VendorEntity::getVendorId).collect(Collectors.toSet()));
        return employeeNames;
    }
}
