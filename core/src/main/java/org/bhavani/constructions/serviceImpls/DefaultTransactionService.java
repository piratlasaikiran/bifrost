package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.TransactionEntityDao;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;
import org.bhavani.constructions.services.TransactionService;
import org.bhavani.constructions.utils.EntityBuilder;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

import static org.bhavani.constructions.constants.ErrorConstants.DOC_PARSING_ERROR;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultTransactionService implements TransactionService {

    private final TransactionEntityDao transactionEntityDao;

    @Override
    public TransactionEntity createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO, InputStream receipt, String userId) {
        try{
            TransactionEntity transactionEntity = EntityBuilder.createTransactionEntity(createTransactionRequestDTO, receipt, userId);
            transactionEntityDao.saveTransaction(transactionEntity);
            return transactionEntity;
        }catch (IOException exception){
            log.error("Error while parsing receipt");
            throw new RuntimeException(DOC_PARSING_ERROR);
        }
    }
}
