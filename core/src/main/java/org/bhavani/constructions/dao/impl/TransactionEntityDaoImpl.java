package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.TransactionEntityDao;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionEntityDaoImpl extends AbstractDAO<TransactionEntity> implements TransactionEntityDao {

    @Inject
    public TransactionEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void saveTransaction(TransactionEntity transactionEntity) {
        persist(transactionEntity);
        currentSession().flush();
    }

    @Override
    public List<TransactionEntity> getTransactions() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllTransactions",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public Optional<TransactionEntity> getTransaction(Long transactionId) {
        return this.get(transactionId);
    }
}
