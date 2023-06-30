package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.PassBookEntityDao;
import org.bhavani.constructions.dao.api.TransactionEntityDao;
import org.bhavani.constructions.dao.entities.PassBookEntity;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;
import org.bhavani.constructions.dto.PassBookResponseDTO;
import org.bhavani.constructions.services.TransactionService;
import org.bhavani.constructions.utils.EntityBuilder;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.bhavani.constructions.constants.ErrorConstants.DOC_PARSING_ERROR;

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

    private CreateTransactionRequestDTO getTransactionRequestDTO(TransactionEntity transactionEntity) {
        return CreateTransactionRequestDTO.builder()
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
