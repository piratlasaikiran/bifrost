package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.TransactionEntity;

import java.util.List;
import java.util.Optional;

public interface TransactionEntityDao {
    void saveTransaction(TransactionEntity transactionEntity);

    List<TransactionEntity> getTransactions();

    Optional<TransactionEntity> getTransaction(Long transactionId);

    List<TransactionEntity> getTransactionsBySourceName(String sourceName);

    List<TransactionEntity> getTransactionsByDestinationName(String destinationName);

    List<TransactionEntity> getTransactionsBySiteName(String site);

    List<TransactionEntity> getTransactionsByVehicleNumber(String site);
}
