package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.BankAccountEntity;
import org.bhavani.constructions.dto.CreateBankAccountRequestDTO;

import java.util.List;

public interface BankAccountService {
    BankAccountEntity createBankAccount(CreateBankAccountRequestDTO createBankAccountRequestDTO, String userId);

    List<CreateBankAccountRequestDTO> getBankAccounts();

    List<Long> getATMCards();
}
