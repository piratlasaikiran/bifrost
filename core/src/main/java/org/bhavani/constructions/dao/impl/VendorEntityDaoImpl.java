package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.VendorEntityDao;
import org.bhavani.constructions.dao.entities.VendorEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.VENDOR_ID;
import static org.bhavani.constructions.constants.Constants.VENDOR_NAME;

@Slf4j
public class VendorEntityDaoImpl extends AbstractDAO<VendorEntity> implements VendorEntityDao {
    @Inject
    public VendorEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<VendorEntity> getVendorById(String vendorId) {
        Map<String, Object> params = new HashMap<>();
        params.put(VENDOR_ID, vendorId);
        log.info("Fetching vendor: {}", vendorId);
        return findOneByNamedQuery("GetVendorById", params);
    }

    @Override
    public Optional<VendorEntity> getVendorByName(String vendorName) {
        Map<String, Object> params = new HashMap<>();
        params.put(VENDOR_NAME, vendorName);
        log.info("Fetching vendor: {}", vendorName);
        return findOneByNamedQuery("GetVendorByName", params);
    }

    @Override
    public void saveSupervisor(VendorEntity vendorEntity) {
        log.info("Saving vendor: {}", vendorEntity.getVendorId());
        persist(vendorEntity);
    }

    @Override
    public List<VendorEntity> getAllVendors() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllVendors",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
