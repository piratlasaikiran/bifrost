package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.SiteEntityDao;
import org.bhavani.constructions.dao.entities.SiteEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.SITE_NAME;

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
}
