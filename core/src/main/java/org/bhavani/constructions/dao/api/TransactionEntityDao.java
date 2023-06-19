package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.TransactionEntity;

public interface TransactionEntityDao {
    void saveTransaction(TransactionEntity transactionEntity);
}
