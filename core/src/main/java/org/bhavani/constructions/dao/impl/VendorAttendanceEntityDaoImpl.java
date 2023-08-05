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
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.ASSET_NAME;
import static org.bhavani.constructions.constants.Constants.SITE_NAME;

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

    @Override
    public Optional<VendorAttendanceEntity> getVendorAttendance(Long existingVendorAttendanceId) {
        return this.get(existingVendorAttendanceId);
    }

    @Override
    public List<VendorAttendanceEntity> getVendorAttendanceInSite(String siteName) {
        Map<String, Object> params = new HashMap<>();
        params.put(SITE_NAME, siteName);
        return findAllByNamedQuery("GetVendorAttendanceInSite",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
