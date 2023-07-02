package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;
import org.bhavani.constructions.dto.PassBookResponseDTO;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;

public interface TransactionService {
    TransactionEntity createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO, InputStream bill, String userId);

    EnumSet<TransactionMode> getTransactionModes();

    EnumSet<TransactionPurpose> getTransactionPurposes();

    List<CreateTransactionRequestDTO> getAllTransactions();

    List<PassBookResponseDTO> getAllPassBookMainPages();

    List<PassBookResponseDTO> getAccountPassBook(String accountName);

    EnumSet<TransactionStatus> getTransactionStatuses();
}