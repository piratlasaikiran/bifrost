package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.BankAccountEntity;

import java.util.List;

public interface BankAccountEntityDao {
    void saveBankAccount(BankAccountEntity bankAccountEntity);

    List<BankAccountEntity> getBankAccounts();
}
