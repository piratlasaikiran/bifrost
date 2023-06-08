package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.UserEntityDao;
import org.bhavani.constructions.dao.entities.UserEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.USER_NAME;


@Slf4j
public class UserEntityDaoImpl extends AbstractDAO<UserEntity> implements UserEntityDao {

    @Inject
    public UserEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void createNewUser(UserEntity userEntity) {
        this.currentSession().persist(userEntity);
    }

    @Override
    public Optional<UserEntity> getUser(String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put(USER_NAME, userName);
        log.info("Fetching user: {}", userName);
        return findOneByNamedQuery("GetPasswordByName", params);
    }
}
