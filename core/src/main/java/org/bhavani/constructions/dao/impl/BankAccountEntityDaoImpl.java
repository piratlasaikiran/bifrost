package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.BankAccountEntityDao;
import org.bhavani.constructions.dao.entities.BankAccountEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAccountEntityDaoImpl extends AbstractDAO<BankAccountEntity> implements BankAccountEntityDao {

    @Inject
    public BankAccountEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void saveBankAccount(BankAccountEntity bankAccountEntity) {
        persist(bankAccountEntity);
    }

    @Override
    public List<BankAccountEntity> getBankAccounts() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllBankAccounts",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
