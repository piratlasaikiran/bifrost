package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.PassBookEntityDao;
import org.bhavani.constructions.dao.entities.PassBookEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.ACCOUNT_NAME;

public class PassBookEntityDaoImpl extends AbstractDAO<PassBookEntity> implements PassBookEntityDao {

    @Inject
    public PassBookEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<PassBookEntity> getLatestPassBookEntity(String accountName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ACCOUNT_NAME, accountName);
        return findOneByNamedQuery("GetAccountPassBook", params);
    }

    @Override
    public void savePassBookEntities(List<PassBookEntity> passBookEntities) {
        passBookEntities.forEach(this::persist);
    }

    @Override
    public List<PassBookEntity> getLatestPassBookForAll() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetLatestPassBookEntryForAllAccounts",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public List<PassBookEntity> getAccountPasBook(String accountName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ACCOUNT_NAME, accountName);
        return findAllByNamedQuery("GetAccountPassBook",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
