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

import static org.bhavani.constructions.constants.Constants.DESTINATION;
import static org.bhavani.constructions.constants.Constants.SOURCE;

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

    @Override
    public List<TransactionEntity> getTransactionsBySourceName(String sourceName) {
        Map<String, Object> params = new HashMap<>();
        params.put(SOURCE, sourceName);
        return findAllByNamedQuery("GetTransactionsBySource",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public List<TransactionEntity> getTransactionsByDestinationName(String destinationName) {
        Map<String, Object> params = new HashMap<>();
        params.put(DESTINATION, destinationName);
        return findAllByNamedQuery("GetTransactionsByDestination",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
