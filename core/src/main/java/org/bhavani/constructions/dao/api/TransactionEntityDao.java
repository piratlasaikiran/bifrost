package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.TransactionEntity;

import java.util.List;

public interface TransactionEntityDao {
    void saveTransaction(TransactionEntity transactionEntity);

    List<TransactionEntity> getTransactions();
}
