package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.EmployeeAttendanceDao;
import org.bhavani.constructions.dao.entities.EmployeeAttendanceEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.EMPLOYEE_NAME;
import static org.bhavani.constructions.constants.Constants.SITE_NAME;

public class EmployeeAttendanceDaoImpl extends AbstractDAO<EmployeeAttendanceEntity> implements EmployeeAttendanceDao {

    @Inject
    public EmployeeAttendanceDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void enterAttendance(EmployeeAttendanceEntity employeeAttendanceEntity) {
        persist(employeeAttendanceEntity);
    }

    @Override
    public List<EmployeeAttendanceEntity> getAllEmployeeAttendance() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllEmployeeAttendances",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public Optional<EmployeeAttendanceEntity> getEmployeeAttendance(Long existingEmployeeAttendanceId) {
        return this.get(existingEmployeeAttendanceId);
    }

    @Override
    public List<EmployeeAttendanceEntity> getEmployeeAttendancesForEmployee(String employeeName) {
        Map<String, Object> params = new HashMap<>();
        params.put(EMPLOYEE_NAME, employeeName);
        return findAllByNamedQuery("GetEmployeeAttendancesForEmployee",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public List<EmployeeAttendanceEntity> getAttendancesInSite(String siteName) {
        Map<String, Object> params = new HashMap<>();
        params.put(SITE_NAME, siteName);
        return findAllByNamedQuery("GetEmployeeAttendancesInSite",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
