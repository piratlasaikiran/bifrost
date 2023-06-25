package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.EmployeeAttendanceDao;
import org.bhavani.constructions.dao.entities.EmployeeAttendanceEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public class EmployeeAttendanceDaoImpl extends AbstractDAO<EmployeeAttendanceEntity> implements EmployeeAttendanceDao {

    @Inject
    public EmployeeAttendanceDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void enterAttendance(EmployeeAttendanceEntity employeeAttendanceEntity) {
        persist(employeeAttendanceEntity);
    }
}
