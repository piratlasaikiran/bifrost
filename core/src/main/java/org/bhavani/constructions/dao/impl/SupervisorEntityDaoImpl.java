package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.SupervisorEntityDao;
import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dao.entities.subentities.Employee;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.ATM_CARD;
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

    @Override
    public Optional<SupervisorEntity> getSupervisor(String supervisorName) {
        Map<String, Object> params = new HashMap<>();
        params.put(NAME, supervisorName);
        log.info("Fetching supervisor: {}", supervisorName);
        return findOneByNamedQuery("GetSupervisorByName", params);
    }

    @Override
    public void saveSupervisor(SupervisorEntity supervisor) {
        log.info("Saving supervisor: {}", supervisor.getName());
        persist(supervisor);
    }

    @Override
    public void updateSupervisor(SupervisorEntity supervisor) {
        log.info("Updating user: {}", supervisor.getName());
        saveOrUpdate(supervisor);
    }

    @Override
    public void deleteSupervisor(SupervisorEntity supervisor){
        currentSession().delete(supervisor);
    }

    @Override
    public List<SupervisorEntity> getAllSupervisors() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllSupervisors",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public Optional<SupervisorEntity> getSupervisorByATMCard(Long atmCardNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put(ATM_CARD, atmCardNumber);
        log.info("Fetching supervisor with ATM card: {}", atmCardNumber);
        return findOneByNamedQuery("GetSupervisorByATMCard", params);
    }
}
