package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bhavani.constructions.dao.api.PassBookEntityDao;
import org.bhavani.constructions.dao.api.TransactionEntityDao;
import org.bhavani.constructions.dao.entities.PassBookEntity;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;
import org.bhavani.constructions.dto.PassBookResponseDTO;
import org.bhavani.constructions.dto.TransactionStatusChangeDTO;
import org.bhavani.constructions.services.TransactionService;
import org.bhavani.constructions.utils.EntityBuilder;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.bhavani.constructions.constants.Constants.TRANSACTION_STATE_CHANGE_ALLOWANCE;
import static org.bhavani.constructions.constants.ErrorConstants.DOC_PARSING_ERROR;
import static org.bhavani.constructions.constants.ErrorConstants.TRANSACTION_NOT_FOUND;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultTransactionService implements TransactionService {

    private final TransactionEntityDao transactionEntityDao;
    private final PassBookEntityDao passBookEntityDao;

    @Override
    public TransactionEntity createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO, InputStream bill, String userId) {
        try{
            TransactionEntity transactionEntity = EntityBuilder.createTransactionEntity(createTransactionRequestDTO, bill, userId);
            transactionEntityDao.saveTransaction(transactionEntity);
            return transactionEntity;
        }catch (IOException exception){
            log.error("Error while parsing receipt");
            throw new RuntimeException(DOC_PARSING_ERROR);
        }
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
        transactionEntities.forEach(transactionEntity -> {
            transactionRequestDTOS.add(getTransactionRequestDTO(transactionEntity));
        });
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
    public TransactionEntity updateTransaction(CreateTransactionRequestDTO createTransactionRequestDTO, InputStream bill, String userId, Long transactionId) {
        TransactionEntity transactionEntity = transactionEntityDao.getTransaction(transactionId).orElseThrow(() -> {
            log.error("Transaction not found");
             return new RuntimeException(TRANSACTION_NOT_FOUND);
        });
        transactionEntity.setSource(createTransactionRequestDTO.getSource());
        transactionEntity.setDestination(createTransactionRequestDTO.getDestination());
        transactionEntity.setAmount(createTransactionRequestDTO.getAmount());
        transactionEntity.setTransactionDate(createTransactionRequestDTO.getTransactionDate());
        transactionEntity.setPurpose(createTransactionRequestDTO.getPurpose());
        transactionEntity.setMode(createTransactionRequestDTO.getMode());
        transactionEntity.setBankAccount(createTransactionRequestDTO.getBankAccount());
        transactionEntity.setStatus(createTransactionRequestDTO.getStatus());
        transactionEntity.setRemarks(createTransactionRequestDTO.getRemarks());
        try{
            if(Objects.nonNull(bill))
                transactionEntity.setBill(IOUtils.toByteArray(bill));
        }catch (IOException exception){
            log.error("Error while updating transaction document");
            throw new RuntimeException(DOC_PARSING_ERROR);
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
        //ToDo: Handle many things.
        // Automated entry to passbook, pending balance, etc..
    }

    private CreateTransactionRequestDTO getTransactionRequestDTO(TransactionEntity transactionEntity) {
        return CreateTransactionRequestDTO.builder()
                .transactionId(transactionEntity.getId())
                .source(transactionEntity.getSource())
                .destination(transactionEntity.getDestination())
                .amount(transactionEntity.getAmount())
                .mode(transactionEntity.getMode())
                .purpose(transactionEntity.getPurpose())
                .status(transactionEntity.getStatus())
                .transactionDate(transactionEntity.getTransactionDate())
                .bankAccount(transactionEntity.getBankAccount())
                .remarks(transactionEntity.getRemarks())
                .build();
    }
}
