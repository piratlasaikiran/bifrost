package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.PendingBalanceEntityDao;
import org.bhavani.constructions.dao.entities.PendingBalanceEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.ACCOUNT_NAME;

@Slf4j
public class PendingBalanceEntityDaoImpl extends AbstractDAO<PendingBalanceEntity> implements PendingBalanceEntityDao {

    @Inject
    public PendingBalanceEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<PendingBalanceEntity> getLatestPendingBalanceEntity(String accountName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ACCOUNT_NAME, accountName);
        log.info("Fetching account: {}", accountName);
        return findOneByNamedQuery("GetLatestPendingBalanceForAccountName", params);
    }

    @Override
    public void savePendingBalanceEntity(PendingBalanceEntity pendingBalanceEntity) {
        this.persist(pendingBalanceEntity);
    }

    @Override
    public List<PendingBalanceEntity> getAllPendingBalancesForAccount(String accountName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ACCOUNT_NAME, accountName);
        log.info("Fetching account: {}", accountName);
        return findAllByNamedQuery("GetAllPendingBalanceForAccount",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public List<PendingBalanceEntity> getAllPendingBalancesForAllAccounts() {
        Map<String, Object> params = new HashMap<>();
        log.info("Fetching all pending balances");
        return findAllByNamedQuery("GetLatestPendingBalancesForAllAccounts",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public void deleteEntities(List<PendingBalanceEntity> pendingBalanceEntities) {
        pendingBalanceEntities.forEach(pendingBalanceEntity -> this.currentSession().delete(pendingBalanceEntity));
    }
}
