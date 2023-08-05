package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.PendingBalanceEntity;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dto.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    TransactionEntity createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO,
                                        InputStream bill, FormDataContentDisposition billContent,
                                        String userId);

    EnumSet<TransactionMode> getTransactionModes();

    EnumSet<TransactionPurpose> getTransactionPurposes();

    List<CreateTransactionRequestDTO> getAllTransactions();

    List<PassBookResponseDTO> getAllPassBookMainPages();

    List<PassBookResponseDTO> getAccountPassBook(String accountName);

    EnumSet<TransactionStatus> getTransactionStatuses();

    TransactionEntity updateTransaction(CreateTransactionRequestDTO createTransactionRequestDTO,
                                        InputStream bill, FormDataContentDisposition billContent,
                                        String userId, Long transactionId);

    TransactionEntity getTransaction(Long transactionId);

    Map<TransactionStatus, List<TransactionStatus>> getTransactionStatusChangeMapping();

    void changeTransactionStatus(TransactionStatusChangeDTO transactionStatusChangeDTO, String userId);

    void settlePendingBalance(String accountName, SettlePendingBalanceRequestDTO settlePendingBalanceRequestDTO, String userId);

    List<PendingBalanceResponseDTO> getAllPendingBalancesForAllAccounts();

    List<PendingBalanceResponseDTO> getPendingBalancesForAccount(String accountName);
}
