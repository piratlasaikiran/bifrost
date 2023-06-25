package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.TransactionEntityDao;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;
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
            transactionRequestDTOS.add(CreateTransactionRequestDTO.builder()
                            .source(transactionEntity.getSource())
                            .destination(transactionEntity.getDestination())
                            .amount(transactionEntity.getAmount())
                            .mode(transactionEntity.getMode())
                            .purpose(transactionEntity.getPurpose())
                            .status(transactionEntity.getStatus())
                            .transactionDate(transactionEntity.getTransactionDate())
                            .bankAccount(transactionEntity.getBankAccount())
                            .remarks(transactionEntity.getRemarks())
                    .build());
        });
        return transactionRequestDTOS;
    }
}
