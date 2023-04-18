package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.SupervisorEntityDao;
import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dao.entities.subentities.Employee;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.NAME;

@Slf4j
public class SupervisorEntityDaoImpl extends AbstractDAO<SupervisorEntity> implements SupervisorEntityDao {

    @Inject
    public SupervisorEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    private void saveOrUpdate(List<SupervisorEntity> supervisorEntities) {
        supervisorEntities.forEach(this::saveOrUpdate);
    }

    private void saveOrUpdate(SupervisorEntity supervisor) {
        this.currentSession().saveOrUpdate(supervisor);
    }

    private final String GET_SUPERVISOR_BY_NAME = "GetEmployeeByName";

    @Override
    public Optional<SupervisorEntity> getEmployee(String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put(NAME, userName);
        log.info("Fetching user: {}", userName);
        return findOneByNamedQuery(GET_SUPERVISOR_BY_NAME, params);
    }

    @Override
    public void saveEmployee(SupervisorEntity supervisor) {
        log.info("Saving user: {}", supervisor.getName());
        persist(supervisor);
    }

    @Override
    public void updateEmployee(SupervisorEntity supervisor) {
        log.info("Updating user: {}", supervisor.getName());
        saveOrUpdate(supervisor);
    }

    @Override
    public void deleteEmployee(SupervisorEntity supervisor){
        currentSession().delete(supervisor);
    }
}
