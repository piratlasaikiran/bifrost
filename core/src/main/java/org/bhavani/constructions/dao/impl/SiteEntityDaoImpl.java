package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.SiteEntityDao;
import org.bhavani.constructions.dao.entities.SiteEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.*;

@Slf4j
public class SiteEntityDaoImpl extends AbstractDAO<SiteEntity> implements SiteEntityDao {

    @Inject
    public SiteEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<SiteEntity> getSite(String siteName) {
        Map<String, Object> params = new HashMap<>();
        params.put(SITE_NAME, siteName);
        log.info("Fetching site: {}", siteName);
        return findOneByNamedQuery("GetSiteByName", params);
    }

    @Override
    public void saveSite(SiteEntity siteEntity) {
        this.currentSession().save(siteEntity);
    }

    @Override
    public SiteEntity modifySite(SiteEntity siteEntity) {
        this.currentSession().saveOrUpdate(siteEntity);
        return siteEntity;
    }

    @Override
    public void deleteSite(SiteEntity siteEntity) {
        this.currentSession().delete(siteEntity);
    }

    @Override
    public List<SiteEntity> getSites() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllSites",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public List<SiteEntity> getSitesUnderSupervisor(String supervisorName) {
        Map<String, Object> params = new HashMap<>();
        params.put(EMPLOYEE_NAME, supervisorName);
        return findAllByNamedQuery("GetSitesUnderSupervisor",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public List<SiteEntity> getSitesWithVehicle(String vehicleNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put(VEHICLE_NUMBER, vehicleNumber);
        return findAllByNamedQuery("GetSitesWithVehicle",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
