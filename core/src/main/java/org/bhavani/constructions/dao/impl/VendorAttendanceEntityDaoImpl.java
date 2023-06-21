package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.VendorAttendanceEntityDao;
import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public class VendorAttendanceEntityDaoImpl extends AbstractDAO<VendorAttendanceEntity> implements VendorAttendanceEntityDao {

    @Inject
    public VendorAttendanceEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void enterAttendance(VendorAttendanceEntity vendorAttendanceEntity) {
        persist(vendorAttendanceEntity);
    }
}
