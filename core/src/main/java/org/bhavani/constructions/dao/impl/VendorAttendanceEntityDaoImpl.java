package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.VendorAttendanceEntityDao;
import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorAttendanceEntityDaoImpl extends AbstractDAO<VendorAttendanceEntity> implements VendorAttendanceEntityDao {

    @Inject
    public VendorAttendanceEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void enterAttendance(VendorAttendanceEntity vendorAttendanceEntity) {
        persist(vendorAttendanceEntity);
    }

    @Override
    public List<VendorAttendanceEntity> getAllVendorAttendance() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllVendorAttendances",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
