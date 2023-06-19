package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.TransactionEntityDao;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public class TransactionEntityDaoImpl extends AbstractDAO<TransactionEntity> implements TransactionEntityDao {

    @Inject
    public TransactionEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void saveTransaction(TransactionEntity transactionEntity) {
        persist(transactionEntity);
    }
}
