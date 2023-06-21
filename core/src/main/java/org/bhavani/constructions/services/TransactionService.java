package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;

import java.io.InputStream;
import java.util.EnumSet;

public interface TransactionService {
    TransactionEntity createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO, InputStream bill, String userId);

    EnumSet<TransactionMode> getTransactionModes();

    EnumSet<TransactionPurpose> getTransactionPurposes();
}
