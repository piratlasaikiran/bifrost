package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.PendingBalanceEntity;

import java.util.List;
import java.util.Optional;

public interface PendingBalanceEntityDao {

    Optional<PendingBalanceEntity> getLatestPendingBalanceEntity(String accountName);

    void savePendingBalanceEntity(PendingBalanceEntity pendingBalanceEntity);

    List<PendingBalanceEntity> getAllPendingBalancesForAccount(String accountName);

    List<PendingBalanceEntity> getAllPendingBalancesForAllAccounts();

    void deleteEntities(List<PendingBalanceEntity> pendingBalanceEntities);
}
