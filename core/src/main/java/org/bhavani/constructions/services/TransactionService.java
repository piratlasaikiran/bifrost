package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;

import java.io.InputStream;

public interface TransactionService {
    TransactionEntity createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO, InputStream receipt, String userId);
}
