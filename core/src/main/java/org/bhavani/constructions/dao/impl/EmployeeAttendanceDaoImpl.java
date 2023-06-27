package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.EmployeeAttendanceDao;
import org.bhavani.constructions.dao.entities.EmployeeAttendanceEntity;
import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
