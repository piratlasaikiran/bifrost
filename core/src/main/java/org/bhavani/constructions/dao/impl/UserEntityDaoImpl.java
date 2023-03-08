package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.UserEntityDao;
import org.bhavani.constructions.dao.entities.UserEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.NAME;

@Slf4j
public class UserEntityDaoImpl extends AbstractDAO<UserEntity> implements UserEntityDao {

    @Inject
    public UserEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    private void saveOrUpdate(List<UserEntity> userEntityList) {
        userEntityList.forEach(this::saveOrUpdate);
    }

    private void saveOrUpdate(UserEntity userEntity) {
        this.currentSession().saveOrUpdate(userEntity);
    }

    private final String GET_USER_BY_NAME = "GetUserByName";

    @Override
    public Optional<UserEntity> getUser(String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put(NAME, userName);
        log.info("Fetching user: {}", userName);
        return findOneByNamedQuery(GET_USER_BY_NAME, params);
    }

    @Override
    public void saveUser(UserEntity userEntity) {
        log.info("Saving user: {}", userEntity.getName());
        persist(userEntity);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        log.info("Updating user: {}", userEntity.getName());
        saveOrUpdate(userEntity);
    }
}
