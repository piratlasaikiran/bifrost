package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.SiteEntity;

import java.util.List;
import java.util.Optional;

public interface SiteEntityDao {
    Optional<SiteEntity> getSite(String siteName);

    void saveSite(SiteEntity siteEntity);

    SiteEntity modifySite(SiteEntity siteEntity);

    void deleteSite(SiteEntity siteEntity);

    List<SiteEntity> getSites();
}
